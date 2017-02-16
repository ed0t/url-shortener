package com.fridgewizard.urlshortener.rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.fridgewizard.urlshortener.service.UrlShortener
import spray.json.DefaultJsonProtocol

import scala.util.{Failure, Success}



case class UrlParameter(url:String)

trait UrlRouteFormat extends DefaultJsonProtocol {

  implicit val urlParameterFormat = jsonFormat1(UrlParameter)

}

class UrlRoute(service: UrlShortener)(implicit materializer: Materializer) extends UrlRouteFormat with SprayJsonSupport {


  val route: Route = urlAdd ~ urlRetrieve

  def urlAdd =
    (path("urls") & post & entity(as[UrlParameter])) { requestedUrl =>
      onComplete(service.addUrl(requestedUrl.url)){
        case Success(shortenedUrl) => complete(Created, shortenedUrl)
        case Failure(e) => complete(BadRequest, e.getMessage)
      }
    }

  def urlRetrieve =
    (path("urls" /  Segment) & get) { shortUrl =>
      onComplete(service.retrieveUrl(shortUrl)){
//        case Success(Some(url)) => redirect(Uri(url), MovedPermanently)
        case Success(Some(url)) => complete(url)
        case Success(None) => complete(NotFound)
        case Failure(e) => failWith(e)
      }

    }

}
