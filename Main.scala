package grebForWindows

/**
  * find keyword in files
  *
  */
// TODO : read wordprogram files
// TODO : 메모리 관리 => lazy 를 이용해보자
object Main extends App {

  if (args.length == 0) {
    println("Directory path is required!!")
    sys.exit(1)
  }

  if (args.length < 2) {
    println("keyword is missing")
    sys.exit(1)
  }

  val dir = args(0)
  val keyword = args(1)

  // memory info
  //val mb = 1024*1024
  //val runtime = Runtime.getRuntime

  val dirFile = new DirectoryAndFiles(dir, keyword)

  lazy val result = dirFile.grebAll
  println("Search '" + keyword + "' in '" + dir + " (total " + result.length + " hits)")
  result.foreach(println)

  println("Search '" + keyword + "' in '" + dir + " (total " + result.length + " hits)")
}
