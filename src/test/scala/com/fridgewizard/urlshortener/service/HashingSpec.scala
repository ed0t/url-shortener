package com.fridgewizard.urlshortener.service

import com.fridgewizard.urlshortener.service.Hashing.hash
import org.scalatest.{FlatSpecLike, Matchers}

class HashingSpec extends FlatSpecLike with Matchers {

  "Hashing" should "create an hash for the url" in {
    hash("www.google.com") shouldBe "ulElDs"
  }

  it should "create a different hash for a different url" in {
    hash("www.google.it") shouldBe "P5RJSZ"
  }

  it should "create a different hash once again" in {
    hash("www.google.co.uk") shouldBe "6TGLH5G"
  }

  it should "create a fixed length hash for a very long url " in {
    hash("http://www.google.com/url?q=http%3A%2F%2Fdevelopers.jollypad.com%2Ffb%2Findex.php%3Fdmmy%3D1%26fb_sig_in_iframe%3D1%26fb_sig_iframe_key%3D8e296a067a37563370ded05f5a3bf3ec%26fb_sig_locale%3Dbg_BG%26fb_sig_in_new_facebook%3D1%26fb_sig_time%3D1282749119.128%26fb_sig_added%3D1%26fb_sig_profile_update_time%3D1229862039%26fb_sig_expires%3D1282755600%26fb_sig_user%3D761405628%26fb_sig_session_key%3D2.IuyNqrcLQaqPhjzhFiCARg__.3600.1282755600-761405628%26fb_sig_ss%3DigFqJKrhJZWGSRO__Vpx4A__%26fb_sig_cookie_sig%3Da9f110b4fc6a99db01d7d1eb9961fca6%26fb_sig_ext_perms%3Duser_birthday%2Cuser_religion_politics%2Cuser_relationships%2Cuser_relationship_details%2Cuser_hometown%2Cuser_location%2Cuser_likes%2Cuser_activities%2Cuser_interests%2Cuser_education_history%2Cuser_work_history%2Cuser_online_presence%2Cuser_website%2Cuser_groups%2Cuser_events%2Cuser_photos%2Cuser_videos%2Cuser_photo_video_tags%2Cuser_notes%2Cuser_about_me%2Cuser_status%2Cfriends_birthday%2Cfriends_religion_politics%2Cfriends_relationships%2Cfriends_relationship_details%2Cfriends_hometown%2Cfriends_location%2Cfriends_likes%2Cfriends_activities%2Cfriends_interests%2Cfriends_education_history%2Cfriends_work_history%2Cfriends_online_presence%2Cfriends_website%2Cfriends_groups%2Cfriends_events%2Cfriends_photos%2Cfriends_videos%2Cfriends_photo_video_tags%2Cfriends_notes%2Cfriends_about_me%2Cfriends_status%26fb_sig_country%3Dbg%26fb_sig_api_key%3D9f7ea9498aabcd12728f8e13369a0528%26fb_sig_app_id%3D177509235268%26fb_sig%3D1a5c6100fa19c1c9b983e2d6ccfc05ef&sa=D&sntz=1&usg=AFQjCNFaGgXBs8ayad09eMnc_imFKCwvxw") shouldBe "3OUNM1"
  }

}
