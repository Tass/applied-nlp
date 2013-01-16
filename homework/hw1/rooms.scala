// Default values of room number assignments you will build on. Don't
// change the next line.
val defaultRoomNumbers = Map("Sam" -> "312", "Ted" -> "325", "Jane" -> "312")


// Check that there are an even number of command line
// arguments. Print a warning and exit if there aren't.
if (args.length % 2 != 0) {
  println("Please supply an even number of arguments.")
  System.exit(1)
}


// Add the command line information to defaultRoomNumbers to create
// the roomNumbers map.

// Maybe with partition?
def filterOnIndex[A](list: List[A], func: (Int) => Boolean) = list.zipWithIndex.filter({case (ele, index) => func(index)}).unzip._1

val roomNumbers = defaultRoomNumbers ++ filterOnIndex(args.toList, (_ % 2 == 0)).zip(filterOnIndex(args.toList, _ % 2 == 1)).toMap

// Print out the people and the room they are in, sorted
// alphabetically by name.
println("\nPart (a)")

roomNumbers.toList.sorted.foreach({case (name, number) => println("%s: Room %s".format(name, number))})

// Create a new Map roomsToPeople that maps room numbers to lists of
// the people who are in them.

val roomsToPeople = roomNumbers.toList.map({case (k, v) => ((v, k))})
  .foldLeft(Map[String, List[String]]())({(b,a) => b + ((a._1, a._2 :: b.applyOrElse(a._1, (str:String) => List[String]())))})

// For each room, print the list of who is in it.
println("\nPart (b)")

roomsToPeople.toList.foreach({case (number, names) => println("Room %s: %s".format(number, names.reverse.mkString(",")))})
