
enum class RouteType(val id: Int) {
    FORWARD(0),
    BACKWARD(1),
    LEFT(2),
    RIGHT(3);
}

object Control {
    val BLINK_DURATION = 1000

    fun run() {
        Memory.setHeap(Memory.DYNAMIC_HEAP)
        while (true) {
            executeCommand()
            Memory.cleanDynamicHeap()
        }
    }

    fun executeCommand() {
        val task = Reader.readTask()
        Leds.blink()
        Time.wait(BLINK_DURATION)

        when (task.type.id) {
            TaskRequest.Type.DEBUG.id -> debugTask()
            TaskRequest.Type.ROUTE.id -> routeTask()
        }

        Leds.blink()
        Time.wait(BLINK_DURATION)
    }

    fun debugTask() {
        val request = Reader.readDebug()

        when (request.type.id) {
            DebugRequest.Type.MEMORY_STATS.id -> sendMemoryStats()
        }
    }

    fun routeTask() {
        val route = Reader.readRoute()

        val times = route.times
        val directions = route.directions
        var j = 0

        while (j < times.size) {
            val time = times[j]
            val direction = directions[j]

            when (direction) {
                RouteType.FORWARD.id -> Engine.forward()
                RouteType.BACKWARD.id -> Engine.backward()
                RouteType.LEFT.id -> Engine.left()
                RouteType.RIGHT.id -> Engine.right()
            }

            Time.wait(time)
            Engine.stop()
            j++
        }

        val response = RouteResponse.BuilderRouteResponse(0).build()
        Writer.writeRoute(response)
    }

    fun sendMemoryStats() {
        val stats = DebugResponseMemoryStats.BuilderDebugResponseMemoryStats(
                DebugInfo.getDynamicHeapTail(),
                DebugInfo.getStaticHeapTail(),
                DebugInfo.getDynamicHeapMaxSize(),
                DebugInfo.getDynamicHeapTotalBytes()
        ).build()

        Writer.writeMemoryStats(stats)
    }
}