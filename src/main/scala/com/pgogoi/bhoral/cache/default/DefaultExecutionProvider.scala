package com.pgogoi.bhoral.cache.default

import java.util.concurrent.{ ExecutorService, Executors, TimeUnit }

import scala.concurrent.duration.Duration
import scala.concurrent.{ ExecutionContext, ExecutionContextExecutor }

object DefaultExecutionProvider {

  val DefaultDuration = Duration.apply(10, TimeUnit.SECONDS)
  val unboundedES: ExecutorService = Executors.newCachedThreadPool()
  val ioEC: ExecutionContext = ExecutionContext.fromExecutor(unboundedES)

  implicit val boundedECE: ExecutionContextExecutor =
    concurrent.ExecutionContext.global
  implicit val ec: ExecutionContext =
    concurrent.ExecutionContext.Implicits.global
}
