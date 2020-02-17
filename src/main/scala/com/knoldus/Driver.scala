package com.knoldus

import com.knoldus.Utilities.{userWithPostsList,postsWithCommentsList}

object Driver extends App {
  val jsonUserData = JsonDataParsing.readJsonData("https://jsonplaceholder.typicode.com/users")
  val jsonCommentsData = JsonDataParsing.readJsonData("https://jsonplaceholder.typicode.com/comments")
  val jsonPostsData = JsonDataParsing.readJsonData("https://jsonplaceholder.typicode.com/posts")

  val userWithMaxpost: (User, Int) = Utilities.maxPostByUser(userWithPostsList)
  println(userWithMaxpost._1.name + " " + userWithMaxpost._2)
 val maxCommentOnPost: (Post, Int) = Utilities.maxCommentOnPost( postsWithCommentsList)
  println(maxCommentOnPost._1.title + " " +  maxCommentOnPost._2)
}
