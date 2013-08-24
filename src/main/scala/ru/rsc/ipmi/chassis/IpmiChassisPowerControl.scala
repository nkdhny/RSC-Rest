package ru.rsc.ipmi.chassis

import ru.rsc.ipmi.common.CredentialsProvider
import com.veraxsystems.vxipmi.coding.commands.{ResponseData, IpmiVersion, PrivilegeLevel, IpmiCommandCoder}
import com.veraxsystems.vxipmi.api.sync.IpmiConnector
import java.net.InetAddress
import com.veraxsystems.vxipmi.coding.commands.chassis.{GetChassisStatusResponseData, GetChassisStatus, PowerCommand, ChassisControl}
import com.veraxsystems.vxipmi.coding.security.CipherSuite
import collection.JavaConversions._
import com.veraxsystems.vxipmi.coding.protocol.AuthenticationType
import ru.rsc.ipmi.chassis.ChassisPowerControl.PowerState

/**
 * User: alexey
 * Date: 8/23/13
 * Time: 10:32 PM
 */
trait IpmiChassisPowerControl extends ChassisPowerControl with CredentialsProvider {

  def defaultPort: Int
  lazy val connectionManager = new IpmiConnector(defaultPort)

  private def doInAFreshSession(whatToDo: CipherSuite => IpmiCommandCoder): InetAddress => ResponseData = addr => {
    val connection = connectionManager.createConnection(addr)

    val availableCipherSites = connectionManager.getAvailableCipherSuites(connection)

    val cipherSuite = availableCipherSites.toList match {
      case h::t => {
        connectionManager.getChannelAuthenticationCapabilities(connection, h, PrivilegeLevel.MaximumAvailable)
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

  def powerCycle(addr: InetAddress) {
    val command  = powerCommandTemplate(_: CipherSuite, PowerCommand.HardReset)
    doInAFreshSession(command)(addr)
  }

  def powerState(addr: InetAddress) = {
    val commmand = new GetChassisStatus(IpmiVersion.V20, _: CipherSuite, AuthenticationType.RMCPPlus)
    doInAFreshSession(commmand)(addr) match {
      case chassisStatus: GetChassisStatusResponseData if chassisStatus.isPowerOn => Some(PowerState.ON)
      case chassisStatus: GetChassisStatusResponseData if !chassisStatus.isPowerOn => Some(PowerState.OFF)
      case _ => None
    }
  }
}



