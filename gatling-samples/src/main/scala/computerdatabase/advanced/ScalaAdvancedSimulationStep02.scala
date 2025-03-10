/*
 * Copyright 2011-2022 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package computerdatabase.advanced

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ScalaAdvancedSimulationStep02 extends Simulation {

  val httpProtocol =
    http
      .baseUrl("http://computer-database.gatling.io")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
      .doNotTrackHeader("1")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .acceptEncodingHeader("gzip, deflate")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val search =
    exec(http("Home").get("/"))
      .pause(1)
      .exec(http("Search").get("/computers?f=macbook"))
      .pause(1)
      .exec(http("Select").get("/computers/6"))
      .pause(1)

  val browse =
    exec(http("Home").get("/"))
      .pause(2)
      .exec(http("Page 1").get("/computers?p=1"))
      .pause(670.millis)
      .exec(http("Page 2").get("/computers?p=2"))
      .pause(629.millis)
      .exec(http("Page 3").get("/computers?p=3"))
      .pause(734.millis)
      .exec(http("Page 4").get("/computers?p=4"))
      .pause(5)

  val edit =
    exec(http("Form").get("/computers/new"))
      .pause(1)
      .exec(
        http("Post")
          .post("/computers")
          .formParam("name", "Beautiful Computer")
          .formParam("introduced", "2012-05-30")
          .formParam("discontinued", "")
          .formParam("company", "37")
      )

  // Let's have multiple populations
  val users = scenario("Users").exec(search, browse) // regular users can't edit

  val admins = scenario("Admins").exec(search, browse, edit)

  // Let's have 10 regular users and 2 admins, and ramp them over 10 seconds so we don't hammer
  // the server
  setUp(users.inject(rampUsers(10).during(10)), admins.inject(rampUsers(2).during(10)))
    .protocols(httpProtocol)
}
