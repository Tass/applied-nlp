package appliednlp.classify
import nak.core.AttrVal
object TextFeaturesOpts {

  import org.rogach.scallop._
  
  def apply(args: Array[String]) = new ScallopConf(args) {
    banner("""
For usage see below:
	     """)
    val help = opt[Boolean]("help", noshort = true, descr = "Show this message")
    val inputFile = trailArg[String]("inputfile", descr = "Input file to create features from.")
  }
}


object TextFeatures {

  /**
   * The main method -- do the work. Don't change it.
   */
  def main(args: Array[String]) {

    // Parse and get the command-line options
    val opts = TextFeaturesOpts(args)
    io.Source.fromFile(opts.inputFile()).getLines.foreach({
      line => 
      val splitted = line.split('\t')
      val features = extractFeatures(splitted(1))
      println(features.map(_.toString).mkString(",") + "," + splitted(0))
    })
  }

  def extractFeatures(sms: String): Iterable[AttrVal] = {
    val somePunctuation = """\?!,'\"$""".r
    val fullPunctuation = """-+:;""".r // likely included in smilies

    List() ++
    extract(("length", sms))(sms => (sms.length / 10).toString) ++ 
    extract(("number", sms))("""\d{2,}""".r.findFirstMatchIn(_).nonEmpty.toString) ++ 
    extract(("markers", sms))( sms =>  sms.take(sms.length - 2) match {
      case somePunctuation(_) => "some"
      case fullPunctuation(_) => "full"
      case _ => "none"
    }) ++ 
    extract(("dots", sms))("""\.\.""".r.findFirstMatchIn(_).nonEmpty.toString) ++ 
    extract(("uppercase", sms))("""[^\.] [A-Z]""".r.findFirstMatchIn(_).nonEmpty.toString)
  }
    
  def extract[T](features: (String, T)*)(f: T => String) = features.map({case (name, value) => AttrVal(name, f(value))})

}
