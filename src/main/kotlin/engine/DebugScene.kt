package engine

import engine.components.Renderer
import engine.systems.DebugCamera
import engine.systems.KeyboardControls
import engine.systems.Light
import engine.systems.RenderPipeline
import glm_.glm
import glm_.vec3.Vec3

/**
 * Сцена для отладки, можно менять.
 */
fun main(): Unit = launchGame {
    it.scene = Scene(it)

    val model = loadModel("/suzanne.obj")
    val coordsModel = loadModel("/coords.obj")
    val gridModel = loadModel("/unitgrid.obj")

    it.scene.systems.add(RenderPipeline(it.scene))
    it.scene.systems.add(KeyboardControls(it.scene))
    it.scene.systems.add(Light(it.scene, Vec3(0.0f, 1.0f, 0.0f)))

    // sceneManager.scene.aimlab.components.systems.add(
    //     SceneCamera(
    //         Camera(
    //             Vec3(0.0f, 0.0f, 3.0f),
    //             0.0f, 0.0f,
    //             60.0f,
    //             this.glfwContext,
    //         ),
    //         sceneManager.scene
    //     )
    // )

    it.scene.systems.add(DebugCamera(it.scene))

    val entity = it.scene.create {
        addComponent(Renderer(this, SolidColorMaterial(Vec3(1.0f, 0.0f, 0.0f)), model))
    }

    entity.transform.position = Vec3(-5.0f, -5.0f, -5.0f)


    it.scene.create {
        addComponent(Renderer(this, SolidColorMaterial(Vec3(1.0f, 1.0f, 1.0f)), coordsModel))
    }

    it.scene.create {
        addComponent(Renderer(this, SolidColorMaterial(Vec3(1.0f, 1.0f, 1.0f)), gridModel))
    }

    val coords = it.scene.create {
        addComponent(Renderer(this, SolidColorMaterial(Vec3(1.0f, 1.0f, 1.0f)), coordsModel))
    }

    coords.transform.position = Vec3(1.0f, 2.0f, 3.0f)
    coords.transform.scale = 0.5f
    coords.transform.rotation = glm.quatLookAt(Vec3(1.0f, 1.0f, 1.0f), Vec3(0.0f, 1.0f, 0.0f))


    val coords2 = it.scene.create(coords) {
        addComponent(Renderer(this, SolidColorMaterial(Vec3(1.0f, 0.0f, 1.0f)), coordsModel))
    }

    coords2.transform.position = Vec3(8.0f, 0.0f, 0.0f)
    coords2.transform.scale = 0.5f
}
