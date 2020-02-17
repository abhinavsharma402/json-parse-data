name := "json-parse-data"

version := "0.1"

scalaVersion := "2.12.7"
libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.11"

// https://mvnrepository.com/artifact/net.liftweb/lift-json
libraryDependencies += "net.liftweb" %% "lift-json" % "3.4.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0"% "test"