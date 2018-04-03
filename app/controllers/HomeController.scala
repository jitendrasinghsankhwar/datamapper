package controllers

import javax.inject._

import model.Template
import play.api.mvc._
import services.VendorJsonMapper
import utils.{FileReader, JSONUtils, XLSConvertor}

import scala.io.Source

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    val template = FileReader.readFile[Template]("conf/array.template.json")
    val sourceData = JSONUtils.deserialize[Map[String, AnyRef]](Source.fromFile("conf/array.data.json").getLines.mkString)
    XLSConvertor.convert(VendorJsonMapper.convert(sourceData, template))
    Ok(VendorJsonMapper.convert(sourceData, template))
  }

}
