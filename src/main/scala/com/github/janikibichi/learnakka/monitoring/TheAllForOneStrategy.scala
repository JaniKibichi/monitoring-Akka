package com.github.janikibichi.learnakka.monitoring

import akka.actor.{Actor, ActorRef, ActorSystem, AllForOneStrategy, Props}
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}

import scala.concurrent.duration._

//Create Messages
case class Add(a: Int, b: Int)
case class Sub(a: Int, b: Int)
case class Div(a: Int, b: Int)

object TheAllForOneStrategy extends App{
  val system = ActorSystem("SupervisorStrategy")

  val supervisor = system.actorOf(Props[AllForOneStrategySupervisor],"supervisor")

  supervisor ! "Start"
}

//Create a calculator Actor
class Calculator(printer: ActorRef) extends Actor {
  override def preRestart(reason: Throwable, message: Option[Any]): Unit ={
    println("Calculator: is restarting because of an ArithmeticException")
  }

  def receive = {
    case Add(a,b) =>
      printer ! s"sum is ${a+b}"

    case Sub(a,b) =>
      printer ! s"diff is ${a-b}"

    case Div(a,b) =>
      printer ! s"div is ${a/b}"
  }
}

//Create a ResultPrinter actor
class ResultPrinter extends Actor {
  override def preRestart(reason: Throwable, message: Option[Any]): Unit ={
    println("Printer : is restarting as well")
  }

  def receive = {
    case msg => println(msg)
  }
}

//Create the Supervisor actor
class AllForOneStrategySupervisor extends Actor {
  override val supervisorStrategy = AllForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 seconds){
    case _: ArithmeticException => Restart
    case _: NullPointerException => Resume
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate
  }

  //Create children actors
  val printer = context.actorOf(Props[ResultPrinter])

  val calculator = context.actorOf(Props(classOf[Calculator], printer))

  def receive = {
    case "Start" =>
      calculator ! Add(10,12)
      calculator ! Sub(12,10)
      calculator ! Div(5,2)
      calculator ! Div(5,0)
  }
}


