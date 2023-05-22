package aimlab.systems

import engine.systems.KeyboardControls
import engine.Scene
import graphics.InputKey
import aimlab.Settings
import graphics.InputKeyStatus

class AimlabKeyboardControls(scene: Scene) : KeyboardControls(scene) {
    fun IncreaseSensitivity() {
        if (scene.tickContext!!.input.getKeyStatus(InputKey.LCTRL) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.LSHIFT) == InputKeyStatus.UP &&
            scene.tickContext!!.input.getKeyStatus(InputKey.PLUS) == InputKeyStatus.PRESSED) {
            Settings.increaseSensitivity()
        }
    }

    fun DecreaseSensitivity() {
        if (scene.tickContext!!.input.getKeyStatus(InputKey.LCTRL) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.LSHIFT) == InputKeyStatus.UP &&
            scene.tickContext!!.input.getKeyStatus(InputKey.MINUS) == InputKeyStatus.PRESSED) {
            Settings.decreaseSensitivity()
        }
    }

    fun IncreaseSensitivityFast() {
        if (scene.tickContext!!.input.getKeyStatus(InputKey.LCTRL) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.LSHIFT) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.PLUS) == InputKeyStatus.PRESSED
        ) {
            Settings.increaseSensitivityFast()
        }
    }

    fun DecreaseSensitivityFast() {
        if (scene.tickContext!!.input.getKeyStatus(InputKey.LCTRL) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.LSHIFT) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.MINUS) == InputKeyStatus.PRESSED
        ) {
            Settings.decreaseSensitivityFast()
        }
    }

    fun ToggleFullscreen() {
        if (scene.tickContext!!.input.getKeyStatus(InputKey.F11) == InputKeyStatus.PRESSED) {
            scene.tickContext!!.glfwContext.fullscreen = !scene.tickContext!!.glfwContext.fullscreen
        }
    }

    override fun beforeTick() {
        super.beforeTick()
        IncreaseSensitivity()
        DecreaseSensitivity()
        IncreaseSensitivityFast()
        DecreaseSensitivityFast()
        ToggleFullscreen()
    }
}