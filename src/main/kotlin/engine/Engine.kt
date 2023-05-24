package engine

import glm_.vec4.Vec4
import graphics.*

/**
 * Это контекст, в котором запускается игра, до создания сцен.
 *
 * Только в этом контексте можно подгружать модели, текстуры и т. д. из ресурсов.
 *
 * Создавать [Vao] можно и позднее, но все необходимые [Mesh] и [Texture] нужно подгрузить здесь.
 *
 * @see Vao
 * @see Mesh
 * @see Texture
 */
class GameLaunchContext(
    val glfwContext: GlfwContext,
) {
    /**
     * Загружает модель из файла и срезу же создает [Vao] для нее.
     */
    fun loadModel(path: String): Vao {
        return loadObj(path).vao()
    }

    /**
     * Загружает модель из файла, но просто возращает ее,
     *
     * не создавая [Vao] и не отправляя ничего на GPU.
     */
    fun loadObj(path: String): Mesh {
        return Mesh.loadObj(path)
    }

    /**
     * Загружает текстуру из файла.
     */
    fun loadTexture(path: String): Texture {
        return Texture(path)
    }
}

/**
 * Запускает игру. Предполагается, что эта функция вызывается в точке входа.
 *
 * При запуске обязательно нужно создать сцену.
 *
 * Компиляция шейдеров и прочая низкоуровневая инициализация происходит здесь автоматически,
 * [Game.glfwContext] трогать не следует.
 *
 * @param block блок, в котором можно подгрузить модели и текстуры.
 */
fun launchGame(block: GameLaunchContext.(Game) -> Unit) = NativeAllocatorContext.new {
    GlfwContext(
        WindowSettings(),
        GlfwSettings(),
    ).apply {
        val gameLaunchContext = GameLaunchContext(this)
        val game = Game(this)

        gameLaunchContext.block(game)

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

        game.scene.start()

        mainLoop {
            clear(Vec4())

            shader.using {
                game.scene.tick()
            }
        }
    }
}
