package com.pgogoi.bhoral.slickOps.postgres

import com.pgogoi.bhoral.slickOps.DBComponent
import com.typesafe.config.ConfigFactory
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

object PostgresDBComponent {

  private val config = ConfigFactory.load()

  val databaseConfig =
    DatabaseConfig.forConfig[JdbcProfile]("postgres.config", config)

  implicit val postgresComponent = new DBComponent {
    override val driver: JdbcProfile = databaseConfig.profile
    import driver.api._
    override val db: Database = databaseConfig.db
  }

}
