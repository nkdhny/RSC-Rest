package ru.rsc.rest.service

import ru.rsc.ipmi.chassis.ChassisPowerControl
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import java.net.InetAddress
import ru.rsc.ipmi.chassis.ChassisPowerControl.PowerState
import scala.util.{Success, Try}

/**
 * User: alexey
 * Date: 8/22/13
 * Time: 9:23 PM
 */

object TestPowerControl {
  var asked: Option[String] = None
}

import TestPowerControl._

trait TestPowerControl extends ChassisPowerControl {

  def powerState(addr: InetAddress): Try[PowerState.PowerState] = {

    Success(PowerState.ON)
  }

  protected def powerSet(addr: InetAddress, powerState: PowerState.PowerState): Try[Unit] = {
    asked = Some(addr.getHostName)
    Success(Unit)
  }
}

class IPMIChassisManagementServiceTest extends Specification with Specs2RouteTest
  with IPMIChassisManagementService with TestPowerControl {

  def actorRefFactory = system

  "IPMIRackManagementService" should {
    "respond with OK to power status request" in {

      Get("/power/ya.ru") ~> rackMgmtRoute ~> check {

        entityAs[String] must contain("OK")

      }
    }

    "Ask a service to turn chassis off when it is powered on" in {
      Put("/power/ya.ru/off") ~> rackMgmtRoute ~> check {
        asked.get === "ya.ru"
        entityAs[String] must contain("OK")
      }
    }

    "Respond with error when asked to turn on powered chassis" in {
      Put("/power/ya.ru/on") ~> rackMgmtRoute ~> check {
        entityAs[String] must contain("FAILED")
      }
    }
  }

}
