package services

import model.Template
import utils.JSONUtils

/**
  * @author Jitendra Singh Sankhwar
  */
object VendorJsonMapper {

  var grouped: Map[(String, Int), String] = Map()

  // convert to vendor json
  def convert(source: Map[String, Any], template: Map[String, Template]): String = {
    val data = template.map ( f => processObject(source, f))
    grouped = Map()
    JSONUtils.serialize(denormalize(data))
  }

  def processObject(source: Map[String, Any], f: (String, Template)): (String, Any) =
  // Handling extra key-value pair in template
    if (!source.get(f._1).isDefined) (f._2 newKeyName, f._2.value.get)
    // Handling nested object
    else if (f._2.nested.isDefined && !source(f._1).isInstanceOf[List[Any]]) nestedConvert(f._1, f._2, source(f._1).asInstanceOf[Map[String, Any]])
    else if (source.get(f._1).isDefined && source.get(f._1).get.isInstanceOf[List[Any]] && source.get(f._1).get.asInstanceOf[List[Any]].apply(0).isInstanceOf[Map[String, Any]]) (f._2.newKeyName, source.get(f._1).get.asInstanceOf[List[Map[String, Any]]]
    .map { elem => processArray(f._2.nested.get, elem) })
    // Handling grouping of keys
    else if (f._2.groupedTo.isDefined) {
      val value = applyFun(source.get(f._1), f._2.funcList).asInstanceOf[Some[Any]].get
      val separator = f._2.groupedTo.get.separator
      grouped +=  ((f._2.groupedTo.get.groupKey.toString, f._2.groupedTo.get.order.get) -> value.toString)
      (f._2.groupedTo.get.groupKey, grouped.groupBy(f => f._1._1).map(f => f._2.toSeq.sortBy( f => f._1._2)).flatten.map(f => f._2).mkString(separator))
    }
    else (f._2.newKeyName, applyFun(source.get(f._1).get, f._2.funcList))

  def processArray(template: Map[String, Template], source: Map[String, Any]): Map[String, Any] = template.map(data => processObject(source, data))

  // convert nested json
  def nestedConvert(key: String, obj: Template, source: Map[String, Any]): (String, Any) = (obj.newKeyName, obj.nested.get.map(f => processObject(source, f)))

  def denormalize(data: Map[String, Any]): Map[String, Any] = {
    data.map { f =>
      if(f._2.isInstanceOf[Map[String, Any]]) Map(f._1 -> denormalize(f._2.asInstanceOf[Map[String, Any]]))
      else if (f._2.isInstanceOf[List[Any]]) listToMap(data.asInstanceOf[Map[String, List[Any]]])
      else Map(f._1 -> f._2)
    }.flatten.toMap
  }

  def listToMap(data: Map[String, List[Any]]): Map[String, Any] = {
    val denormalizeListData = data.filter( f => f._2.isInstanceOf[List[Any]]).map { f =>
      var counter = -1
      f._2.asInstanceOf[List[Any]].map { value =>
        counter = counter + 1
        (f._1 + "" + counter, value)
      }
    }.flatMap( f => f).toMap
    data.filter( f => !f._2.isInstanceOf[List[Any]]) ++ denormalizeListData
  }



  // TODO: Needs to implement all the function which can be apply to convert value data of map.
  // TODO: Move this functionality to util object.
  def applyFun(value: Any, funcList: List[String]): Any =
    if (funcList.isEmpty) value
    else value match {
      case data: String => funcList.head.toUpperCase match {
        case "UPPERCASE" => data.toUpperCase
        case "LOWERCASE" => data.toLowerCase
      }
      case data: Double => funcList.head toUpperCase match {
        case "INTEGER" => data.toInt
      }

      case _ => "DEFAULT"
    }

}
