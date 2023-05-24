package aimlab

import aimlab.scenes.createMainMenu
import engine.launchGame

fun main() = launchGame {
    Resources.load(this)

    createMainMenu(it)
}
