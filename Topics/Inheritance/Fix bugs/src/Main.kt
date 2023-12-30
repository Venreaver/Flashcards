//open class Car(val model: String, val speed: Int)
//
//class Bus(val typeOfBus: String, model: String, speed: Int) : Car(model, speed) {
//    fun printInfo() = println("Type of bus: $typeOfBus, model: $model, speed: $speed")
//}
//
//fun main() {
//    val bus = Bus("Personal", "N4", 130)
//    bus.printInfo()
//}

fun main() {
    val factory = FactoryWithRoof(3, 2, 23000)
    print(factory.employeesPerFloor())
}

open class Facility(val floors: Byte) {
    fun addFloors(num: Byte): Int = floors + num
}

open class Factory(floors: Byte, val employees: Short, val roof: Byte) : Facility(floors) {
    fun buildRoof(): Int = super.addFloors(roof)
    open fun employeesPerFloor(): Int = employees / floors
}

open class FactoryWithRoof(floors: Byte, roof: Byte, employees: Short) : Factory(floors, employees, roof) {
    override fun employeesPerFloor(): Int = employees / (floors + super.buildRoof())
}