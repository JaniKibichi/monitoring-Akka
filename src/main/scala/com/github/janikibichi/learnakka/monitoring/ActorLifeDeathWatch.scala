package com.github.janikibichi.learnakka.monitoring

import akka.actor.{ActorSystem,Actor,Props,Terminated}

case object Service
case object Kill

object ActorLifeDeathWatch extends App{
  val actorSystem = ActorSystem("Supervision")

  //Define and Create Actor in ActorSystem
  val deathWatchActor = actorSystem.actorOf(Props[DeathWatchActor])

  //Send Messages to Actors
  deathWatchActor ! Service
  deathWatchActor ! Service
  Thread.sleep(1000)
  deathWatchActor ! Kill
  deathWatchActor ! Service
}

//Define a service actor to be monitored
class ServiceActor extends Actor{
  def receive ={
    case Service =>
      println("I provide a special service")
  }
}

//Define a parent actor that creates the service actor
class DeathWatchActor extends Actor{
  val child = context.actorOf(Props[ServiceActor],"serviceActor")
  context.watch(child)

  def receive ={
    case Service =>
      child ! Service
    case Kill =>
      context.stop(child)
    case Terminated('child') =>
    println("The service actor has terminated and is no longer available")
  }
}
