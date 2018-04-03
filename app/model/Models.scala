package model


/**
  * @author Jitendra Singh Sankhwar
  */

class Models extends Serializable {}

// common template model
case class Template(newKeyName: String, funcList: List[String], value: Option[Any], nested: Option[Map[String, Template]], groupedTo: Option[GroupedTo])
// model to handle grouped keys
case class GroupedTo(groupKey: String, separator: String, order: Option[Int])



