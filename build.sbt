import CiCommands.{ ciBuild, devBuild }
import sbt.Compile
import sbtprotoc.ProtocPlugin.autoImport.PB

lazy val protobuf =
  project
    .in(file("protocol-cats"))
    .enablePlugins(Fs2Grpc)

lazy val monixProtobuf =
  project
    .in(file("protocol-monix"))
    .settings(
      evictionErrorLevel := Level.Warn,
      dependencyOverrides += "org.typelevel" %% "cats-effect" % "2.5.4",
      libraryDependencies ++= Seq(
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
        "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
        "io.monix" %% "monix" % "3.4.1",
        "io.grpc" % "grpc-stub" % scalapb.compiler.Version.grpcJavaVersion,
        "io.grpc" % "grpc-protobuf" % scalapb.compiler.Version.grpcJavaVersion,
        "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
        "io.grpc" % "grpc-services" % scalapb.compiler.Version.grpcJavaVersion
      ),
      Compile / PB.protoSources := Seq(
        (Compile / baseDirectory).value / "protobuf"
      ),
      Compile / PB.targets := Seq(
        PB.gens.java -> (Compile / sourceManaged).value,
        scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
      ),
      Compile / PB.targets := Seq(
        scalapb
          .gen(
            flatPackage = true,
            lenses = false,
            javaConversions = false
          ) -> (Compile / sourceManaged).value / "scalapb"
      )
    )

lazy val server =
  project
    .in(file("server"))
    .settings(
      evictionErrorLevel := Level.Warn,
      dependencyOverrides += "org.typelevel" %% "cats-effect" % "2.5.4",
      libraryDependencies ++= Seq(
        "co.fs2" %% "fs2-core" % "2.5.0",
        "co.fs2" %% "fs2-io" % "2.5.0",
        "org.lyranthe.fs2-grpc" %% "java-runtime" % "0.11.2",
        "org.typelevel" %% "cats-effect" % "2.5.4",
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
        "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
        "io.grpc" % "grpc-stub" % scalapb.compiler.Version.grpcJavaVersion,
        "io.grpc" % "grpc-protobuf" % scalapb.compiler.Version.grpcJavaVersion,
        "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
        "io.grpc" % "grpc-services" % scalapb.compiler.Version.grpcJavaVersion,
        "io.monix" %% "monix" % "3.4.1"
      )
    )
    .dependsOn(monixProtobuf, protobuf)

commands ++= Seq(ciBuild, devBuild)

scalafmtOnCompile := true
scalafmtConfig := file(".scalafmt.conf")
