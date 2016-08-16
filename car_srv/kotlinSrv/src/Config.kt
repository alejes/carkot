/**
 * Created by user on 8/16/16.
 */
class Config(val configFileName: String) {


    private var serverIp = "127.0.0.1"

    fun loadConfig(): Boolean {
        val data: String = fs.readFileSync(configFileName, "utf8")
        data.split("\n").forEach { line ->
            val keyValue = line.split(":")
            if (keyValue.size == 2) {
                when (keyValue[0]) {
                    "ip" -> serverIp = keyValue[1]
                }
            } else {
                return false
            }
        }
        return true
    }

    fun getIp(): String {
        return serverIp
    }
}