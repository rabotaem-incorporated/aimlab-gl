package engine

import engine.components.Transform
import glm_.quat.Quat
import glm_.vec3.Vec3

class Entity(val scene: Scene, val parent: Entity?) {
    val children = mutableListOf<Entity>()
    val components = mutableListOf<Component>()
    val transform = addComponent(Transform(this, scene, Vec3(), Quat(), 1.0f))

    inline fun <reified T : Component> query(): T? {
        for (component in components) {
            if (component is T) return component
        }
        return null
    }

    fun <T: Component> addComponent(component: T): T {
        components.add(component)
        component.onCreate()
        return component
    }

    fun tick() {
        for (component in components) component.onTick()
        for (child in children) child.tick()
    }

    fun destory() {
        for (component in components) component.onDestroy()
        for (child in children) child.destory()
        parent?.children?.remove(this)
        scene.entities.remove(this)
    }
}