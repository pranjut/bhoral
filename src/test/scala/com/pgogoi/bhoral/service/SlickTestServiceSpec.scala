package com.pgogoi.bhoral.service

import com.pgogoi.bhoral.SlickSpec
import com.pgogoi.bhoral.slickOps.UserRepo
import com.pgogoi.bhoral.vos.UserInfo
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class SlickTestServiceSpec
  extends SlickSpec
  with ScalaFutures
  with BeforeAndAfterEach {

  val userInfo = UserInfo(0, "pranjut", Some("password"))

  val action = new UserRepo

  override def beforeAll(): Unit = {
    Await.result(action.createSchema, 1.minute)
  }

  override def afterAll(): Unit = {
    Await.result(action.deleteSchema, 1.minute)
  }

  it should "be able to insert into the table" in {
    val result = action.insert(userInfo)
    result.map { res =>
      res shouldEqual (UserInfo(1, "pranjut", Some("password")))
    }
  }

  it should "be able to insert and increment the id" in {
    for {
      result <- action.insert(userInfo)
      result2 <- action.insert(userInfo)
    } yield {
      result shouldEqual (UserInfo(2, "pranjut", Some("password")))
      result2 shouldEqual (UserInfo(3, "pranjut", Some("password")))
    }
  }

  it should "be able to get user by username" in {
    for {
      _ <- action.insert(userInfo)
      _ <- action.insert(userInfo)
      result3 <- action.getuser(userInfo.userName)
    } yield {
      result3 shouldEqual Some(userInfo.copy(id = 1))
    }

  }

}
