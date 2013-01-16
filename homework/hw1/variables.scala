// Variables exercise

// Get the arguments from the command line and check there are two of
// them and that they are all integers. (Don't change the next block
// of code.)
val IntegerPattern = """-?\d+""".r
if (args.length != 2 || args.exists(arg => !IntegerPattern.pattern.matcher(arg).matches)) {
  println("Incorrect arguments to variables.scala. Please provide two integers.")
  System.exit(1)
}

////////////////////////////////////////////////////////////////////////
// Start your work here

// Obtain the numbers, converting them from Strings to Ints while
// doing so. 

val numbers = args.map(_.toInt)

// Add the numbers and print the result.

val sum = numbers.sum
println("%d + %d = %d".format(numbers(0), numbers(1), sum))

// Multiply the numbers and print the result.

val product = numbers.product
println("%d * %d = %d".format(numbers(0), numbers(1), product))

// Compare the two numbers and set the "smaller" and "larger"
// variables appropriately.

val smaller = numbers.min
val larger = numbers.max

// Print out which number is smaller and which is larger.

println("Smaller: %d".format(smaller))
println("Larger: %d".format(larger))

// Calculate the value of adding the two numbers and multiplying the
// result by the smaller number.

val result = sum * smaller
println("(%d + %d) * %d = %d".format(numbers(0), numbers(1), smaller, result))
