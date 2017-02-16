package com.fridgewizard.urlshortener.service

import scala.annotation.tailrec
import scala.util.hashing.MurmurHash3

object Hashing {

  val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
  val base = alphabet.length / 2

  private def encode(value: Int): String = {

    @tailrec
    def encode(v: Int, r: String): (Int, String) = {
      val mod = v % base
      val position = if (mod < 0) mod * (-1) + base else mod

      val s = r + alphabet.charAt(position).toString
      val i = v / base
      if (i == 0) (i, s) else encode(i, s)

    }

    encode(value, "")._2

  }

  def hash(url: String): String = {
    encode(MurmurHash3.stringHash(url))
  }

}
