
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scala.concurrent.duration._
import scala.util.Random

class LoadSimulation extends Simulation {

  val httpProtocol: Any = http.baseUrl("http://localhost:8080").userAgentHeader("Load Tester")

  def randomUsername(): String = Random.alphanumeric.take(10).mkString

  def randomPassword(): String = Random.alphanumeric.take(10).mkString

  val customers: Any = scenario("Customers")
    .exec { s =>
      val username = randomUsername()
      val password = randomPassword()
      val payload = s"""{"customer":{"username":"$username","password":"$password"}}"""
      val session = s.setAll(Map("payload" -> payload))
      session
    }
    .exec(
      http("customers")
        .post("/api/customers")
        .header("Content-Type", "application/json")
        .body(StringBody(s => s("payload").as[String]))
        .check(status.in(201))
    )

  setUp(
    customers.inject(
      constantUsersPerSec(10).during(10.seconds),
      rampUsersPerSec(10).to(100).during(3.minutes),
    )
  ).protocols(httpProtocol)
}
