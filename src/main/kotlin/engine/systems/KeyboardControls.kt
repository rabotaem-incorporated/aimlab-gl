package engine.systems

import engine.Scene
import engine.System
import graphics.Key
import graphics.KeyButtonStatus

open class KeyboardControls(scene: Scene) : System(scene) {
    private fun ExitOnEscape() {
        if (scene.tickContext!!.input.isKeyPressed(Key.ESCAPE)) {
            scene.tickContext!!.glfwContext.close()
        }
    }

    fun ToggleFullscreen() {
        if (scene.tickContext!!.input.getKeyStatus(Key.F11) == KeyButtonStatus.PRESS) {
            scene.tickContext!!.glfwContext.fullscreen = !scene.tickContext!!.glfwContext.fullscreen
        }
    }

    override fun beforeTick() {
        ExitOnEscape()
        ToggleFullscreen()
    }
}