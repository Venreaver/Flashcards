fun main(args: Array<String>) {
    if (args.size == 3) {
        args.forEachIndexed { i, w -> println("Argument ${i + 1}: $w. It has ${w.length} characters") }
    } else println("Invalid number of program arguments")
}