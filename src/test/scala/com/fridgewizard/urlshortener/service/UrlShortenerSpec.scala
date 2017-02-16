package com.fridgewizard.urlshortener.service

import java.net.{URL, MalformedURLException}

import com.fridgewizard.urlshortener.persistance.InMemoryDatastore
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpecLike, Matchers}

import scala.concurrent.ExecutionContext

class UrlShortenerSpec extends FlatSpecLike with Matchers with ScalaFutures {

  "UrlShortener" should "shorten a given URL" in new Context {
    whenReady(service.addUrl("http://www.google.com")) {
      result => result shouldBe "http://localhost/#/HX6SM1G"
    }
  }

  it should "always return the same shortened URL for the same input URL" in new Context {
    whenReady(for {
      first <- service.addUrl("http://www.google.com")
      second <- service.addUrl("http://www.google.com")
    } yield (first, second)) {
      case (first, second) => first shouldBe second
    }
  }

  it should "return different shortened URL for different input URL" in new Context {
    whenReady(for {
      first <- service.addUrl("http://www.google.com")
      second <- service.addUrl("http://www.google.co.uk")
    } yield (first, second)) {
      case (first, second) => first shouldNot be(second)
    }
  }

  it should "fail if the URL is not valid" in new Context {
    whenReady(service.addUrl("http:/www.google.com").failed) {
      result => result shouldBe a[MalformedURLException]
    }
  }

  it should "retireve a URL from the shortened URL" in new Context {
    whenReady(
      for {
        shorten <- service.addUrl("http://www.google.com")
        url <- service.retrieveUrl(new URL(shorten).getRef.tail)
      } yield url) {
      result => result shouldBe Some("http://www.google.com")
    }
  }

  it should "return None when there is no URL associated to the shortened URL" in new Context {
    whenReady(service.retrieveUrl("http://localhost/HX6SM1G")) {
      result => result shouldBe None
    }
  }

  private abstract class Context {
    implicit val executionContext = ExecutionContext.Implicits.global
    val service = new UrlShortener(new InMemoryDatastore, "http://localhost/#")

  }

}
