package ru.rsc.rest.service

import spray.routing.HttpService
import spray.http.MediaTypes._
import akka.actor.Actor
import ru.rsc.ipmi.chassis.ChassisPowerControl
import java.net.InetAddress
import ru.rsc.ipmi.chassis.ChassisPowerControl.PowerState
import scala.util.{Failure, Try}

/**
 * User: alexey
 * Date: 8/22/13
 * Time: 8:53 PM
 */

abstract class IPMIChassisManagementServiceActor extends Actor with IPMIChassisManagementService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(rackMgmtRoute)
}



trait IPMIChassisManagementService extends HttpService with ChassisPowerControl{

  val rackMgmtRoute =
    path("power" / Segment / Segment) { (addr, action) => {
      val resolvedAddr = InetAddress.getByName(addr)
      lazy val currentState = powerState(resolvedAddr).toOption.getOrElse(PowerState.UNDEFINED)

      get {
        respondWithMediaType(`application/json`) {
          complete {
               s"""{
                      result: OK,
                      power: ${currentState}
               }"""
          }
        }
      }
      put{
        val resp = action match {
          case "on" => toggleChassis(on, currentState, resolvedAddr, PowerState.ON::PowerState.UNDEFINED::Nil)
          case "off" => toggleChassis(off, currentState, resolvedAddr, PowerState.OFF::PowerState.UNDEFINED::Nil)
          case _ => respondWithError(new IllegalArgumentException(s"No such action ${action}"))
        }
          respondWithMediaType(`application/json`) {
            complete {
              resp
            }
          }
        }
      }
  }


  private def toggleChassis(action: InetAddress => Try[Unit],
                            currentState: ChassisPowerControl.PowerState.Value,
                            resolvedAddr: InetAddress,
                            notAllowedStates: Seq[PowerState.PowerState]): String = {
    if (notAllowedStates.contains(currentState)) {
      respondWithError(new IllegalStateException("Chassis must be turned off before turning it on"))
    } else {
      Try(action(resolvedAddr)) match {
        case Failure(t: Throwable) => respondWithError(t)
        case _ => "{result: OK}"
      }
    }
  }

  private def respondWithError(t: Throwable) = {
    s"""{result: FAILED, cause: ${t.getMessage}"""
  }
}
