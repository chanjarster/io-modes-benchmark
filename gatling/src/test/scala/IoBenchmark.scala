import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

class IoBenchmark extends Simulation {
  val idFeeder = Iterator.continually(
    Map("randomId" -> Random.nextInt(2000))
  )
  val httpConf = http.baseUrl("http://localhost:8080")


  val hasher = during(130 seconds) {
    exec(
      http("Hasher")
        .get("/hasher?id=${randomId}")
        .check(status.is(200))
    )
  }

  val scn = scenario("Hashing LoadTest")
    .feed(idFeeder)
    .exec(hasher)

  setUp(
    scn.inject(
      rampUsersPerSec(50) to 250 during (30 seconds),
    ),
  ).protocols(httpConf)
}
