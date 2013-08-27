## RSC sample APP based on _spray_ Template Project

This projects provides a starting point for your own _spray-routing_ endeavors.
There are four branches, providing templates for _spray-routing_ on

* _spray-can_, Scala 2.9 + Akka 2.0 + spray 1.0 (the `on_spray-can_1.0` branch)
* _spray-can_, Scala 2.10 + Akka 2.1 + spray 1.1 (the `on_spray-can_1.1` branch)
* _spray-can_, Scala 2.10 + Akka 2.2 + spray 1.2 (the `on_spray-can_1.2` branch)
* _Jetty_, Scala 2.9 + Akka 2.0 + spray 1.0 (the `on_jetty_1.0` branch)
* _Jetty_, Scala 2.10 + Akka 2.1 + spray 1.1 (the `on_jetty_1.1` branch)
* _Jetty_, Scala 2.10 + Akka 2.2 + spray 1.2 (the `on_jetty_1.2` branch)

You are currently on the `on_spray-can_1.1` branch.

Follow these steps to get started:

1. Git-clone this repository.

        $ git clone git://github.com/spray/spray-template.git my-project

2. Change directory into your clone:

        $ cd my-project

3. Launch SBT:

        $ sbt

4. Compile everything and run all tests:

        > test

5. Start the application:

        > re-start

6. Browse to http://localhost:8080/

7. Start the application:

        > re-stop

8. Learn more at http://www.spray.io/

9. Start hacking on `src/main/scala/com/example/MyService.scala`


## API

* get the state of the chassis _chassis.somewhere.com_

To get the state of the power of chassis identified by name _chassis.somewhere.com_ make a _GET_ request to

    /power/chassis.somewhere.com/

Response will be like this

    {result: OK, power: ON}

* set the state of the power for _chassis.somewhere.com_

Make the _PUT_ request to

    /power/chassis.somewhere.com/{on|off}

Response

    {result: OK}
