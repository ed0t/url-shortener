package com.fridgewizard.urlshortener.rest

import akka.http.scaladsl.server._
import akka.stream.Materializer

class RestService(apiRoutes: Route, uiRoute: Route)
  extends CorsSupport
  with Directives {

  override val corsAllowOrigins: List[String] = List("*")

  def route(implicit materializer: Materializer): Route = cors {

      pathPrefix("api") {
        apiRoutes
      } ~ uiRoute
  }

}
