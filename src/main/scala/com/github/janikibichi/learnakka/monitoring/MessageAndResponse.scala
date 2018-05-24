package com.github.janikibichi.learnakka.monitoring

import akka.actor.{Props,ActorSystem,ActorRef, Actor}

//Messages To be Sent to the Actors
case class DoubleValue(x: Int)
case object CreateChildren
case object Send
case class Response(x: Int)

object MessageAndResponse extends App{
  val actorSystem = ActorSystem("MessageAndResponse")
  //Create and define an Actor in an ActorSystem
  val parent = actorSystem.actorOf(Props[ParentOfActor],"Parent")

  //Send the Message
  parent ! CreateChildren
  parent ! CreateChildren
  parent ! CreateChildren
  parent ! Send
}


//Define a child actor that doubles values sent to it
class DoubleActor extends Actor{
  def receive ={
    case DoubleValue(number) =>
      println(s"${self.path.name} Got the number $number")
      sender ! Response(number*2)
  }
}

class ParentOfActor extends Actor{
  val random = new scala.util.Random
  var children = scala.collection.mutable.ListBuffer[ActorRef]()

  def receive = {
    case CreateChildren =>
      children ++= List(context.actorOf(Props[DoubleActor]))

    case Send =>
      println(s"Sending messages to child")
      children.zipWithIndex map { case(child, value)=>
          child ! DoubleValue(random.nextInt(10))
      }

    case Response(x) =>
      println(s"Parent: Response from child ${sender.path.name} is $x")
  }
}