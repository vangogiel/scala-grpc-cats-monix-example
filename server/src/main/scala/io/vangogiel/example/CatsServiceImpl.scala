package io.vangogiel.example

import cats.effect.IO
import io.vangogiel.example.cats_service.{ CatsRequest, CatsResponse }
import io.vangogiel.example.cats_service.CatsServiceGrpc.CatsService

import scala.concurrent.Future

class CatsServiceImpl extends CatsService {
  override def handleRequest(
      request: CatsRequest
  ): Future[CatsResponse] =
    IO.pure(CatsResponse("served by cats")).unsafeToFuture()
}
