package com.pgogoi.bhoral.cache

import java.util.concurrent.ExecutorService

import com.github.benmanes.caffeine.cache.Caffeine
import com.typesafe.scalalogging.LazyLogging
import scalacache.Entry
import scalacache.caffeine.CaffeineCache

import scala.concurrent.duration.Duration

trait GeneralCaching[Entity] extends LazyLogging {

  /**
   * Override it for logging like below
   * e.g. val cacheMiss: String => Any = key =>
   *     logger.warn(s"cache miss in ${getClass} repo for key $key")
   */
  val cacheMiss: String => Any

  protected def ttl: Option[Duration]

  protected val seqTtl: Option[Duration] = ttl
  protected val optTtl: Option[Duration] = ttl

  def unboundedExecutor: ExecutorService

  implicit lazy val cacheSequence =
    CaffeineCache[Seq[Entity]](
      Caffeine
        .newBuilder()
        .executor(unboundedExecutor)
        .build[String, Entry[Seq[Entity]]]())

  implicit lazy val cacheOptional =
    CaffeineCache[Option[Entity]](
      Caffeine
        .newBuilder()
        .executor(unboundedExecutor)
        .build[String, Entry[Option[Entity]]]())

}
