// Part (a)

val book = io.Source.fromFile(args(0)).mkString
val JustTextRE = """(?s).*PROJECT GUTENBERG[^\n]*\n(.*)\n\s+End of the Project Gutenberg EBook.*""".r
val JustTextRE(text) = book
val rawArticles = text.split("FEDERALIST.? No.").drop(1)

// Part (c)

val symbols = {new java.text.DateFormatSymbols(new java.util.Locale("en"))}
val months = symbols.getMonths.toList.take(12).mkString("|")
val days = symbols.getWeekdays.toList.drop(1).take(7).mkString("|")
val d = "(?:[1-2]?[1-9]|[1-3]0|31:)"
val y = "(?:17[0-9]{2})"

val dates = """(?:((?:(?:%s),? )?(?:%s) %s, %s)\.?)?""".format(days, months, d, y)
val ArticleRE = """(\d+)\s+([\s\S]*?)\n+((?:For|From).*?)?\s*%s\s+([A-Z ]+)\n+(To .*)\n+([\s\S]*)""".format(dates).r
case class Article(id: String, title: String, author: String, venue: String, date: String, addressee: String, text: String)

// Part (d)

val articles = rawArticles.map(ArticleRE.findFirstIn(_) match {
  case Some(ArticleRE(id, title, venue, date, author, addressee, text)) => new Article(id, title.replaceAll("\n", " "), Option(author).getOrElse(""), Option(venue).getOrElse(""), Option(date).getOrElse(""), Option(addressee).getOrElse(""), text)
  case None => throw new Exception("An article didn't get parsed. Fix it.")
})

articles.foreach((article) =>
  println("""id: %s
title: %s
author: %s
venue: %s
date: %s
addressee: %s
text: %s
""".format(article.productIterator.toSeq : _*)))
