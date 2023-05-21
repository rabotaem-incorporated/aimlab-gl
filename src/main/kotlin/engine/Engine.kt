package engine

import glm_.vec4.Vec4
import graphics.*

class GameLaunchContext(
    val glfwContext: GlfwContext,
) {
    val sceneManager: SceneManager = SceneManager()

    fun loadModel(path: String): Vao {
        return Mesh.loadObj(path).vao(glfwContext)
    }

    fun loadObj(path: String): Mesh {
        return Mesh.loadObj(path)
    }

    fun loadTexture(path: String): Texture {
        return Texture(path)
    }
}

class GameTickContext(
    val glfwContext: GlfwContext,
    private val shader: ShaderProgram,
    val sceneManager: SceneManager,
) {
    val time = glfwContext.time
    val input = glfwContext.input

    fun draw(block: ShaderProgram.() -> Unit) {
        shader.block()
    }
}

class GameConfig {
    val runOnStartup = mutableListOf<GameLaunchContext.() -> Unit>()
    val runOnTick = mutableListOf<GameTickContext.() -> Unit>()

    fun startup(block: GameLaunchContext.() -> Unit) {
        runOnStartup.add(block)
    }

    fun mainLoop(block: GameTickContext.() -> Unit) {
        runOnTick.add(block)
    }
}

fun launchGame(block: GameLaunchContext.() -> Unit) = NativeAllocatorContext.new {
    GlfwContext(
        WindowSettings(),
        GlfwSettings(),
    ).apply {
        val gameConfig = GameConfig()
        val gameLaunchContext = GameLaunchContext(this)
        gameLaunchContext.block()

        val shader = compileShaderProgram {
            vertex("/engine.vert")
            fragment("/engine.frag")

            uniform("mode")
            uniform("projectionView")
            uniform("transform")
            uniform("solidColor")
            uniform("ambientLight")
            uniform("diffuseLight")
            uniform("specularLight")
            uniform("specularDir")
            uniform("lightDir")
            uniform("viewPos")
            uniform("shininess")
        }

        gameConfig.runOnStartup.forEach {
            gameLaunchContext.it()
        }

        gameLaunchContext.sceneManager.scene.start()

        mainLoop {
            clear(Vec4())

            shader.using {
                val gameTickContext = GameTickContext(
                    this@apply,
                    this@using,
                    gameLaunchContext.sceneManager,
                )

                gameTickContext.sceneManager.scene.tickContext = gameTickContext

                gameConfig.runOnTick.forEach { gameTickContext.it() }

                gameTickContext.sceneManager.scene.tick()
            }
        }
    }
}
