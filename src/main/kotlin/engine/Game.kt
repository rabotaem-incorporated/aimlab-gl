package engine

import graphics.GlfwContext

/**
 * Класс, отвечающий за общее состояние игры.
 *
 * Содержит текущую сцену и предоставляет доступ к низкоуровневым ресурсам: времени и вводу.
 *
 * Чтобы изменить текущую сцену, нужно просто присвоить полю [scene] новую сцену.
 * Начальную сцену можно задать в конструкторе, или позднее.
 *
 * После того как сцена была изменена, вызывается [Scene.stop] у старой сцены, и [Scene.start] у новой.
 * Переиспользование сцен не поддерживается.
 *
 * @see [Scene]
 */
class Game(val glfwContext: GlfwContext) {
    /**
     * Shortcut для [GlfwContext.time].
     */
    val time get() = glfwContext.time

    /**
     * Shortcut для [GlfwContext.input].
     */
    val input get() = glfwContext.input

    private var sceneOrNull: Scene? = null

    /**
     * Текущая сцена. Чтобы изменить текущую сцену, нужно просто присвоить полю [scene] новую сцену.
     */
    var scene: Scene
        get() = sceneOrNull ?: throw IllegalStateException("Scene is not set")
        set(value) {
            sceneOrNull?.stop()
            sceneOrNull = value
            value.start()
        }

    /**
     * Создаёт новую сцену и после инициализации делает ее текущей.
     */
    fun newScene(block: Scene.() -> Unit) {
        val scene = Scene(this)
        scene.block()
        this.scene = scene
    }
}
