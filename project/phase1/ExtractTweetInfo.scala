// Read in JSON tweets and output username, follower count and text
// for each.

scala.io.Source.fromFile(args(0)).getLines.foreach((line) => {
  val name = """"name":"([^"]*)"""".r.findFirstMatchIn(line).map(_.group(1))
  val followers = """"followers_count":(\d+)""".r.findFirstMatchIn(line).map(_.group(1))
  val text = """"text":"([^"]*)"""".r.findFirstMatchIn(line).map(_.group(1))
  for {
    n <- name
    f <- followers
    t <- text
  } println("%s %s %s".format(n, f, t))
}
)

