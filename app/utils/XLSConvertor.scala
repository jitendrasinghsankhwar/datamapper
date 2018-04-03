package utils

import java.io.{FileNotFoundException, FileOutputStream, IOException}
import java.util.Date

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json.{JSONArray, JSONObject}


import scala.collection.mutable

object XLSConvertor {
  val companyName: String = "Amazon"
  val FILE_NAME = "conf/amazon.xlsx"
  var WORKBOOK = new XSSFWorkbook()
  var EXCEL_SHEET = WORKBOOK.createSheet(companyName)

  import org.apache.poi.ss.usermodel.HorizontalAlignment
  import org.apache.poi.ss.usermodel.IndexedColors
  import org.apache.poi.xssf.usermodel.XSSFFont

  val font: XSSFFont = WORKBOOK.createFont
  font.setFontHeightInPoints(10.toShort)
  font.setFontName("Arial")
  font.setColor(IndexedColors.BLUE_GREY.getIndex)
  font.setBold(true)
  font.setItalic(false)
  var STYLE = EXCEL_SHEET.getWorkbook().createCellStyle()
  STYLE.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex)
  STYLE.setAlignment(HorizontalAlignment.CENTER)
  STYLE.setLeftBorderColor(IndexedColors.BLACK.getIndex)
  STYLE.setRightBorderColor(IndexedColors.BLACK.getIndex)
  STYLE.setTopBorderColor(IndexedColors.BLACK.getIndex)
  STYLE.setBottomBorderColor(IndexedColors.BLACK.getIndex)


  def createExcel(convertObject: JSONObject): Unit = {
    var num = mutable.MutableList[Int](0, 0, 7)
    val rowObj = mutable.MutableList[Row]()
    val keyRow = EXCEL_SHEET.createRow(num(0))
    rowObj +=  keyRow
    val valueRow = EXCEL_SHEET.createRow(num(2))
    jsonObjectConverter(convertObject, num, rowObj, valueRow)
    try {
      val outputStream = new FileOutputStream(FILE_NAME)
      WORKBOOK.write(outputStream)
      WORKBOOK.close()
    } catch {
      case e: FileNotFoundException =>
        e.printStackTrace()
      case e: IOException =>
        e.printStackTrace()
    }
    System.out.println("Done")
  }

  private def jsonValueType(value: Any, num: mutable.MutableList[Int], key: String, rowObjList: mutable.MutableList[Row], valueRow: Row): mutable.MutableList[Int] = {
    if (value.isInstanceOf[String]) {
      rowObjList.get(num(0)).get.createCell(num(1)).setCellValue(key)
      valueRow.createCell({
        num(1) += 1; num(1) - 1
      }).setCellValue(value.asInstanceOf[String])
    }
    else if (value.isInstanceOf[Boolean]) {
      rowObjList.get(num(0)).get.createCell(num(1)).setCellValue(key)
      valueRow.createCell({
        num(1) += 1; num(1) - 1
      }).setCellValue(value.asInstanceOf[Boolean])
    }
    else if (value.isInstanceOf[Double]) {
      rowObjList.get(num(0)).get.createCell(num(1)).setCellValue(key)
      valueRow.createCell({
        num(1) += 1; num(1) - 1
      }).setCellValue(value.asInstanceOf[Double])
    }
    else if (value.isInstanceOf[Date]) {
      rowObjList.get(num(0)).get.createCell(num(1)).setCellValue(key)
      valueRow.createCell({
        num(1) += 1; num(1) - 1
      }).setCellValue(value.asInstanceOf[Date])
    }
    else if (value.isInstanceOf[Long]) {
      rowObjList.get(num(0)).get.createCell(num(1)).setCellValue(key)
      valueRow.createCell({
        num(1) += 1; num(1) - 1
      }).setCellValue(value.asInstanceOf[Long])
    }
    else if (value.isInstanceOf[JSONObject]) {
      rowObjList.get({
        num(0) += 1;
        num(0) - 1
      }).get.createCell(num(1)).setCellValue(key)
      if (num(0) >= rowObjList.size) {
        val keyRow = EXCEL_SHEET.createRow(num(0))
        rowObjList += keyRow
      }
      jsonObjectConverter(value.asInstanceOf[JSONObject], num, rowObjList, valueRow)
      num(0) -= 1
    }
    else if (value.isInstanceOf[JSONArray]) {
      val arrayValue = value.asInstanceOf[JSONArray]
      var i = 0
      while ( {
        i < arrayValue.length()
      }) {
        jsonValueType(arrayValue.get(i), num, key + "_" + (i + 1), rowObjList, valueRow)

        {
          i += 1; i - 1
        }
      }
    }
    else {
      rowObjList.get(num(0)).get.createCell(num(1)).setCellValue(key)
      valueRow.createCell({
        num(1) += 1; num(1) - 1
      }).setCellValue("")
    }
    num
  }

  private def jsonObjectConverter(convert: JSONObject, num: mutable.MutableList[Int], rowObjList: mutable.MutableList[Row], valueRow: Row): Unit = {
    var keySet = convert.keySet
    import scala.collection.JavaConversions._
    for (key <- keySet) {
      jsonValueType(convert.get(key.toString), num, key.toString, rowObjList, valueRow)
    }
  }

  import java.io.{BufferedReader, FileReader};

  def convert(data: String): Unit = {
    val jsonObj = new JSONObject(data)
   new JSONConvertor("Amazon").createExcel(jsonObj)
  }

  def readFile(filename: String): String = {
    var result = ""
    try {
      val br = new BufferedReader(new FileReader(filename))
      val sb = new StringBuilder
      var line = br.readLine
      while ( {
        line != null
      }) {
        sb.append(line)
        line = br.readLine
      }
      result = sb.toString
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
    result
  }
}
