package de.unruh.homeautomation
package webapp

import controller.Controller
import org.eclipse.jetty.ee10.servlet.DefaultServlet
import org.eclipse.jetty.ee10.webapp.WebAppContext
import org.eclipse.jetty.server.Server
import org.scalatra.servlet.ScalatraListener

import java.net.InetSocketAddress

object Server {
  def main(args: Array[String]): Unit = {
    Controller.run()
    val server = new Server(InetSocketAddress("localhost",8082))
    val context = new WebAppContext()
    context.setBaseResource(DummyResource)
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")
    context.setInitParameter(ScalatraListener.LifeCycleKey, classOf[ScalatraBootstrap].getName)
    server.setHandler(context)
    server.start()
    server.join()
  }
}

