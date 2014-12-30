package aims.core

import akka.actor.{ Actor, ActorLogging, ActorRef, Props }
import akka.http.model.{ HttpRequest, HttpResponse, StatusCodes }
import aims.core.Resources.{ Resource, ResourceMirror }
import aims.parser.PatternParser

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
class ServiceEngine(resources: List[Resource]) extends Actor with ActorLogging {
  private val actors = scala.collection.mutable.HashMap[String, ActorRef]()
  private val mirrors: Map[String, ResourceMirror] =
    resources.map(resource ⇒ {
      val res = PatternParser.parse(resource.pattern)
      val matcher = resource.method.name + "::" + res._1
      (matcher, ResourceMirror(resource.pattern, matcher, resource.method, MicroService.props(resource.handler), res._2,
        resource.consumerTypes, resource.producerTypes))
    }).toMap

  override def receive: Receive = {
    case request: HttpRequest ⇒
      val path = request.method.name + "::" + request.uri.path.toString
      val filteredMirrors = mirrors.filterKeys(pattern ⇒ path.matches(pattern))
      if (filteredMirrors.isEmpty) {
        sender() ! HttpResponse(StatusCodes.NotFound)
      } else {
        val mirror = filteredMirrors.values.headOption.get
        val parameters = path.split("/")
        val actorName = mirror.method.name + mirror.pattern.replace("/", "-").replace(":", "").replace("#", "")
        val req: ServiceRequest = ServiceRequest(sender(), request, mirror.pattern, mirror.parameters.mapValues(parameters(_)))
        actors.getOrElseUpdate(mirror.pattern, context.actorOf(mirror.props, actorName)) ! req
      }
  }
}

object ServiceEngine {
  def props(resources: List[Resource]): Props = {
    Props(new ServiceEngine(resources))
  }
}
