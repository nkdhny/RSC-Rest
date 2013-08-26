package ru.rsc.ipmi.chassis

import org.scalatest.FunSuite
import ru.rsc.ipmi.common.StubCredentialsProvider
import java.net.InetAddress
import ru.rsc.ipmi.chassis.ChassisPowerControl.PowerState
import scala.util.{Failure, Success}

/**
 * User: alexey
 * Date: 8/24/13
 * Time: 12:59 PM
 */
class IpmiChassisPowerControlTest extends FunSuite {

  val powerControl = new IpmiChassisPowerControl with StubCredentialsProvider {
    def defaultPort: Int = 8623
  }
  val addr = InetAddress.getByName("msk.rsc-tech.ru")
  val sleep = 1000


  test("Power status must be defined"){
    assert(getPowerStateAndPrintIt.isDefined)
  }


  private def getPowerStateAndPrintIt: Option[ChassisPowerControl.PowerState.Value] = {
    val m = powerControl.powerState(addr) match {
      case Success(power: PowerState.PowerState) => Some(power)
      case Failure(t: Throwable) => throw t
    }
    println(s"Test chassis status is: ${m.getOrElse("Undefined")}")
    m
  }

  test("Power state must be changed"){
    val currentState = powerControl.powerState(addr).toOption
    assert(currentState.isDefined)
    for(st <- currentState) {
      if(st == PowerState.ON) powerControl.off(addr) else powerControl.on(addr)
    }

    while (!getPowerStateAndPrintIt.isDefined){
      Thread.sleep(sleep)
    }

    val changedState = powerControl.powerState(addr).toOption
    assert(currentState != changedState)
  }

}
