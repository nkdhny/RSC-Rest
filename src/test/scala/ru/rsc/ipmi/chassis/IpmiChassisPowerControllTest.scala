package ru.rsc.ipmi.chassis

import org.scalatest.FunSuite
import ru.rsc.ipmi.common.StubCredentialsProvider
import java.net.InetAddress
import ru.rsc.ipmi.chassis.ChassisPowerControl.PowerState

/**
 * User: alexey
 * Date: 8/24/13
 * Time: 12:59 PM
 */
class IpmiChassisPowerControlTest extends FunSuite {

  test("Power status must be defined"){
    val powerControl = new IpmiChassisPowerControl with StubCredentialsProvider {
      def defaultPort: Int = 8623
    }

    val addr = InetAddress.getByName("msk.rsc-tech.ru")

    assert(powerControl.powerState(addr).exists(PowerState.values.contains))
  }

}
