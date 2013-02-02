// Read data and compute counts

val (tokens, tags, mentions) = scala.io.Source.fromFile(args(0)).getLines.map((line) => {
  val splitted = line.split(" ").drop(2)
  (splitted.toList, splitted.filter(_.startsWith("#")).toList, splitted.filter(_.startsWith("@")).map((string) => if(string.endsWith(":")) string.substring(0, string.length-2) else string).toList)
}).toList.unzip3

def top_10[T](list:List[List[T]]): List[T] = list.flatten.foldLeft(Map[T,Int]().withDefaultValue(0))((map, token) => map + (token -> (map(token) + 1))).toList.sortBy((tuple) => -tuple._2).take(10).map((tuple:Tuple2[T, Int]) => tuple._1)

// Print output
println("Tokens:")
top_10(tokens).foreach(println)

println("Hashtags:")
top_10(tags).foreach(println)

println("Mentions:")
top_10(mentions).foreach(println)
