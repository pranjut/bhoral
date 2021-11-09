package com.pgogoi.bhoral.service

import com.pgogoi.bhoral.SlickSpec
import com.pgogoi.bhoral.slickOps.UserCacheRepo
import com.pgogoi.bhoral.vos.UserInfo
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class SlickCacheTestServiceSpec
  extends SlickSpec
  with ScalaFutures
  with BeforeAndAfterEach {

  val userInfo = UserInfo(0, "pranjut", Some("password"))

  val action = new UserCacheRepo

  override def beforeAll(): Unit = {
    Await.result(action.createSchema, 1.minute)
  }

  override def afterAll(): Unit = {
    Await.result(action.deleteSchema, 1.minute)
  }

  it should "be able to insert into the table" in {
    val result = action.insert(userInfo)
    result.map { res =>
      res shouldEqual Some(userInfo.copy(id = 1))
    }
  }

  it should "be able to insert and increment the id" in {
    for {
      result <- action.insert(userInfo)
      result2 <- action.insert(userInfo)
    } yield {
      result shouldEqual Some(userInfo.copy(id = 2))
      result2 shouldEqual Some(userInfo.copy(id = 3))
    }
  }

  it should "be able to get user by username" in {
    for {
      _ <- action.insert(userInfo)
      _ <- action.insert(userInfo)
      result3 <- action.getuser(userInfo.userName)
    } yield {
      assert(result3.isInstanceOf[Some[UserInfo]])
    }

  }

  it should "be able to fetch user from cache if not deleted from cache" in {
    for {
      _ <- action.insert(userInfo)
      _ <- action.insert(userInfo)
      _ <- action.deleteUser(userInfo.userName)
      result3 <- action.getuser(userInfo.userName)
    } yield {
      assert(result3 !== None)
    }
  }

  it should "be able to fetch user from general cache" in {
    for {
      _ <- action.insertInSeq(userInfo)
      _ <- action.insertInSeq(userInfo)
      result3 <- action.getUserFromSeq(userInfo.userName)
    } yield {
      assert(result3.size >= 1)
    }
  }

  it should "not be able to fetch user from cache if deleted" in {
    for {
      _ <- action.insertInSeq(userInfo)
      _ <- action.insertInSeq(userInfo)
      _ <- action.cleanAllCache
      _ <- action.deleteUser(userInfo.userName)
      result3 <- action.getuser(userInfo.userName)
    } yield {
      assert(result3 == None)
    }
  }

}
