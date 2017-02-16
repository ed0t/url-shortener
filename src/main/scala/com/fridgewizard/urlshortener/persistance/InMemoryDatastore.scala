package com.fridgewizard.urlshortener.persistance

import scala.concurrent.{ExecutionContext, Future}

class InMemoryDatastore(implicit val executionContext: ExecutionContext) extends Datastore {

  val map = collection.mutable.Map.empty[String, String]

  override def put(key: String, value: String): Unit = map.put(key, value)

  override def get(key: String): Future[Option[String]] = Future {
    map.get(key)
  }
}
