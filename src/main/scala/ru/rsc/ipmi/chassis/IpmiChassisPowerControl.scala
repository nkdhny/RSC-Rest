package ru.rsc.ipmi.chassis

import ru.rsc.ipmi.common.CredentialsProvider
import com.veraxsystems.vxipmi.coding.commands.{ResponseData, IpmiVersion, PrivilegeLevel, IpmiCommandCoder}
import com.veraxsystems.vxipmi.api.sync.IpmiConnector
import java.net.InetAddress
import com.veraxsystems.vxipmi.coding.commands.chassis.{GetChassisStatusResponseData, GetChassisStatus, PowerCommand, ChassisControl}
import com.veraxsystems.vxipmi.coding.security.{AuthenticationRakpNone, CipherSuite}
import collection.JavaConversions._
import com.veraxsystems.vxipmi.coding.protocol.AuthenticationType
import ru.rsc.ipmi.chassis.ChassisPowerControl.PowerState
import scala.util.Try

/**
 * User: alexey
 * Date: 8/23/13
 * Time: 10:32 PM
 */
trait IpmiChassisPowerControl extends ChassisPowerControl with CredentialsProvider {

  def defaultPort: Int = 8386
  lazy val connectionManager = new IpmiConnector(defaultPort)

  private def doInAFreshSession(whatToDo: CipherSuite => IpmiCommandCoder): InetAddress => ResponseData = addr => {
    val connection = connectionManager.createConnection(addr)

    def goodAlgorythm: CipherSuite => Boolean = c => {
      Try(!c.getAuthenticationAlgorithm.isInstanceOf[AuthenticationRakpNone]).toOption
                                                                             .getOrElse(false)
    }

    val availableCipherSites = connectionManager.getAvailableCipherSuites(connection)
                                                .toList
                                                .filter(goodAlgorythm)
    val cipherSuite = availableCipherSites match {
      case h::t => {
        connectionManager.getChannelAuthenticationCapabilities(connection, h, PrivilegeLevel.Administrator)
        h
      }
    }

    connectionManager.openSession(connection, getLogin(), getPassword(), null)
    val data = connectionManager.sendMessage(connection, whatToDo(cipherSuite))
    connectionManager.closeSession(connection)
    connectionManager.closeConnection(connection)

    data
  }

  private def powerCommandTemplate :
    (CipherSuite, PowerCommand) => IpmiCommandCoder = new ChassisControl(IpmiVersion.V20, _, AuthenticationType.RMCPPlus, _)

  def powerState(addr: InetAddress) = {
    val command = new GetChassisStatus(IpmiVersion.V20, _: CipherSuite, AuthenticationType.RMCPPlus)
    Try{
      doInAFreshSession(command)(addr) match {
        case chassisStatus: GetChassisStatusResponseData if chassisStatus.isPowerOn => PowerState.ON
        case chassisStatus: GetChassisStatusResponseData if !chassisStatus.isPowerOn => PowerState.OFF
      }
    }
  }

  protected def powerSet(addr: InetAddress, powerState: PowerState.PowerState): Try[Unit] = {
    val command  = powerCommandTemplate(_: CipherSuite, if(powerState == PowerState.ON) PowerCommand.PowerUp else PowerCommand.PowerDown)
    Try(doInAFreshSession(command)(addr))
  }
}



