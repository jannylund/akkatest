package actors;

import actors.HelloActorProtocol.Joke;
import actors.HelloActorProtocol.SayHello;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.libs.akka.InjectedActorSupport;
import play.libs.ws.WS;
import play.libs.ws.WSClient;

import javax.inject.Inject;

public class HelloActor extends UntypedActor implements InjectedActorSupport {

    public static Props props = Props.create(HelloActor.class);
    private static WSClient ws = WS.client();
    
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof SayHello) {
            sender().tell("Hello, " + ((SayHello) msg).name, self());
        }

        if (msg instanceof Joke) {
            F.Promise<JsonNode> jsonPromise = ws.url("http://api.icndb.com/jokes/random").get().map(response -> response.asJson());
            sender().tell(jsonPromise.get(1000), self());
        }
    }
}