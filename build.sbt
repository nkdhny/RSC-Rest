organization  := "com.example"

version       := "0.1"

scalaVersion  := "2.10.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

unmanagedBase <<= baseDirectory { base => base / "lib" }

libraryDependencies ++= Seq(
  "io.spray"            %   "spray-can"     % "1.1-M8",
  "io.spray"            %   "spray-routing" % "1.1-M8",
  "io.spray"            %   "spray-testkit" % "1.1-M8",
  "com.typesafe.akka"   %%  "akka-actor"    % "2.1.4",
  "com.typesafe.akka"   %%  "akka-testkit"  % "2.1.4",
  "log4j"               %   "log4j"         % "1.2.14",
  "org.specs2"          %%  "specs2"        % "1.14" % "test",
  "org.scalatest"       %   "scalatest_2.10"% "1.9.1"% "test"
)

seq(Revolver.settings: _*)