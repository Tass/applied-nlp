val hw1dir = new java.io.File("../hw1")

println("*** " + hw1dir + " ***")
val command = List("scala",hw1dir+"/"+args(0)) ::: args.drop(1).toList
scala.sys.process.stringSeqToProcess(command).lines.foreach(println)
println()
