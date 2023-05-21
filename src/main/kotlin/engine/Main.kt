package engine

import engine.components.Renderer
import engine.components.SolidColorMaterial
import engine.systems.DebugCamera
import engine.systems.ExitOnEscape
import engine.systems.RenderPipeline
import glm_.glm
import glm_.vec3.Vec3

fun main() = launchGame {
    sceneManager.scene = Scene(glfwContext)

    val model = loadModel("/suzanne.obj")
    val coordsModel = loadModel("/coords.obj")
    val gridModel = loadModel("/unitgrid.obj")

    sceneManager.scene.systems.add(RenderPipeline(sceneManager.scene))
    sceneManager.scene.systems.add(ExitOnEscape(sceneManager.scene))

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

    sceneManager.scene.systems.add(DebugCamera(sceneManager.scene))

    val entity = sceneManager.scene.create {
        addComponent(Renderer(this, scene, SolidColorMaterial(Vec3(1.0f, 0.0f, 0.0f)), model))
    }

    entity.transform.position = Vec3(-5.0f, -5.0f, -5.0f)


    sceneManager.scene.create {
        addComponent(Renderer(this, scene, SolidColorMaterial(Vec3(1.0f, 1.0f, 1.0f)), coordsModel))
    }

    sceneManager.scene.create {
        addComponent(Renderer(this, scene, SolidColorMaterial(Vec3(1.0f, 1.0f, 1.0f)), gridModel))
    }

    val coords = sceneManager.scene.create {
        addComponent(Renderer(this, scene, SolidColorMaterial(Vec3(1.0f, 1.0f, 1.0f)), coordsModel))
    }

    coords.transform.position = Vec3(1.0f, 2.0f, 3.0f)
    coords.transform.scale = 0.5f
    coords.transform.rotation = glm.quatLookAt(Vec3(1.0f, 1.0f, 1.0f), Vec3(0.0f, 1.0f, 0.0f))


    val coords2 = sceneManager.scene.create(coords) {
        addComponent(Renderer(this, scene, SolidColorMaterial(Vec3(1.0f, 0.0f, 1.0f)), coordsModel))
    }

    coords2.transform.position = Vec3(8.0f, 0.0f, 0.0f)
    coords2.transform.scale = 0.5f
}
