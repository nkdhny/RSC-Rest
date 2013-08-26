package ru.rsc.ipmi.chassis

import java.net.InetAddress
import scala.util.Try
import ru.rsc.ipmi.chassis.ChassisPowerControl.PowerState

/**
 * User: alexey
 * Date: 8/22/13
 * Time: 9:19 PM
 */
object ChassisPowerControl {
  object PowerState extends Enumeration {
    type PowerState = Value
    val ON, OFF, UNDEFINED = Value
  }
}

trait ChassisPowerControl {

  import ChassisPowerControl.PowerState._

  def powerState(addr: InetAddress): Try[PowerState]
  protected def powerSet(addr: InetAddress, powerState: PowerState.PowerState): Try[Unit]
  def on(addr: InetAddress): Try[Unit] = powerSet(addr, PowerState.ON)
  def off(addr: InetAddress): Try[Unit] = powerSet(addr, PowerState.OFF)
}