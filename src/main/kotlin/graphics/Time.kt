package graphics

class Time {
    private fun realTime(): Double {
        return System.currentTimeMillis().toDouble() / 1000.0
    }

    private val startTime = realTime()

    var current: Float = 0.0f
        private set

    var delta: Float = 0.0f
        private set

    fun tick() {
        val currentTime = realTime() - startTime
        delta = (currentTime - current).toFloat()
        current = currentTime.toFloat()
    }
}
