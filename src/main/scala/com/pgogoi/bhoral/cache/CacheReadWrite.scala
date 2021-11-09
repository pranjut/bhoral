package com.pgogoi.bhoral.cache

import cats.Monad
import cats.data.OptionT
import scalacache.caffeine.CaffeineCache
import scalacache.{ Flags, Mode }

import scala.concurrent.duration.Duration
import cats.implicits._

trait CacheReadWrite[F[_]] {

  val cacheMiss: String => Any

  private implicit val writeEnabledFlag: Flags =
    Flags(readsEnabled = false, writesEnabled = true)

  def writeCache[T](
    key: String,
    ttl: Option[Duration],
    fn: => F[T],
    cache: CaffeineCache[T])(implicit mode: Mode[F]): F[T] = {
    cache.cachingForMemoizeF(key)(ttl)(fn)
  }

  def readCache[T](
    key: String,
    ttl: Option[Duration],
    fn: => F[T],
    cache: CaffeineCache[T])(implicit mode: Mode[F], monad: Monad[F]): F[Option[T]] = {
    OptionT(cache.doGet(key)).flatTapNone(writeCache(key, ttl, fn, cache)).value
  }

  def readOnlyFromCache[T](
    key: String,
    cache: CaffeineCache[T])(implicit mode: Mode[F], monad: Monad[F]): F[Option[T]] = {
    cache.doGet(key)
  }

  def deleteCache[T](key: String, cache: CaffeineCache[T])(
    implicit
    mode: Mode[F]): F[Any] = {
    cache.doRemove(key)
  }

  def cleanCache[T](cache: CaffeineCache[T])(implicit mode: Mode[F]): F[Any] = {
    cache.doRemoveAll()
  }

}
