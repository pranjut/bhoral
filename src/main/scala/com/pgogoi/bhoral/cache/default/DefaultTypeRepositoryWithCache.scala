package com.pgogoi.bhoral.cache.default

import java.util.concurrent.ExecutorService

import cats.effect.IO
import com.pgogoi.bhoral.cache.{ CacheReadWrite, GeneralCaching }
import com.typesafe.scalalogging.LazyLogging
import scalacache.Mode

import scala.concurrent.Future
import scala.concurrent.duration.Duration

trait DefaultIORepositoryWithCache[Entity]
  extends GeneralCaching[Entity]
  with CacheReadWrite[IO]
  with LazyLogging {

  import DefaultExecutionProvider._
  implicit val mode: Mode[IO] = scalacache.CatsEffect.modes.async

  def ttl: Option[Duration] = None

  override val cacheMiss: String => Any = key =>
    logger.warn(s"cache miss in ${getClass} repo for key $key")

  override val unboundedExecutor: ExecutorService = unboundedES

  def withReadCacheSequential(
    key: String)(fn: => IO[Seq[Entity]]): IO[Seq[Entity]] = {
    super.readCache(key, seqTtl, fn, cacheSequence).map(_.getOrElse(Seq()))
  }

  def withWriteCacheSequential(
    key: String)(fn: Seq[Entity] => IO[Seq[Entity]]): IO[Seq[Entity]] = {
    super.readOnlyFromCache(key, cacheSequence).flatMap {
      case Some(value) =>
        super.writeCache[Seq[Entity]](key, seqTtl, fn(value), cacheSequence)
      case None =>
        super.writeCache[Seq[Entity]](key, seqTtl, fn(Seq()), cacheSequence)
    }

  }

  def withReadCacheOptional(
    key: String)(fn: => IO[Option[Entity]]): IO[Option[Entity]] = {
    super.readCache(key, optTtl, fn, cacheOptional).map(_.flatten)
  }

  def withWriteCacheOptional[T](
    key: String)(fn: => IO[Option[Entity]]): IO[Option[Entity]] = {
    super.writeCache[Option[Entity]](key, seqTtl, fn, cacheOptional)
  }

  def deleteCacheOptional(key: String): IO[Any] = {
    super.deleteCache(key, cacheOptional)
  }

  def deleteCacheSequential(key: String): IO[Any] = {
    super.deleteCache(key, cacheSequence)
  }

  def cleanAllCache: Unit = {
    (for {
      _ <- super.cleanCache(cacheSequence)
      _ <- super.cleanCache(cacheOptional)
    } yield ()).unsafeRunSync()

  }
}

trait DefaultFutureRepositoryWithCache[Entity]
  extends GeneralCaching[Entity]
  with CacheReadWrite[Future]
  with LazyLogging {

  import DefaultExecutionProvider._
  import scalacache.modes.scalaFuture._

  def ttl: Option[Duration] = None

  override val cacheMiss: String => Any = key =>
    logger.warn(s"cache miss in ${getClass} repo for key $key")

  override val unboundedExecutor: ExecutorService = unboundedES

  def withReadCacheSequential(
    key: String)(fn: => Future[Seq[Entity]]): Future[Seq[Entity]] = {
    super.readCache(key, seqTtl, fn, cacheSequence).map(_.getOrElse(Seq()))
  }

  def withWriteCacheSequential(
    key: String)(fn: Seq[Entity] => Future[Seq[Entity]]): Future[Seq[Entity]] = {
    super.readOnlyFromCache(key, cacheSequence).flatMap {
      case Some(value) =>
        super.writeCache[Seq[Entity]](key, seqTtl, fn(value), cacheSequence)
      case None =>
        super.writeCache[Seq[Entity]](key, seqTtl, fn(Seq()), cacheSequence)
    }

  }

  def withReadCacheOptional(
    key: String)(fn: => Future[Option[Entity]]): Future[Option[Entity]] = {
    super.readCache(key, optTtl, fn, cacheOptional).map(_.flatten)
  }

  def withWriteCacheOptional(
    key: String)(fn: => Future[Option[Entity]]): Future[Option[Entity]] = {
    super.writeCache[Option[Entity]](key, seqTtl, fn, cacheOptional)
  }

  def withReadCache(
    key: String)(fn: => Future[Seq[Entity]]): Future[Seq[Entity]] = {
    super.readCache(key, seqTtl, fn, cacheSequence).map(_.getOrElse(Seq()))
  }

  def deleteCacheOptional(key: String): Future[Any] = {
    super.deleteCache(key, cacheOptional)
  }

  def deleteCacheSequence(key: String): Future[Any] = {
    super.deleteCache(key, cacheSequence)
  }

  def cleanAllCache: Future[Unit] = {
    (for {
      _ <- super.cleanCache(cacheSequence)
      _ <- super.cleanCache(cacheOptional)
    } yield ())

  }

}
