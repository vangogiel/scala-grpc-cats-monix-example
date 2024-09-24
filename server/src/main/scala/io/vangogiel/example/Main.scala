package io.vangogiel.example

import cats.effect.{ Async, ExitCode, IO, IOApp }
import io.grpc.netty.NettyServerBuilder
import io.vangogiel.example.MonixServiceGrpc.MonixService
import io.grpc.protobuf.services.ProtoReflectionService
import io.vangogiel.example.cats_service.CatsServiceGrpc.CatsService
import monix.execution.Scheduler
import monix.execution.schedulers.SchedulerService

import scala.concurrent.ExecutionContext

object Main extends IOApp {
  val ec: ExecutionContext = ExecutionContext.global
  val scheduler: SchedulerService = Scheduler.io()
  implicit val async: Async[IO] = Async.apply
  override def run(args: List[String]): IO[ExitCode] = {
    val monixService = new MonixServiceImpl()(scheduler)
    val catsService = new CatsServiceImpl()
    (for {
      _ <- IO {
        NettyServerBuilder
          .forPort(8080)
          .addService(MonixService.bindService(monixService, ec))
          .addService(CatsService.bindService(catsService, ec))
          .addService(ProtoReflectionService.newInstance())
          .build()
          .start()
      }
    } yield ()).flatMap(_ => IO.never.as(ExitCode.Success))
  }
}
