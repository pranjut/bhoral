package com.pgogoi.bhoral.slickcache.postgres

import com.pgogoi.bhoral.cache.default.DefaultExecutionProvider.ec
import com.pgogoi.bhoral.cache.default.DefaultFutureRepositoryWithCache
import com.pgogoi.bhoral.slickOps.SlickOps
import com.pgogoi.bhoral.slickOps.postgres.SlickPostgresRepo
import slick.dbio.Effect
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ ExecutionContext, Future }

trait SlickCachePostgresFutureRepo[Entity, Id, Ops <: SlickOps[Entity, Id]]
  extends SlickPostgresRepo[Entity, Id, Ops]
  with DefaultFutureRepositoryWithCache[Entity] {

  def writeSeq(key: String)(
    action: WriteType)(copy: Id => Entity)(implicit ec: ExecutionContext): Future[Seq[Entity]] =
    withWriteCacheSequential(key) { existingEntity =>
      super.write(action)(copy).map(newEnt => existingEntity :+ newEnt)
    }

  def noCaching[T](
    action: DBIOAction[T, NoStream, Effect.Read with Effect.Write]): Future[Option[T]] = {
    super.customRun[T](action).map(Option(_))
  }

  def readSeq(key: String)(action: ReadType): Future[Seq[Entity]] = {
    withReadCacheSequential(key)(super.read(action))
  }

  def writeOpt(key: String)(action: WriteType)(
    copy: Id => Entity)(implicit ec: ExecutionContext): Future[Option[Entity]] = {
    withWriteCacheOptional(key) {
      super.write(action)(copy).map(Option(_))
    }
  }

  def readOpt(key: String)(action: ReadType): Future[Option[Entity]] = {
    withReadCacheOptional(key)(super.read(action).map(_.headOption))
  }

}
