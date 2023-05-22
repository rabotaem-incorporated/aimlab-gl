package graphics

/**
 * Так как системное время для игры не годится, для работы с временем используется этот класс.
 *
 * Время измеряется в секундах с момента запуска игры.
 *
 * @see [Input]
 */
class Time {
    private fun realTime(): Double {
        return System.currentTimeMillis().toDouble() / 1000.0
    }

    private val startTime = realTime()

    /**
     * Время в секундах с момента запуска игры.
     * Гарантируется, что в течении одного кадра это значение не изменится.
     */
    var current: Float = 0.0f
        private set

    /**
     * Время в секундах, прошедшее с предыдущего кадра.
     */
    var delta: Float = 0.0f
        private set

    /**
     * Вызывается в начале каждого кадра, за это отвечает [GlfwContext]
     */
    fun tick() {
        val currentTime = realTime() - startTime
        delta = (currentTime - current).toFloat()
        current = currentTime.toFloat()
    }
}
