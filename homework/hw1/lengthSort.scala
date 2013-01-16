// Part a

val sorted = args(0).split(' ').map((word) => (word.length, word)).sorted

println("Sorted: " + sorted.map((word) => word._2 + ":" + word._1).mkString(" "))

// Part b

val remixed = sorted.filter(_._1 <= 3) ++ sorted.filter((word) => word._1 > 3 && word._1 < 11).reverse ++ sorted.filter(_._1 >= 11)

println("Remixed: " + remixed.map((word) => word._2 + ":" + word._1).mkString(" "))

