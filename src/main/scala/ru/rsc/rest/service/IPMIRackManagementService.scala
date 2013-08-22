package ru.rsc.rest.service

import spray.routing.HttpService
import spray.http.MediaTypes._
import akka.actor.Actor
import ru.rsc.ipmi.rack.RackPowerControl

/**
 * User: alexey
 * Date: 8/22/13
 * Time: 8:53 PM
 */

abstract class IPMIRackManagementServiceActor extends Actor with IPMIRackManagementService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(rackMgmtRoute)
}



trait IPMIRackManagementService extends HttpService with RackPowerControl{

  val rackMgmtRoute =
    path("power" / Segment ) { addr => {
      get {
        respondWithMediaType(`application/json`) {
          complete {
               powerCycle(addr)
               "{result: OK}"
          }
        }
      }
    }
  }
}
