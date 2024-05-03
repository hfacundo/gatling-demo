package simulations;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class DummySimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http.baseUrl("https://jsonplaceholder.typicode.com")
                    .acceptHeader("application/json");

    ChainBuilder testGet =
            exec(http("Test GET")
                    .get("/todos/1")
                    .check(status().is(200))
            );

    ChainBuilder testPost =
            exec(http("Test POST")
                    .post("/posts")
                    .check(status().is(201))
            );

    ChainBuilder testGetAndResult =
            exec(http("Test GET (Validate userId)")
                    .get("/todos/1")
                    .check(status().in(200, 304))
                    .check(jsonPath("$.userId").is("1"))
            );

    ScenarioBuilder scenario = scenario("Users").exec(testGet, testPost, testGetAndResult);

    {
        setUp(
                scenario.injectOpen(
                        rampUsers(2)
                        .during(2)))
                .protocols(httpProtocol);
    }

}
