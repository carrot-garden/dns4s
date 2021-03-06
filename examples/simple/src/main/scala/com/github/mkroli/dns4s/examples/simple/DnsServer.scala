/*
 * Copyright 2014 Michael Krolikowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mkroli.dns4s.examples.simple

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

import com.github.mkroli.dns4s.akka.Dns
import com.github.mkroli.dns4s.dsl._

import akka.actor._
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout

class DnsHandlerActor extends Actor {
  override def receive = {
    case Query(q) ~ Questions(QName(host) ~ TypeA() :: Nil) =>
      sender ! Response(q) ~ Answers(RRName(host) ~ ARecord("1.2.3.4"))
  }
}

object DnsServer extends App {
  implicit val system = ActorSystem("DnsServer")
  implicit val timeout = Timeout(5 seconds)
  IO(Dns) ? Dns.Bind(system.actorOf(Props[DnsHandlerActor]), 5354)
}
