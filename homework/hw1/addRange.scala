// Get the arguments from the command line and check there are two of
// them and that they are all integers. (Don't change the next block
// of code.)
val IntegerPattern = """-?\d+""".r
if (args.length != 2 || args.exists(arg => !IntegerPattern.pattern.matcher(arg).matches)) {
  System.err.println("Incorrect arguments to variables.scala. Please provide two integers.")
  System.exit(1)
}

////////////////////////////////////////////////////////////////////////
// Start your work here

// Obtain the numbers, converting them from Strings to Ints while
// doing so. 

val numbers = args.map(_.toInt)

// Check that the first number is smaller than the second. If it
// isn't, print a warning message and exit.

if (numbers(0) > numbers(1)) {
  System.err.println("Please make sure that the first number is smaller than the second.")
  System.exit(1) // 0 means success
}

// Create a range using "to" and the numbers provided as arguments. 

val range = numbers(0) to numbers(1)

// Print the numbers in the range with "+" in between each one (hint:
// use mkString) and the result of adding them all together (hint: use
// sum)

println("%s = %d".format(range.toList.mkString(" + "), range.sum))
