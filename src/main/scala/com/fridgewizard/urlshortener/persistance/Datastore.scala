package com.fridgewizard.urlshortener.persistance

import scala.concurrent.Future

trait Datastore {

  def put(key:String, value:String)

  def get(key:String) : Future[Option[String]]

}
