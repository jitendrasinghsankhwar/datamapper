package controllers

import model.Template
import org.scalatest.FlatSpec
import services.VendorJsonMapper
import utils.{FileReader, JSONUtils}

import scala.io.Source

/**
  * @author Jitendra Singh Sankhwar
  */
class VendorJsonMapper extends FlatSpec {

  "JsonMapper" should "convert nested object" in {
    val templateObj = FileReader readFile[Template] "conf/product.template.json"
    val dataObj = JSONUtils deserialize[Map[String, Any]] Source.fromFile("conf/product_info.json").getLines.mkString
    println("\n\n" + VendorJsonMapper.convert(templateObj, dataObj) + "\n\n")
  }
  "it" should "parse array object, aggregate keys and add additional keys" in {
    val templateObj = FileReader readFile[Template] "conf/array.template.json"
    val dataObj = JSONUtils deserialize[Map[String, Any]] Source.fromFile("conf/array.data.json").getLines.mkString
    println("\n\n" + VendorJsonMapper.convert(templateObj, dataObj) + "\n\n" )
  }

}
