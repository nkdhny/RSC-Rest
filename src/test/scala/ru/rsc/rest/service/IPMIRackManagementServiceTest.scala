package ru.rsc.rest.service

import ru.rsc.ipmi.rack.RackPowerControl
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest

/**
 * User: alexey
 * Date: 8/22/13
 * Time: 9:23 PM
 */

object TestPowerControl {
  var rackCycled: Option[String] = None
}

import TestPowerControl._

trait TestPowerControl extends RackPowerControl {
  def powerCycle(addr: String) {
    rackCycled = Some(addr)
  }
}

class IPMIRackManagementServiceTest extends Specification with Specs2RouteTest
  with IPMIRackManagementService with TestPowerControl {

  def actorRefFactory = system

  "IPMIRackManagementService" should {
    "respond with OK to power cycle request and call the power control service" in {

      Get("/power/sample") ~> rackMgmtRoute ~> check {

        entityAs[String] must contain("OK")
        rackCycled === Some("sample")

      }
    }
  }

}
