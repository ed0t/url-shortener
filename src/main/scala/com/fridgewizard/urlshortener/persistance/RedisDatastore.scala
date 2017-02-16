package com.fridgewizard.urlshortener.persistance

import redis.RedisClient

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class RedisDatastore(client:RedisClient)(implicit val executionContext: ExecutionContext) extends Datastore {

  override def put(key: String, value: String): Unit = {
    client.set[String](key, value).onComplete {
      case Success(result) => if(!result) throw new RuntimeException("error during insertion in datastore")
      case Failure(e) => throw e
    }
  }

  override def get(key: String): Future[Option[String]] = client.get[String](key)
}
