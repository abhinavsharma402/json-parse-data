package com.knoldus
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST.JNothing
import net.liftweb.json.JsonAST.JValue
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.implicitConversions



case class User(id: String, name: String, username: String, email: String, address: Address, phone: String, website: String, company: Company)

case class Geo(lat: String, lng: String)

case class Address(street: String, suite: String, city: String, zipcode: String, geo: Geo)

case class Company(name: String, catchPhrase: String, bs: String)

case class Comment(postId: String, id: String, name: String, email: String, body: String)

case class Post(userId: String, id: String, title: String, body: String)


object JsonDataParsing {


  def readJsonData(url: String): String = {
    val request = new HttpGet(url)
    val client = HttpClientBuilder.create().build()
    val response = client.execute(request)
    val res = IOUtils.toString(response.getEntity.getContent)
    res
  }

  def parseData(jsonData: String): List[User] = {

    implicit val formats: DefaultFormats.type = DefaultFormats

    val parsingData = net.liftweb.json.parse(jsonData)


    parsingData.children map { user =>

      val id = (user \ "id").extract[String]

      val name = (user \ "name").extract[String]

      val username = (user \ "username").extract[String]
      val email = (user \ "email").extract[String]
      val street = (user \ "address" \ "street").extract[String]
      val suite = (user \ "address" \ "suite").extract[String]
      val city = (user \ "address" \ "city").extract[String]
      val zipcode = (user \ "address" \ "zipcode").extract[String]
      val lat = (user \ "address" \ "geo" \ "lat").extract[String]
      val lng = (user \ "address" \ "geo" \ "lng").extract[String]

      val phone = (user \ "phone").extract[String]
      val website = (user \ "website").extract[String]
      val companyName = (user \ "company" \ "name").extract[String]
      val catchPhrase = (user \ "company" \ "catchPhrase").extract[String]
      val bs = (user \ "company" \ "bs").extract[String]
      User(id, name, username, email, Address(street, suite, city, zipcode, Geo(lat, lng)), phone, website, Company(companyName, catchPhrase, bs))

    }
  }

  def parseComment(jsonData: String): List[Comment] = {
    val parsedJsonData = net.liftweb.json.parse(jsonData)
    parsedJsonData.children map { comment =>

      val postId = (comment \ "postId").extract[String]
      val id = (comment \ "id").extract[String]
      val name = (comment \ "name").extract[String]
      val email = (comment \ "email").extract[String]
      val body = (comment \ "body").extract[String]

      Comment(postId, id, name, email, body)
    }
  }

  def parsePosts(jsonData: String): List[Post] = {
    val parsedJsonData = net.liftweb.json.parse(jsonData)
    //parsedJsonData.e
    parsedJsonData.children map { comment =>

      val userId = (comment \ "userId").extract[String]
      val id = (comment \ "id").extract[String]
      val title = (comment \ "title").extract[String]
      val body = (comment \ "body").extract[String]

      Post(userId, id, title, body)
    }
  }

  implicit val formats: DefaultFormats.type = DefaultFormats

  implicit def extract(json: JValue): String = json match {
    case JNothing => ""
    case data => data.extract[String]
  }

  val jsonUserData: Future[String] = Future {
    JsonDataParsing.readJsonData("https://jsonplaceholder.typicode.com/users")
  }
  val jsonCommentsData: Future[String] = Future {
    JsonDataParsing.readJsonData("https://jsonplaceholder.typicode.com/comments")
  }

  val jsonPostsData: Future[String] = Future {
    JsonDataParsing.readJsonData("https://jsonplaceholder.typicode.com/posts")
  }

  def userData(futureOfString: Future[String]): List[User] = {
    val parsedJsonUserData: Future[List[User]] = for {
      userData <- jsonUserData
      parsedJsonData <- Future(JsonDataParsing.parseData(userData))
    } yield parsedJsonData
    Await.result(parsedJsonUserData, 10.seconds)
  }

  def commentsData(futureOfString: Future[String]): List[Comment] = {
    val parsedJsonComments: Future[List[Comment]] = for {
      commentsData <- jsonCommentsData
      parsedJsonData1 <- Future(JsonDataParsing.parseComment(commentsData))
    } yield parsedJsonData1
    Await.result(parsedJsonComments, 10.seconds)
  }

  def postsData(futureOfString: Future[String]): List[Post] = {
    val parsedJsonPosts = for {
      postsData <- jsonPostsData
      parsedJsonData <- Future(JsonDataParsing.parsePosts(postsData))
    } yield parsedJsonData
    Await.result(parsedJsonPosts, 10.seconds)
  }

}


