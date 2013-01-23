import io.Source
// Create the translation dictionary

if (args.length < 1) {
  System.err.println("Please supply the dictionary and text to translate.")
  System.exit(1)
}

val dictionary = Source.fromFile(args(0)).getLines.map(_.split("\t")).map((line) => (line(0), line(1))).toMap

// Translate the input sentences

val input = args.drop(1)
val translated = input.map(_.split(" ").map(dictionary.applyOrElse(_,(str:String) => str.toUpperCase)).mkString(" ")).toList

println(input.zip(translated).map({case (port, eng) => "Portuguese: %s\nEnglish:    %s\n".format(port, eng)}).mkString("\n"))
