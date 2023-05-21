package engine

class SceneManager(private var sceneOrNull: Scene? = null) {
    var scene: Scene
        get() = sceneOrNull ?: throw IllegalStateException("Scene is not set")
        set(value) {
            sceneOrNull?.stop()
            sceneOrNull = value
            value.start()
        }
}