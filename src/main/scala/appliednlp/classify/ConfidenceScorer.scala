package appliednlp.classify

/**
 * An application that takes a gold labeled file and a file containing
 * predictions, and then computes the accuracy for the top-third most
 * confident instances and the accuracy for the bottom-third (least 
 * confident instances).
 */
object ConfidenceScorer {

  import org.rogach.scallop._

  def main(args: Array[String]) {
    val opts = ConfidenceScorerOpts(args)

    val lines = io.Source.fromFile(opts.goldFile()).getLines.map(_.split(" ").last)
      .zip(io.Source.fromFile(opts.predictFile()).getLines
      .map({line => val splitted = line.split(" ")
            // Parse the predicted data
           (splitted(0), splitted(1).toDouble)
          }))
       .map({case (gold, (result, confidence)) =>
             // Check if correct
             (confidence, result(0) == gold(0))
           })
    // returns (confidence, correct)
      .toList.sortBy(_._1)

    val size = lines.size
    val result = Seq(lines.take(size/3), lines.drop(size/3).take(size/3), lines.drop(size/3*2)).map(
      lines => lines.count(_._2).toDouble/lines.size
      )
    val top = result(2)
    val middle = result(1)
    val lower = result(0)
    println(s"High confidence accuracy: $top")
    println(s"Mid confidence accuracy: $middle")
    println(s"Low confidence accuracy: $lower")
  }

}


object ConfidenceScorerOpts {

  import org.rogach.scallop._
  
  def apply(args: Array[String]) = new ScallopConf(args) {
    banner("""
For usage see below:
	     """)
    val goldFile = trailArg[String]("gold", descr = "File containing the gold standard")
    val predictFile = trailArg[String]("predict", descr = "File containing the predictions")
  }
}
