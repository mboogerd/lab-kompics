package com.github.mboogerd.labkompics

import se.sics.kompics.{ComponentDefinition, Handler, Start}

class HelloComponent extends ComponentDefinition {
		val startHandler: Handler[Start] = new Handler[Start] {
      override def handle(e: Start): Unit = {
        println("Hello World")
        System.exit(0)
      }
    }

		subscribe(startHandler, control)
}