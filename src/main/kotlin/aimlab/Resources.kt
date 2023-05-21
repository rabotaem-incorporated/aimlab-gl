package aimlab

import engine.GameLaunchContext
import graphics.Texture
import graphics.Vao

object Resources {
    fun load(launcher: GameLaunchContext) {
        monkey = launcher.loadModel("/suzanne.obj")
        coords = launcher.loadModel("/coords.obj")
        grid = launcher.loadModel("/unitgrid.obj")
        ball = launcher.loadModel("/ball.obj")
        quad = launcher.loadModel("/quad.obj")

        pepega = launcher.loadTexture("/pepega.jpg")
    }

    lateinit var monkey: Vao
    lateinit var coords: Vao
    lateinit var grid: Vao
    lateinit var ball: Vao
    lateinit var quad: Vao

    lateinit var pepega: Texture
}