package com.fridgewizard.urlshortener.rest

import java.net.MalformedURLException

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.{ActorMaterializer, Materializer}
import akka.testkit.TestKit
import com.fridgewizard.urlshortener.service.UrlShortener
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import spray.json._
import scala.concurrent.Future.{failed, successful}

class UrlRouteSpec extends FlatSpec with ScalatestRouteTest with Matchers with MockitoSugar with UrlRouteFormat{

  "UrlRoute" should "return Created when a new URL is created" in new Context {
    when(shortenerService.addUrl("http://www.google.com")).thenReturn(successful("http://localhost/123456"))
    private val parameter = UrlParameter("http://www.google.com").toJson.compactPrint
    Post("/urls", HttpEntity(`application/json`, parameter)) ~> route ~> check {
      status shouldBe Created
      responseAs[String] shouldBe "http://localhost/123456"
    }
  }

  it should "return BadRequest if the input URL is not valid" in new Context {
    when(shortenerService.addUrl("htp://www.google.com")).thenReturn(failed(new MalformedURLException("error")))
    private val parameter = UrlParameter("htp://www.google.com").toJson.compactPrint

    Post("/urls", HttpEntity(`application/json`, parameter)) ~> route ~> check {
      status shouldBe BadRequest
    }
  }

  it should "return Redirect when the URL exists" in new Context {
    when(shortenerService.retrieveUrl("123456")).thenReturn(successful(Some("http://www.google.com")))
    Get("/urls/123456") ~> route ~> check {
      status shouldBe OK
      responseAs[String] shouldBe "http://www.google.com"
    }
  }

  it should "return NotFound when the shorted URL does not match any URL" in new Context {
    when(shortenerService.retrieveUrl("123456")).thenReturn(successful(None))
    Get("/urls/123456") ~> route ~> check {
      status shouldBe NotFound
    }
  }

  it should "return InternalServerError in case of any other failure" in new Context {
    when(shortenerService.retrieveUrl("123456")).thenReturn(failed(new Exception()))
    Get("/urls/123456") ~> route ~> check {
      status shouldBe InternalServerError
    }

  }

  abstract class Context extends TestKit(ActorSystem("testSystem")) {
    implicit val materializer: Materializer = ActorMaterializer()
    val shortenerService = mock[UrlShortener]
    val route: Route = new UrlRoute(shortenerService).route
  }

}
