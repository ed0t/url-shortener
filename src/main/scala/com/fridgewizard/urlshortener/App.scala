package com.fridgewizard.urlshortener

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import com.fridgewizard.urlshortener.persistance.RedisDatastore
import com.fridgewizard.urlshortener.rest.{RestService, UrlRoute}
import com.fridgewizard.urlshortener.service.UrlShortener
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import redis.RedisClient


object App {
  val log = LoggerFactory.getLogger(App.getClass)
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  private val config = ConfigFactory.load("application.conf")

  private val datastore = new RedisDatastore(new RedisClient(config.getString("redis.host"), config.getInt("redis.port")))
  private val baseUrl = config.getString("http.host") + ":" + config.getInt("http.port") + "/#"
  private val service = new UrlShortener(datastore, baseUrl)

  val apiRoute = new UrlRoute(service).route
  val restService = new RestService(apiRoute, StaticResources.servedFrom("web"))

  def main(args: Array[String]): Unit = {
    val bindingFuture = Http().bindAndHandle(restService.route, config.getString("http.interface"), config.getInt("http.port"))
    log.info(s"Server is listening on port ${config.getInt("http.port")}")
  }


}

object StaticResources {
  def servedFrom(root: String): Route =
    path(RemainingPath) {
      rest =>
        getFromResource(s"$root/$rest")
    } ~
      path(RemainingPath) {
        rest =>
          getFromResource(s"$root/$rest/index.html")
      } ~
      pathEndOrSingleSlash {
        getFromResource(s"$root/index.html")
      }
}

