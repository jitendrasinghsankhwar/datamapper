package utils

import scala.io.Source

/**
  * @author Jitendra Singh Sankhwar
  */

object FileReader {

  def readFile[T](path: String)(implicit mf: Manifest[T]): Map[String, T] =
    JSONUtils.deserialize[Map[String, AnyRef]](Source.fromFile(path).getLines.mkString).
      map { f => (f _1, JSONUtils.deserialize[T](JSONUtils.serialize(f _2))) }

}