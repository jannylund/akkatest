package controllers;

import actors.HelloActor;
import actors.HelloActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.F;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

import static akka.pattern.Patterns.ask;

@Singleton
public class Application extends Controller {

    // DI use of WSClient
    @Inject WSClient ws;
    public F.Promise<Result> index() {
        return ws.url("http://api.icndb.com/jokes/random").get().map(response -> ok(response.asJson()));
    }


    // Inject actor, use akka ask.
    final ActorRef helloActor;
    @Inject public Application(ActorSystem system) {
        helloActor = system.actorOf(HelloActor.props);
    }

    public F.Promise<Result> jokeActor() {
        return F.Promise.wrap(ask(helloActor, new HelloActorProtocol.Joke(), 1000))
            .map(response -> ok((ObjectNode) response));
    }
}
