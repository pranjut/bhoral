package com.pgogoi.bhoral.slickOps

import slick.jdbc.JdbcProfile

trait DBComponent {
  val driver: JdbcProfile
  import driver.api._
  val db: Database
}

trait DBComponentProfile {
  val dBComponent: DBComponent
}
