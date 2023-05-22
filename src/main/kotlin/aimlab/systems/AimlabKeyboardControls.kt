package aimlab.systems

import engine.systems.KeyboardControls
import engine.Scene
import graphics.Key
import aimlab.Settings
import graphics.KeyButtonStatus

class AimlabKeyboardControls(scene: Scene) : KeyboardControls(scene) {
    fun IncreaseSensitivity() {
        if (scene.tickContext!!.input.getKeyStatus(Key.LCTRL) == KeyButtonStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(Key.LSHIFT) == KeyButtonStatus.UP &&
            scene.tickContext!!.input.getKeyStatus(Key.PLUS) == KeyButtonStatus.PRESS) {
            Settings.increaseSensitivity()
        }
    }

    fun DecreaseSensitivity() {
        if (scene.tickContext!!.input.getKeyStatus(Key.LCTRL) == KeyButtonStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(Key.LSHIFT) == KeyButtonStatus.UP &&
            scene.tickContext!!.input.getKeyStatus(Key.MINUS) == KeyButtonStatus.PRESS) {
            Settings.decreaseSensitivity()
        }
    }

    fun IncreaseSensitivityFast() {
        if (scene.tickContext!!.input.getKeyStatus(Key.LCTRL) == KeyButtonStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(Key.LSHIFT) == KeyButtonStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(Key.PLUS) == KeyButtonStatus.PRESS
        ) {
            Settings.increaseSensitivityFast()
        }
    }

    fun DecreaseSensitivityFast() {
        if (scene.tickContext!!.input.getKeyStatus(Key.LCTRL) == KeyButtonStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(Key.LSHIFT) == KeyButtonStatus.DOWN &&
            scene.tickContext!!.input.getKeyStatus(Key.MINUS) == KeyButtonStatus.PRESS
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