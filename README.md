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