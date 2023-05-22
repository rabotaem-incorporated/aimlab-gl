package aimlab

import engine.GameLaunchContext
import glm_.vec3.Vec3
import graphics.*

enum class TextAlign {
    START, CENTER, END
}

object Resources {
    fun load(launcher: GameLaunchContext) {
        monkey = launcher.loadModel("/suzanne.obj")
        coords = launcher.loadModel("/coords.obj")
        grid = launcher.loadModel("/unitgrid.obj")
        ball = launcher.loadModel("/ball.obj")
        quad = launcher.loadModel("/quad.obj")
        terrain = launcher.loadModel("/terrain.obj")
        pepega = launcher.loadTexture("/pepega.jpg")
        sand = launcher.loadTexture("/sand.jpg")

        letters = mutableListOf()
        for (i in 0 until 128) {
            val letter = launcher.loadObj("/letters/letter-$i.obj")
            letter.shift(Vec3(-letter.boundingBox.min.x))
            letters.add(letter)
        }

        glfwContext = launcher.glfwContext
    }

    lateinit var glfwContext: GlfwContext

    lateinit var monkey: Vao
    lateinit var coords: Vao
    lateinit var grid: Vao
    lateinit var ball: Vao
    lateinit var quad: Vao
    lateinit var terrain: Vao

    private lateinit var letters: MutableList<Mesh>

    fun getLetter(char: Char): Mesh {
        return letters[char.code]
    }

    fun getText(text: String, horizontalAlign: TextAlign = TextAlign.START, verticalAlign: TextAlign = TextAlign.START): Vao {
        val mesh = Mesh(mutableListOf(), mutableListOf())
        val shift = Vec3()

        val widthOfSpace = getLetter('m').boundingBox.size().x * 0.7f
        val charSpacing = getLetter('m').boundingBox.size().x * 0.1f
        val heightOfLine = getLetter('Q').boundingBox.size().z * 1.3f

        for (c in text) {
            when (c) {
                ' ' -> shift.x += widthOfSpace
                '\n' -> {
                    shift.z -= heightOfLine
                    shift.x = 0.0f
                }
                else -> {
                    mesh.append(getLetter(c), shift)
                    shift.x += getLetter(c).boundingBox.size().x * 1.1f + charSpacing
                }
            }
        }

        val totalBoundingBox = mesh.boundingBox

        val alignShift = Vec3(
            -totalBoundingBox.min.x - when (horizontalAlign) {
                TextAlign.START -> 0.0f
                TextAlign.CENTER -> totalBoundingBox.size().x / 2.0f
                TextAlign.END -> totalBoundingBox.size().x
            },
            0.0f,
            -totalBoundingBox.min.z - when (verticalAlign) {
                TextAlign.END -> 0.0f
                TextAlign.CENTER -> totalBoundingBox.size().z / 2.0f
                TextAlign.START -> totalBoundingBox.size().z
            },
        )

        mesh.shift(alignShift)
        return mesh.vao(false)
    }

    lateinit var pepega: Texture
    lateinit var sand: Texture
}