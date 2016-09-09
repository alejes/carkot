package clInterface.executor

import algorithm.RouteData
import objects.Car
import objects.Environment
import roomScanner.CarController
import java.net.ConnectException

class RouteMetric : CommandExecutor {

    private val ROUTE_REGEX = Regex("route [0-9]{1,10}")
    private val HELP_STRING = "print way points als [distance/degrees] [direction] in sm and degrees." +
            "after enter all points print \"done\". for reset route print \"reset\". available directions:" +
            "0 - FORWARD, 1 - BACKWARD, 2 - LEFT, 3 - RIGHT"

    override fun execute(command: String) {
        if (!ROUTE_REGEX.matches(command)) {
            println("incorrect args of route command")
            return
        }
        val id: Int
        try {
            id = command.split(" ")[1].toInt()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            println("error in converting id to int type")
            return
        }
        val car: Car? =
                synchronized(Environment, {
                    Environment.map[id]
                })
        if (car == null) {
            println("car with id=$id not found")
            return
        }
        val routeData = getRouteMessage() ?: return
        try {
            routeData.distances.forEachIndexed { idx, distance ->
                car.moveCar(distance, routeData.directions[idx]).get()
            }
        } catch (e: ConnectException) {
            synchronized(Environment, {
                Environment.map.remove(id)
            })
        }
    }

    private fun getRouteMessage(): RouteData? {
        println(HELP_STRING)
        val distances = arrayListOf<Int>()
        val directions = arrayListOf<CarController.Direction>()
        while (true) {
            val readLine = readLine()!!.toLowerCase()
            when (readLine) {
                "reset" -> return null
                "done" -> {
                    val result = RouteData(distances.toIntArray(), directions.toTypedArray())
                    return result
                }
            }
            val wayPointData = readLine.split(" ")
            val distance: Int
            val direction: Int
            try {
                distance = wayPointData[0].toInt()
                direction = wayPointData[1].toInt()
            } catch (e: NumberFormatException) {
                println("error in converting distance or direction to int. try again")
                continue
            } catch (e: IndexOutOfBoundsException) {
                println("format error, you must print two number separated by spaces. Try again")
                continue
            }
            distances.add(distance)
            var directionCommand: CarController.Direction? = null
            when (direction) {
                0 -> directionCommand = CarController.Direction.FORWARD
                1 -> directionCommand = CarController.Direction.BACKWARD
                2 -> directionCommand = CarController.Direction.LEFT
                3 -> directionCommand = CarController.Direction.RIGHT
            }
            if (directionCommand == null) {
                println("direction $direction don't supported!")
                println(HELP_STRING)
                continue
            }
            directions.add(directionCommand)
        }
    }


}