package engine

/**
 * Абстрактный класс для систем.
 *
 * Системы - это что-то вроде глобальных компонентов сцены. В отличие от обычных компонентов,
 * в системе есть две функции [beforeTick] и [afterTick], которые вызываются перед и после
 * того, как у всех компонентов сцены вызовется [Component.onTick].
 *
 * Системы могут обращаться к другим системам. Получить систему данного типа можно с помощью
 * функции [Scene.query].
 */
abstract class System(val scene: Scene) {
    /**
     * Вызывается при добавлении системы в сцену.
     */
    open fun onStart() {}

    /**
     * Вызывается при удалении системы из сцены, или при замене сцены на другую.
     * @see [Game]
     */
    open fun onStop() {}

    /**
     * Вызывается перед вызовом [Component.onTick] у всех компонентов сцены,
     * в порядке добавления систем в сцену.
     */
    open fun beforeTick() {}

    /**
     * Вызывается после вызова [Component.onTick] у всех компонентов сцены,
     * в порядке добавления систем в сцену.
     */
    open fun afterTick() {}

    val input get() = scene.game.input
    val time get() = scene.game.time
    val glfwContext get() = scene.game.glfwContext
    val game get() = scene.game
}
