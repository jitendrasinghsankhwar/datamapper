package utils

import java.lang.reflect.{ParameterizedType, Type}

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
  * @author Jitendra Singh Sankhwar
  */

object JSONUtils {

  @transient val mapper = new ObjectMapper()
  mapper registerModule DefaultScalaModule
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
  mapper.configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, false)
//  mapper.configure(SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY, false)
  //mapper setDefaultPropertyInclusion JsonInclude.Value.construct(Include.ALWAYS, Include.NON_NULL)
  mapper.setSerializationInclusion(Include.NON_NULL)



  @throws(classOf[Exception])
  def serialize(obj: Any): String = mapper.writeValueAsString(obj)

  @throws(classOf[Exception])
  def deserialize[T: Manifest](value: String): T = mapper.readValue(value, typeReference[T])

  private[this] def typeReference[T: Manifest] = new TypeReference[T] {
    override def getType = typeFromManifest(manifest[T])
  }

  private[this] def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments isEmpty) m.runtimeClass
    // $COVERAGE-OFF$Disabling coverage as this code is impossible to test
    else new ParameterizedType {
      def getRawType = m.runtimeClass

      def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

      def getOwnerType = null
    }
    // $COVERAGE-ON$
  }

}
