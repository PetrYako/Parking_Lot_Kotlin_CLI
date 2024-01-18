package parking

enum class Filter {
    REG_BY_COLOR, SPOT_BY_COLOR
}

class Car(val regNumber: String, val color: String)

class Spot(val number: Int, var car: Car?)

class Parking(spots: Int) {
    private val spotsStatus: List<Spot> = List(spots) { Spot(it + 1, null) }

    init {
        println("Created a parking lot with $spots spots.")
    }

    fun park(car: Car) {
        val freeSpot = spotsStatus.indexOfFirst { it.car == null }

        if (freeSpot == -1) {
            println("Sorry, the parking lot is full.")
            return
        }

        spotsStatus[freeSpot].car = car
        println("${car.color} car parked in spot ${freeSpot + 1}.")
    }

    fun leave(spot: Int) {
        if (spotsStatus[spot - 1].car == null) {
            println("There is no car in spot $spot.")
            return
        }

        spotsStatus[spot - 1].car = null
        println("Spot $spot is free.")
    }

    fun status() {
        if (spotsStatus.all { it.car == null }) {
            println("Parking lot is empty.")
            return
        }

        spotsStatus.forEach {
            if (it.car != null) {
                println("${it.number} ${it.car!!.regNumber} ${it.car!!.color}")
            }
        }
    }

    fun findByColor(color: String, fieldToPrint: Filter) {
        val spotsWithCar = spotsStatus.filter { it.car != null && it.car!!.color.lowercase() == color.lowercase() }

        if (spotsWithCar.isEmpty()) {
            println("No cars with color $color were found.")
            return
        }

        when (fieldToPrint) {
            Filter.REG_BY_COLOR -> println(spotsWithCar.joinToString { it.car!!.regNumber })
            Filter.SPOT_BY_COLOR -> println(spotsWithCar.joinToString { it.number.toString() })
        }
    }

    fun findByRegNumber(regNumber: String) {
        spotsStatus.forEach {
            if (it.car != null && it.car!!.regNumber == regNumber) {
                println(it.number)
                return
            }
        }
        println("No cars with registration number $regNumber were found.")
    }
}

fun main() {
    var parking: Parking? = null

    var input = readln().split(" ")
    var command = input[0]
    while (command != "exit") {
        if (command == "create") {
            val spots = input[1].toInt()
            parking = Parking(spots)
        }

        if (parking != null) {
            when (command) {
                "park" -> {
                    val regNumber = input[1]
                    val color = input[2]
                    parking.park(Car(regNumber, color))
                }

                "leave" -> {
                    val spot = input[1].toInt()
                    parking.leave(spot)
                }

                "status" -> {
                    parking.status()
                }

                "reg_by_color" -> {
                    val color = input[1]
                    parking.findByColor(color, Filter.REG_BY_COLOR)
                }

                "spot_by_color" -> {
                    val color = input[1]
                    parking.findByColor(color, Filter.SPOT_BY_COLOR)
                }

                "spot_by_reg" -> {
                    val regNumber = input[1]
                    parking.findByRegNumber(regNumber)
                }
            }
        } else {
            println("Sorry, a parking lot has not been created.")
        }

        input = readln().split(" ")
        command = input[0]
    }
}
