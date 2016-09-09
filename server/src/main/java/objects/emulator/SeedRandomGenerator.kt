package objects.emulator

class SeedRandomGenerator(seed: Long) {
    var curState = seed
    val a = 1664525L
    val c = 1013904223L
    val mod = 2147483648L

    fun nextInt(): Int {
        val res = curState
        curState = Math.abs(a * curState + c) % mod
        return (res % mod).toInt()
    }

    fun nextInt(min_val: Int, max_val: Int): Int {
        val rand = nextInt()
        val size = max_val - min_val + 1
        val randInRange = rand % size
        val res = randInRange + min_val
        return res
    }
}