package com.pgogoi.bhoral.cache

trait ReadWriteProrvider[F[_]] {

  def isReadEnabled: F[Boolean]

  def isWriteEnabled: F[Boolean]

}
