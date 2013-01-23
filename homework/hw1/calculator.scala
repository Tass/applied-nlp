// Calculator exercise

// Get the arguments from the command line and check there are three of
// them and that they are of the right type. (Don't change the next block
// of code.)
val IntegerPattern = """-?\d+""".r
val Operators = """(plus|minus|times|div)""".r
args match {
  case Array(first, op, second) => 
    if (!IntegerPattern.pattern.matcher(first).matches 
        || !IntegerPattern.pattern.matcher(second).matches) {
      System.err.println("Please provide integers as arguments to the operator.")
      System.exit(1)
    }
    if (!Operators.pattern.matcher(op).matches) {
      System.err.println("Please provide a valid operator.")
      System.exit(1)
    }

  case _ => 
    System.err.println("Incorrect number of arguments to calculator.scala. Please provide three arguments.")
    System.exit(1)
}


////////////////////////////////////////////////////////////////////////
// Start your work here. The code above has ensured that the inputs
// are correct, so you don't need to worry about that.

// Initialize variables num1, op and num2 based on the values in the args array.

val num1 = args(0).toInt: Int
val op = args(1)
val num2 = args(2).toInt: Int

// Obtain a result by using the string name of the operator to choose
// which arithmetic operation to use.

val func = op match {
  case "plus" => (x:Int,y:Int) => (x + y).toFloat
  case "minus" => (x:Int,y:Int) => (x - y).toFloat
  case "times" => (x:Int,y:Int) => (x * y).toFloat
  case "div" => (x:Int,y:Int) => x / y.toFloat
}
val result = func(num1, num2)

// Print the result.

println("%d %s %d = %f".format(num1, op, num2, result))


