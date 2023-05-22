package engine.systems

import engine.Scene
import engine.System
import graphics.InputKey

open class KeyboardControls(scene: Scene) : System(scene) {
    private fun ExitOnEscape() {
        if (scene.tickContext!!.input.isKeyPressed(InputKey.ESCAPE)) {
            scene.tickContext!!.glfwContext.close()
        }
    }

    override fun beforeTick() {
        ExitOnEscape()
    }
}