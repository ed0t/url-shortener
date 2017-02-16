package com.fridgewizard.urlshortener.service

import java.net.MalformedURLException

import com.fridgewizard.urlshortener.persistance.Datastore
import com.fridgewizard.urlshortener.service.UrlShortener.validate
import com.fridgewizard.urlshortener.persistance.Datastore

import scala.concurrent.{ExecutionContext, Future}

object UrlShortener {
  val validUrl = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]".r

  def validate(url: String): String = {
    url match {
      case validUrl(_*) => url
      case _ => throw new MalformedURLException("The url is not valid")
    }
  }
}

class UrlShortener(datastore: Datastore, baseUrl: String)(implicit val executionContext: ExecutionContext) {

  def addUrl(url: String): Future[String] = Future {
    val validUrl = validate(url)
    val hash: String = Hashing.hash(validUrl)
    datastore.put(hash, validUrl)
    baseUrl + "/" + hash
  }

  def retrieveUrl(shortenId: String): Future[Option[String]] = datastore.get(shortenId)


}
