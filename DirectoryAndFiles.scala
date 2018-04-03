package grebForWindows

import java.io
import java.io.FileNotFoundException
import java.nio.charset.MalformedInputException

import scala.io.Source

class DirectoryAndFiles(directory: String, keyword: String) {

  require(directory.length > 0)
  require(keyword.length > 0)
  require(new java.io.File(directory).exists())

  private def filesList = new java.io.File(directory).listFiles

  //private def fileToStringList(inFile: java.io.File): List[String] = Source.fromFile(inFile).getLines.toList

  private def fileToStringList(inFile: java.io.File): List[String] = {
    try {
      if(inFile.isFile) {
        Source.fromFile(inFile).getLines.toList
      }
      else List[String]()
    } catch {
      case e: FileNotFoundException => List[String]()
      // TODO : handle this Exception
      case e: MalformedInputException => List[String]()
    }
  }

  def isDirectory(dir: String): Boolean = new java.io.File(directory).isDirectory

  def isFile(dir: String) : Boolean = new java.io.File(directory).isFile

  def matchedLine(linesList: List[String]) : List[String] = linesList.filter(_.contains(keyword))

  def indexedMatchLine(linesList: List[String]): List[String] = for (line <- linesList.zipWithIndex.filter(_._1.contains(keyword))) yield "    Line " + line._2.toString + ": " + line._1

  def searchKeywordInFileWithIndex(inFile: java.io.File): List[String] = {
    val matched = indexedMatchLine(fileToStringList(inFile))
    if (matched.isEmpty) Nil
    else (inFile.getAbsoluteFile + "  (" + matched.length.toString + " hits)") :: matched
  }

  val yieldGreb: Array[List[String]] = for (f <- filesList) yield searchKeywordInFileWithIndex(f)

  def grebAll: List[io.Serializable] = recursiveGrebWithIndex(filesList.toList)

  // using tail recursive
  def recursiveGrebWithIndex(inFilesList: List[java.io.File]): List[io.Serializable] = {
    if (inFilesList.isEmpty) Nil
    else {
      try {
        if (inFilesList.head.isDirectory) recursiveGrebWithIndex(inFilesList.head.listFiles.toList) ++ recursiveGrebWithIndex(inFilesList.tail)
        else searchKeywordInFileWithIndex(inFilesList.head) ++ recursiveGrebWithIndex(inFilesList.tail)
      } catch {
        case e: NullPointerException => Nil
        case e: FileNotFoundException => Nil
      }
    }
  }
}

object DirectoryListing {

  // val file = (new java.io.File("C:/$Recycle.Bin/S-1-5-18"))
}
