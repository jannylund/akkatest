package modules;

import actors.HelloActor;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class GuiceAkkaBinderModule extends AbstractModule implements AkkaGuiceSupport {

    @Override
    protected void configure() {
        bindActor(HelloActor.class, "joker-actor");
    }
}
