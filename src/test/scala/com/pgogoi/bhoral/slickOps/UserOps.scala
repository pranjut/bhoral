package com.pgogoi.bhoral.slickOps

import com.pgogoi.bhoral.vos.UserInfo
import com.pgogoi.bhoral.vos.UserInfo
import slick.jdbc.JdbcProfile

class UserOps(implicit db: DBComponent) extends SlickOps[UserInfo, Long] {
  override val jdbcProfile: JdbcProfile = db.driver
  import jdbcProfile.api._

  override type EntityTable = UserInfoTable

  override def primaryColumn(table: EntityTable): Rep[Long] = table.id

  override def tableQuery: TableQuery[UserInfoTable] =
    TableQuery[UserInfoTable]

  class UserInfoTable(tag: Tag) extends Table[UserInfo](tag, "user_info") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def userName = column[String]("user_name")

    def password = column[String]("password")

    override def * = (id, userName, password.?).mapTo[UserInfo]
  }

}
