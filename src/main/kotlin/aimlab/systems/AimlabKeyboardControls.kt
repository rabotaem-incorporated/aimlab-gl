package aimlab.systems

import engine.systems.KeyboardControls
import engine.Scene
import graphics.InputKey
import aimlab.Settings
import graphics.InputKeyStatus

class AimlabKeyboardControls(scene: Scene) : KeyboardControls(scene) {
    private fun IncreaseSensitivity() {
        if (scene.tickContext!!.input.getKeyStatus(InputKey.LCTRL) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.LSHIFT) == InputKeyStatus.UP &&
            scene.tickContext!!.input.getKeyStatus(InputKey.PLUS) == InputKeyStatus.PRESSED) {
            Settings.increaseSensitivity()
        }
    }

    private fun DecreaseSensitivity() {
        if (scene.tickContext!!.input.getKeyStatus(InputKey.LCTRL) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.LSHIFT) == InputKeyStatus.UP &&
            scene.tickContext!!.input.getKeyStatus(InputKey.MINUS) == InputKeyStatus.PRESSED) {
            Settings.decreaseSensitivity()
        }
    }

    private fun IncreaseSensitivityFast() {
        if (scene.tickContext!!.input.getKeyStatus(InputKey.LCTRL) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.LSHIFT) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.PLUS) == InputKeyStatus.PRESSED
        ) {
            Settings.increaseSensitivityFast()
        }
    }

    private fun DecreaseSensitivityFast() {
        if (scene.tickContext!!.input.getKeyStatus(InputKey.LCTRL) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.LSHIFT) == InputKeyStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(InputKey.MINUS) == InputKeyStatus.PRESSED
        ) {
            Settings.decreaseSensitivityFast()
        }
    }

    override fun beforeTick() {
        super.beforeTick()
        IncreaseSensitivity()
        DecreaseSensitivity()
        IncreaseSensitivityFast()
        DecreaseSensitivityFast()
    }
}