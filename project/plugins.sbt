addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.3")
addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.6")
addSbtPlugin("org.typelevel" % "sbt-fs2-grpc" % "2.5.4")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.13"
