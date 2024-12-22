package de.unruh.homeautomation
package webapp

import org.eclipse.jetty.util.resource.Resource

import java.net.URI
import java.nio.file.Path

object DummyResource extends Resource {
  override def getPath: Path = null
  override def isDirectory: Boolean = false
  override def isReadable: Boolean = true
  override def getURI: URI = null
  override def getName: String = "dummy resource"
  override def getFileName: String = null
  override def resolve(subUriPath: String): Resource = null
}
