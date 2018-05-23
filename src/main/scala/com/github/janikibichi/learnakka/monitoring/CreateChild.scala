package com.github.janikibichi.learnakka.monitoring

import akka.actor.{ActorSystem, Props, Actor}

//Create Messages for Sending to Actors
case object CreateAChild
case class Greet(msg: String)


object CreateChild extends App{

}

//Define the Child Actor
class ChildActor extends Actor{
  def receive ={
    case Greet(msg) =>
      println(s"My parent[${self.path.parent}] greeted to me [${self.path}] $msg")
  }
}

//Parents create a child in their context
class ParentActor extends Actor{
  def receive = {
    case CreateAChild =>
      val child = context.actorOf(Props[ChildActor], "child")
      child ! Greet("Hello Child")
  }
}

