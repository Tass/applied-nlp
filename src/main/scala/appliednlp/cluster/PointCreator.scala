package appliednlp.cluster

import nak.cluster._
import nak.util.CollectionUtil._
import chalk.util.SimpleTokenizer

import org.apache.log4j.Logger
import org.apache.log4j.Level

case class ClusterPoint(id: String, label: String, point: Point)

/**
 *  Read data and produce data points and their features.
 *
 *  @param filename the name of the file containing the data
 *  @return a triple, the first element of which is a sequence of id's
 *     (unique for each row / data point), the second element is the sequence
 *     of (known) cluster labels, and the third of which is the sequence of
 *     Points to be clustered.
 */
trait PointCreator extends (String => Iterator[ClusterPoint]) {
  def apply(filename: String): Iterator[ClusterPoint] = io.Source.fromFile(filename).getLines.map(line => processLine(
    " +".r.replaceAllIn("([^0-9 ]) ([^0-9 ])".r.replaceFirstIn(line, "$1_$2"), " ").split(" "))
                                                                                                ).flatten
  def processLine(line: Array[String]): Iterator[ClusterPoint] = Iterator()
}

object Point {
  def apply(x: String, y:String) = nak.cluster.Point(Vector(x,y).map(_.toDouble))
  def apply(points:Seq[Double]) = nak.cluster.Point(Vector(points:_*))
}

/**
 * Read data in the standard format for use with k-means.
 */
object DirectCreator extends PointCreator {

  override def processLine(line: Array[String]) = line match {
      case Array(id, label, x, y, _*) => Iterator(ClusterPoint(id, label, Point(x,y)))
      case _ => Iterator()
    }

}


/**
 * A standalone object with a main method for converting the achieve.dat rows
 * into a format suitable for input to RunKmeans.
 */
object SchoolsCreator extends PointCreator {

  override def processLine(line: Array[String]) = line match {
    case Array(name, read_4, math_4, read_6, math_6, _*) =>
      Iterator((read_4, math_4, "4"), (read_6, math_6, "6"))
      .map({case (x, y, label) =>
            ClusterPoint(
              name + "_" + label,
              label,
              Point(x, y)
            )})
    case _ => Iterator()
  }

}

/**
 * A standalone object with a main method for converting the birth.dat rows
 * into a format suitable for input to RunKmeans.
 */
object CountriesCreator extends PointCreator {

  override def processLine(line: Array[String]) = line match {
      case Array(id, x, y, _*) => Iterator(ClusterPoint(id, "1", Point(x,y)))
      case _ => Iterator()
    }

}

/**
 * A class that converts the raw Federalist
 * papers into rows with a format suitable for input to Cluster. As part of
 * this, it must also perform feature extraction, converting the texts into
 * sets of values for each feature (such as a word count or relative
 * frequency).
 */
class FederalistCreator(simple: Boolean = false) extends PointCreator {

  override def apply(filename: String) = {
    val texts = FederalistArticleExtractor(filename)
    texts.map(text =>
      ClusterPoint(text("id"), text("author"),
                   if(simple) {
        extractSimple(text("text"))
      }
                   else {
        extractFull(text("text"))
      }
                 )).toIterator
  }

  /**
   * Given the text of an article, compute the frequency of "the", "people"
   * and "which" and return a Point per article that has the frequency of
   * "the" as the value of the first dimension, the frequency of "people"
   * for the second, and the frequency of "which" for the third.
   *
   */
  def extractSimple(text: String): Point = {
    val tokens = SimpleTokenizer(text)
    Point(Seq(tokens.count(_=="the"), tokens.count(_=="people"), tokens.count(_=="which")).map(_.toDouble))
  }

  lazy val stopwords = io.Source.fromFile("data/cluster/federalist/english.stop").split('\n').map(_.mkString).toList
  /**
   * Given the text of an article, extract features as best you can to try to
   * get good alignment of the produced clusters with the known authors.
   *
   */
  def extractFull(text: String): Point = {
    val tokens = SimpleTokenizer(text)
    Point(
      stopwords.map(stop => tokens.count(stop ==_).toDouble/tokens.size)
    )
  }

}

object FederalistArticleExtractor {
  /**
   * A method that takes the raw Federalist papers input and extracts each
   * article into a structured format.
   *
   * @param filename The filename containing the Federalist papers.
   * @return A sequence of Maps (one per article) from attributes (like
   *         "title", "id", and "text") to their values for each article.
   */
  def apply(filename: String): IndexedSeq[Map[String, String]] = {

    // Regex to identify the text portion of a document.
    val JustTextRE = (
      """(?s)\*\*\* START OF THIS PROJECT GUTENBERG.+""" +
      """\*\*\*(.+)\*\*\* END OF THIS PROJECT GUTENBERG""").r

    // Regex to capture different parts of each article.
    val ArticleRE = (
      """(?s)(\d+)\n+""" + // The article number.
      """(.+?)\n+""" + // The title (note non-greedy match).
      """((?:(?:For|From)[^\n]+?)?)\s+""" + // The publication venue (optional).
      """((?:(?:Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday).+\d\d\d\d\.)?)\n+""" + // The date (optional).
      """((?:MAD|HAM|JAY).+?)\n+""" + // The author(s).
      """(To the [^\n]+)""" + // The addressee.
      """(.+)""" // The text.
      ).r

    val book = io.Source.fromFile(filename).mkString
    val text = JustTextRE.findAllIn(book).matchData.next.group(1)
    val rawArticles = text.split("FEDERALIST.? No. ")

    // Use the regular expression to parse the articles.
    val allArticles = rawArticles.flatMap {
      case ArticleRE(id, title, venue, date, author, addressee, text) =>
        Some(Map("id" -> id.trim,
          "title" -> title.replaceAll("\\n+", " ").trim,
          "venue" -> venue.replaceAll("\\n+", " ").trim,
          "date" -> date.replaceAll("\\n+", " ").trim,
          "author" -> author.replaceAll("\\n+", " ").trim,
          "addressee" -> addressee.trim,
          "text" -> text.trim))

      case _ => None
    }.toIndexedSeq

    // Get rid of article 71, which is a duplicate, and return the rest.
    allArticles.take(70) ++ allArticles.slice(71, allArticles.length)
  }

}
