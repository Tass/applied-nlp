// Get the argument from the command line and check that it is an
// integer.  (Don't change the next block of code.)
val IntegerPattern = """-?\d+""".r
if (args.length != 1 || !IntegerPattern.pattern.matcher(args(0)).matches) {
  println("Incorrect arguments to variables.scala. Please provide one integer.")
  System.exit(1)
}

////////////////////////////////////////////////////////////////////////
// Start your work here

// Obtain the number, converting it from a String to an Int.

val limit = args(0).toInt

// Check that the number is in the right range. If it isn't, print a
// warning message and exit.

if (limit <= 0) {
  println("Please supply a number greater than or equal to 0.")
  System.exit(1) // 0 means success
}

// Compute the factorial using recursion.

def factorial(limit: Int): Int = limit match {
  case 1 => 1
  case n => n * factorial(n-1)
}

// Print the result

println("%d! = %d".format(limit, factorial(limit)))
