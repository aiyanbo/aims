package aims.marshalling

import aims.model.Marshalling
import akka.actor.{ ActorSystem, Props }

/**
 * Component:
 * Description:
 * Date: 15/2/2
 * @author Andy Ai
 */
class Marshaller(system: ActorSystem) {
  private val actor = system.actorOf(Props[MarshallingActor], MarshallingActor.name)

  def marshal(marshalling: Marshalling) = {
    actor ! marshalling
  }
}
