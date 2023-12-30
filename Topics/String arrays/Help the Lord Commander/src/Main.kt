fun main() {  
    val beyondTheWall = readln().split(',').map { it }.toTypedArray()
    val backFromTheWall = readln().split(',').map { it }.toTypedArray()
    println(beyondTheWall.contentEquals(backFromTheWall))
}