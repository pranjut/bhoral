package com.pgogoi.bhoral.slickOps

import cats.effect.IO
import com.pgogoi.bhoral.vos.UserInfo

import scala.concurrent.Future
import com.pgogoi.bhoral.cache.default.DefaultExecutionProvider.ec
import com.pgogoi.bhoral.slickOps.postgres.SlickPostgresRepo
import com.pgogoi.bhoral.slickcache.postgres.SlickCachePostgresFutureRepo
import com.pgogoi.bhoral.vos.UserInfo

class UserRepo(implicit component: DBComponent)
  extends SlickPostgresRepo[UserInfo, Long, UserOps] {
  override val dBComponent: DBComponent = component
  override val slickOps: UserOps = new UserOps
  import slickOps._
  import jdbcProfile.api._

  def insert(userInfo: UserInfo): Future[UserInfo] =
    write {
      tableQuery.returning(tableQuery.map(primaryColumn)) += userInfo
    } { newId =>
      userInfo.copy(id = newId)
    }

  def getuser(userName: String): Future[Option[UserInfo]] =
    read {
      tableQuery.filter(uin => uin.userName === userName).result
    }.map(_.headOption)

}

class UserCacheRepo(implicit component: DBComponent)
  extends SlickCachePostgresFutureRepo[UserInfo, Long, UserOps] {
  override val dBComponent: DBComponent = component
  override val slickOps: UserOps = new UserOps
  import slickOps._
  import jdbcProfile.api._

  def insert(userInfo: UserInfo): Future[Option[UserInfo]] =
    writeOpt(userInfo.userName) {
      tableQuery.returning(tableQuery.map(primaryColumn)) += userInfo
    } { newId =>
      userInfo.copy(id = newId)
    }

  def getuser(userName: String) =
    readOpt(userName) {
      tableQuery.filter(uin => uin.userName === userName).result
    }

  def insertInSeq(userInfo: UserInfo): Future[Seq[UserInfo]] =
    writeSeq(userInfo.userName) {
      tableQuery.returning(tableQuery.map(primaryColumn)) += userInfo
    } { newId =>
      userInfo.copy(id = newId)
    }

  def getUserFromSeq(userName: String): Future[Seq[UserInfo]] =
    readSeq(userName) {
      tableQuery.filter(uin => uin.userName === userName).result
    }

  def deleteUser(userName: String): Future[Option[Int]] =
    noCaching[Int] {
      tableQuery.filter(uin => uin.userName === userName).delete
    }

  def cleanAllUser: Future[Unit] = {
    cleanAllCache
  }

}
