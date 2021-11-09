package com.pgogoi.bhoral.slickOps

trait SlickRepository[Entity, Id, Ops <: SlickOps[Entity, Id]] {

  val dBComponent: DBComponent

  val slickOps: Ops

  def db: dBComponent.driver.api.Database = dBComponent.db

}
