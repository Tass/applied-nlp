//to test: scala dates.scala "09/07/2011" "09-07-2011" "the 7th of September, 2011"

val monthsList ={new java.text.DateFormatSymbols(new java.util.Locale("en"))}.getMonths.toList.take(12)
val months = monthsList.mkString("|")

val m = "([0-9]|1[0-2])"
val d = "([1-2]?[1-9]|[1-3]0|31)"
val y = "((?:19|20)[0-9]{2})"

// Part (a)

val FullFormDate = new scala.util.matching.Regex("(%s) %s, %s".format(months, d, y), "month", "day", "year")

// Part (b)

val ShortDateA = new scala.util.matching.Regex("%s-%s-%s".format(m,d,y), "month", "day", "year")
val ShortDateB = new scala.util.matching.Regex("%s/%s/%s".format(m,d,y), "month", "day", "year")

// Part (c)

val ordinals = "((?:[1-2]?(?:1st|2nd|3rd|[4-9]th))|[12]0th|31st)"

val OrdinalDate = new scala.util.matching.Regex("the %s of (%s), %s".format(ordinals, months, y), "day", "month", "year")

// Part (d)

def normalize(input: Tuple3[String,String,String]): String = {
  val (year, month, day) = input
  val index = monthsList.indexOf(month)
  val m = if (index < 0) { month } else { (index + 1).toString }
  val d = "\\D+".r.replaceAllIn(day, "")
  "%s-%s-%s".format(year, m, d)
}

def normalizeDate(input: String): Option[String] =
  (OrdinalDate.findFirstMatchIn(input).map((m) => ((m.group("year"), m.group("month"), m.group("day")))) :: List(FullFormDate, ShortDateA, ShortDateB).map(_.findFirstMatchIn(input)).map(_.map((m) => ((m.group("year"), m.group("month"), m.group("day")))))).flatten.map(normalize).headOption

args.map(normalizeDate(_)).flatten.foreach(println)
