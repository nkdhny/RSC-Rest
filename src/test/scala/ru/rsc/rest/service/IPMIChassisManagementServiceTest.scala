package ru.rsc.rest.service

import ru.rsc.ipmi.chassis.ChassisPowerControl
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import java.net.InetAddress
import ru.rsc.ipmi.chassis.ChassisPowerControl.PowerState

/**
 * User: alexey
 * Date: 8/22/13
 * Time: 9:23 PM
 */

object TestPowerControl {
  var rackCycled: Option[String] = None
}

import TestPowerControl._

trait TestPowerControl extends ChassisPowerControl {
  def powerCycle(addr: InetAddress) {
    rackCycled = Some(addr.getHostName)
  }

  def powerState(addr: InetAddress): Option[PowerState.PowerState] = None
}

class IPMIChassisManagementServiceTest extends Specification with Specs2RouteTest
  with IPMIChassisManagementService with TestPowerControl {

  def actorRefFactory = system

  "IPMIRackManagementService" should {
    "respond with OK to power cycle request and call the power control service" in {

      Get("/power/ya.ru") ~> rackMgmtRoute ~> check {

        entityAs[String] must contain("OK")
        rackCycled === Some("ya.ru")

      }
    }
  }

}
