package com.knoldus

case class UserWithPosts(user: User, posts: List[Post])

case class PostsWithComments(post: Post, comments: List[Comment])

object Utilities {
  val usersList: List[User] = JsonDataParsing.userData(JsonDataParsing.jsonUserData)
  val postsList: List[Post] = JsonDataParsing.postsData(JsonDataParsing.jsonPostsData)
  val commentsList: List[Comment] = JsonDataParsing.commentsData(JsonDataParsing.jsonCommentsData)


  def userWithPostsUtility(post: List[Post], user: List[User]): List[UserWithPosts] = {

    user.map(users => UserWithPosts(users, post.filter(users.id == _.userId)))
  }

  def postsWithCommentsUtility(post: List[Post], comment: List[Comment]): List[PostsWithComments] = {
    post.map(posts => PostsWithComments(posts, comment.filter(posts.id == _.postId)))
  }

  val userWithPostsList: List[UserWithPosts] = Utilities.userWithPostsUtility(postsList, usersList)
  val postsWithCommentsList: List[PostsWithComments] = Utilities.postsWithCommentsUtility(postsList, commentsList)

  def maxPostByUser(userWithPosts: List[UserWithPosts]): (User, Int) = {
    val postCount = for {postsByUser <- userWithPosts

                         } yield (postsByUser.user, postsByUser.posts.length)

    postCount.reduceLeft((first, second) => if (first._2 > second._2) first else second)
  }
  def maxCommentOnPost(postWithComment: List[PostsWithComments]): (Post, Int) = {
    val commentCount = for {commentsOnPost <- postWithComment

                         } yield (commentsOnPost.post,commentsOnPost.comments.length)

    commentCount.reduceLeft((first, second) => if (first._2 > second._2) first else second)
  }


}






