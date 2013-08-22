package ru.rsc.ipmi.rack

/**
 * User: alexey
 * Date: 8/22/13
 * Time: 9:19 PM
 */
trait RackPowerControl {

  def powerCycle(addr: String)

}

trait StubRackPowerControl extends RackPowerControl {
  def powerCycle(addr: String) {
    //do nothing
  }
}
