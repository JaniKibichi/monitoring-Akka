package com.github.janikibichi.learnakka.monitoring

import akka.actor._
import akka.actor.SupervisorStrategy._
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.ask

//Create the messages to send to actors
case object Error
case class StopActor(actorRef: ActorRef)

object OverrideHooks extends App{
  implicit val timeout = Timeout(2 seconds)

  //Set up Actor System
  val actorSystem = ActorSystem("Supervisor")

  //Create and define Actors in the ActorSystem
  val supervisor = actorSystem.actorOf(Props[Supervisor],"Supervisor")

  val childFuture = supervisor ? (Props(new LifeCycleActor),"LifeCycleActor")

  val child = Await.result(childFuture.mapTo[ActorRef],2 seconds)

  child ! Error
  Thread.sleep(1000)
  supervisor ! StopActor(child)
}

//Create an Actor and Override the life cycle methods
class LifeCycleActor extends Actor{
  var sum = 1

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println(s"sum in preRestart is $sum")
  }

  override def preStart(): Unit = println(s"sum in preStart is $sum")

  def receive ={
    case Error => throw new ArithmeticException()
    case _ => println("default message")
  }

  override def postStop(): Unit = {
    println(s"sum in postStop is ${sum*3}")
  }

  override def postRestart(reason: Throwable): Unit = {
    sum = sum * 2
    println(s"sum in postRestart is $sum")
  }
}

//Create a Supervisor Actor
class Supervisor extends Actor{

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute){
      case _: ArithmeticException => Restart
      case t => super.supervisorStrategy.decider.applyOrElse(t,(_:Any)=>Escalate)
    }

  def receive = {
    case (props: Props, name: String) =>
      sender ! context.actorOf(props,name)

    case  StopActor(actorRef) =>
      context.stop(actorRef)
  }
}

