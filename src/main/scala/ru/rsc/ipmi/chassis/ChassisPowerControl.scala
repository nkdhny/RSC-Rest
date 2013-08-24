package ru.rsc.ipmi.chassis

import java.net.InetAddress

/**
 * User: alexey
 * Date: 8/22/13
 * Time: 9:19 PM
 */
object ChassisPowerControl {
  object PowerState extends Enumeration {
    type PowerState = Value
    val ON, OFF = Value
  }
}

trait ChassisPowerControl {

  import ChassisPowerControl.PowerState._

  def powerCycle(addr: InetAddress)
  def powerState(addr: InetAddress): Option[PowerState]
}