package io.vangogiel.example

import io.vangogiel.example.MonixServiceGrpc.MonixService
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.Future

class MonixServiceImpl(implicit scheduler: Scheduler) extends MonixService {
  override def handleRequest(request: MonixRequest): Future[MonixResponse] = {
    Task.now(MonixResponse("served by monix")).runToFuture
  }
}
