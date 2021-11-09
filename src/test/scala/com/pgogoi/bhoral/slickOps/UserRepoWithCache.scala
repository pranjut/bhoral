package com.pgogoi.bhoral.slickOps

import com.pgogoi.bhoral.cache.default.DefaultExecutionProvider.ec
import com.pgogoi.bhoral.slickOps.postgres.SlickPostgresRepo
import com.pgogoi.bhoral.vos.UserInfo

import scala.concurrent.Future

class UserRepoWithCache(implicit component: DBComponent)
  extends SlickPostgresRepo[UserInfo, Long, UserOps] {
  override val dBComponent: DBComponent = component
  override val slickOps: UserOps = new UserOps

  import slickOps._
  import jdbcProfile.api._

  def insert(userInfo: UserInfo): Future[UserInfo] =
    write {
      tableQuery.returning(tableQuery.map(primaryColumn)) += userInfo
    } { resultId =>
      userInfo.copy(id = resultId)
    }

  def getuser(userName: String): Future[Option[UserInfo]] =
    read {
      tableQuery.filter(uin => uin.userName === userName).result
    }.map(_.headOption)

}
