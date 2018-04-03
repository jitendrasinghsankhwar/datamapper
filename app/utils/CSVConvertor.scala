package utils

object CSVConvertor {
//  def toCSV(data: List[String]) = {
//    val flattenData = data.map { x => new JsonFlattener(x).withFlattenMode(FlattenMode.KEEP_ARRAYS).flatten() }
//    val dataMapRDD = flattenData.map { x => JSONUtils.deserialize[Map[String, AnyRef]](x) }
//    val allHeaders = dataMapRDD.map(f => f.keys).flatMap { x => x }.distinct().collect();
//
//    // removing hidden columns
//    val filteredMapRdd = dataMapRDD.map { x =>
//      var newData = x
//      for (f <- allHeaders) yield {
//        val columnMapping = eventConfig.csvConfig.columnMappings.getOrElse(f, CsvColumnMapping(to = f, hidden = false, mapFunc = null))
//        if (columnMapping.hidden) {
//          newData = newData - f
//        }
//      }
//      newData
//    }
//
//    val rows = filteredMapRdd.map { x =>
//      for (f <- visibleHeaders) yield {
//        val value = x.getOrElse(f, "")
//        val columnMapping = eventConfig.csvConfig.columnMappings.getOrElse(f, CsvColumnMapping(to = f, hidden = false, mapFunc = null))
//        if (value.isInstanceOf[List[AnyRef]]) {
//          StringEscapeUtils.escapeCsv(JSONUtils.serialize(value));
//        } else {
//          val mapFuncName = columnMapping.mapFunc
//          if (mapFuncName != null) {
//            val transformed = ColumnValueMapper.mapValue(mapFuncName, value.toString())
//            StringEscapeUtils.escapeCsv(transformed);
//          } else {
//            StringEscapeUtils.escapeCsv(JSONUtils.serialize(value));
//          }
//        }
//      }
//    }.map { x => x.mkString(",") }.collect();
//
//    val renamedHeaders = visibleHeaders.map { header =>
//      val columnMapping = eventConfig.csvConfig.columnMappings.getOrElse(header, CsvColumnMapping(to = header, hidden = false, mapFunc = null))
//      if (columnMapping.to == null) header else columnMapping.to
//    }
//
//    val csv = Array(renamedHeaders.mkString(",")) ++ rows;
//    sc.parallelize(csv, 1);
//  }


}
