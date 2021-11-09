package com.pgogoi.bhoral

import java.io.File
import java.util.concurrent.{ ExecutorService, Executors }

import com.pgogoi.bhoral.slickOps.DBComponent
import com.typesafe.config.ConfigFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.{ AnyFlatSpecLike, AsyncFlatSpec }
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{ Millis, Seconds, Span }
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait SlickSpec
  extends AsyncFlatSpec
  with Matchers
  with BeforeAndAfterAll
  with ScalaFutures {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  val unboundedExecutor: ExecutorService = Executors.newCachedThreadPool()
  private val defaultConfigString =
    """
  h2mem {
  profile = "slick.jdbc.H2Profile$"
  db {
    url = "jdbc:h2:mem:contacting;DB_CLOSE_DELAY=-1"
    driver = "org.h2.Driver"
    connectionPool = "HikariCP"
  }
  }
  """

  private val config = ConfigFactory
    .parseFile(new File("src/test/resources/application.conf"))
    .withFallback(ConfigFactory.parseString(defaultConfigString))

  val databaseConfig = DatabaseConfig.forConfig[JdbcProfile]("h2mem", config)

  implicit val h2DBComponent = new DBComponent {
    override val driver: JdbcProfile = databaseConfig.profile
    override val db: driver.api.Database = databaseConfig.db
  }

  val jdbcProfile = h2DBComponent.driver

}
