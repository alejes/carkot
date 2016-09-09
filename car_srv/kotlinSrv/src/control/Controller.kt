package control

import RouteMetricRequest
import RouteRequest
import SonarExploreAngleRequest
import SonarRequest

interface Controller {
    fun executeRoute(route: RouteRequest, callback: (ByteArray) -> Unit)
    fun executeMetricRoute(request: RouteMetricRequest, callback: (ByteArray) -> Unit)
    fun executeRequestSensorData(sonarRequest: SonarRequest, callback: (ByteArray) -> Unit)
    fun executeRequestSensorExploreData(request: SonarExploreAngleRequest, callback: (ByteArray) -> Unit)
}