package com.pgogoi.bhoral.slickOps

import slick.jdbc.JdbcProfile
import slick.relational.RelationalProfile

trait SlickOps[Entity, Id] {

  val jdbcProfile: JdbcProfile

  import jdbcProfile.api._

  type EntityTable <: RelationalProfile#Table[Entity]

  def tableQuery: TableQuery[EntityTable]

  def primaryColumn(table: EntityTable): Rep[Id]

}
