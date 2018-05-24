# Monitoring and Supervision for AKKA
Supervision is what the actorsystem does to actors under its relationships; so that it can delegate tasks, and respond to failures by the children.
##### The Supervisor can:
- Restart the actor(internal state lost)
- Resume the actor(internal state kept)
- Stop the actor(loss)
- Escalate the failure, fail itself

ActorSystems create at least 3 actors:

###### /user:The Guardian Actor
Creates children using:
````
system.actorOf()
````

###### /system:The System Guardian
A special guardian introduced to ensure a graceful shutdown, with logging as all normal actors terminate

###### /:The Root Guardian
The grand-parent of all actors termed as "top-level" + supervises special actors as well.

##Importance:
Using Supervision and Monitoring of AKKA Actors, we can create fault tolerant systems/applications that run continuosly for years!

####### A Fault Tolerant system should always be responsive and cannot fail completely in case of a inabilty to perform a function/operation. Even if a component fails, the system continues and never shuts down, but may have degraded performance.

To Achieve Fault Tolerance, we must bake in:
- Duplication: Multiple identical system instances
- Replication: Multiple identical H/W S/W instances that are all called and one result used
- Isolation: Loosely couple components/separate entity
- Delegation: Pass responsibilities around

AKKA:
- Achieves fault isolation through the supervisor resume, stop, restart, terminate messages
- Can create duplicate actors in case of failure
- Has asynchronous message passing, separating components 

#### What we do in this Repo:

###### Create a child Actor of a Parent Actor
- Create the ActorSystem in <b>com.github.janikibichi.learnakka.monitoring</b>
- Branch out to create the first child in <b>com.github.janikibichi.learnakka.monitoring.CreateChild.scala</b>
- Run the App to [create parent and actor.](https://asciinema.org/a/rJQpblYYu1JNGGHFafL19iBUi)
<br><br>
- Branch out to explore overriding the life cycle hooks of an actor
````
git checkout -b override_life_cycle_hooks create_child_actors
````
- Create the file to handle the actor system: <b>com.github.janikibichi.learnakka.monitoring.OverrideHooks.scala</b>
- Run the App to [see the different overrides.](https://asciinema.org/a/Teg6OOQ24cPFFq6GwqXNcsYAg)
<br><br>
- Branch out to sending messages to child actors and collect responses
````
git checkout -b message_child_actors_get_responses override_life_cycle_hooks
````
- Create the file to handle the ActorSystem: <b>com.github.janikibichi.learnakka.monitoring.MessageAndResponse.scala</b>
- Run the App to [send messages to children and get responses.](https://asciinema.org/a/s3J43MTgDgsNbZrZrmJ4MiAhw)
<br><br>
- Branch out to explore OneForOne Strategy for Actors
````
git checkout -b one_for_one_strategy message_child_actors_get_responses 
````
- Create the file to handle the ActorSystem: <b>com.github.janikibichi.learnakka.monitoring.TheOneForOneStrategy.scala</b>
- Run the App to [test the OneForOne Strategy.](https://asciinema.org/a/ZGss1fbSArbwOTYjn3IMEDYYz)
<br><br>
- Branch out to explore AllForOne Strategy for Actors
````
git checkout -b all_for_one_strategy one_for_one_strategy 
````
- Create the file to handle the ActorSystem: <b>com.github.janikibichi.learnakka.monitoring.TheAllForOneStrategy.scala</b>
- Run the App to [test the AllForOne Strategy.](https://asciinema.org/a/ZGss1fbSArbwOTYjn3IMEDYYz)