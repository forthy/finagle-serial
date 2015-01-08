package io.github.finagle.serial.scodec

import java.net.InetSocketAddress

import com.twitter.finagle.Service
import com.twitter.util.{Await, Future, Return}
import io.github.finagle.Serial
import _root_.scodec._
import _root_.scodec.codecs._
import org.scalatest.{Matchers, FlatSpec}

class ScodecCodecSpec extends FlatSpec with Matchers {

  "An Scodec Codec" should "work being used standalone" in {
    import shapeless._
    case class Point(x: Double, y: Double)
    case class Point2(x: Int, y: Int)

    val pointCodec = (double :: double).as[Point]
    val point2Codec = (int8 :: int8).as[Point2]

    val codec = toSerialCodec(pointCodec)
    val d = toSerialCodec[Point2](point2Codec)

    val p = Point(3.0, 42.0)
    codec.roundTrip(p) shouldBe Return(p)
  }

  it should "work being used with Serial" in {
    case class Foo(x: Int)
    implicit val fooCodec = int8.as[Foo]

    val server = Serial[Foo, Foo].serve(
      new InetSocketAddress(8123),
      new Service[Foo, Foo] {
        override def apply(x: Foo) = Future.value(Foo(x.x * x.x))
      }
    )
    val service = Serial[Foo, Foo].newService("localhost:8123")

    Await.result(service(Foo(10))) shouldBe Foo(100)
    Await.ready(server.close())
  }
}