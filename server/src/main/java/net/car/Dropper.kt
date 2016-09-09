package net.car

import net.car.client.Client
import objects.CarReal
import objects.Environment
import java.net.ConnectException
import kotlin.concurrent.thread

object Dropper {

    private val WAIT_TIMEOUT = 2L * 60 * 1000

    fun createCarsDestroyThread(): Thread {
        return thread(false, false, null, "dropCar", -1, getThreadCode())
    }

    private fun getThreadCode(): () -> Unit {
        return {
            var stopped = false
            while (!stopped) {
                synchronized(Environment, {
                    dropInactiveCar(Environment)
                })
                try {
                    Thread.sleep(WAIT_TIMEOUT)
                } catch (e: InterruptedException) {
                    println("thread for destroy cars stopped")
                    stopped = true
                }
            }
        }
    }

    private fun dropInactiveCar(environment: Environment) {
        for (key in environment.map.keys) {
            try {
                val carValue = environment.map[key] ?: continue
                if (carValue !is CarReal) {
                    continue
                }
                carValue.carConnection.sendRequest(Client.Request.PING, ByteArray(0))
            } catch (e: ConnectException) {
                environment.map.remove(key)
            }
        }
    }
}