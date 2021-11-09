package com.pgogoi.bhoral.slickOps.postgres

import com.pgogoi.bhoral.slickOps.{ SlickOps, SlickRepository }
import slick.dbio.Effect
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ ExecutionContext, Future }

trait SlickPostgresRepo[Entity, Id, Ops <: SlickOps[Entity, Id]]
  extends SlickRepository[Entity, Id, Ops] {

  type WriteType = DBIOAction[Id, NoStream, Effect.Read with Effect.Write]
  type ReadType = DBIOAction[Seq[Entity], Streaming[Entity], Effect.Read]

  def customRun[T](
    action: DBIOAction[T, NoStream, Effect.Read with Effect.Write]): Future[T] = {
    db.run(action.transactionally)
  }

  def write(
    action: WriteType)(copy: Id => Entity)(implicit ec: ExecutionContext) = {
    db.run(action.transactionally).map(copy(_))
  }

  def read(action: ReadType): Future[Seq[Entity]] = {
    db.run(action.transactionally)
  }

  def createSchema: Future[Unit] = db.run(slickOps.tableQuery.schema.create)
  def deleteSchema: Future[Unit] =
    db.run(slickOps.tableQuery.schema.dropIfExists)

}
