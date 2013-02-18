// Part (a)

val book = io.Source.fromFile(args(0)).mkString
val JustTextRE = """(?s).*THE COMPLETE PROJECT GUTENBERG[^\n]*\n(.*)\n\s+End of the Project Gutenberg EBook.*""".r
val JustTextRE(text) = book

// Part (b)
val prefixes = Seq("Dr", "Mr.", "Mrs.", "Miss", "Ms.", "Prof.", "Rev.")
val namesRe = """(?s)((?:%s)\s*(?:\s[A-Z]\w+)+)""".format(prefixes.mkString("|")).r
namesRe.findAllMatchIn(text).map(_.group(0).replaceAll("""\s+""", " ")).foreach(println)
