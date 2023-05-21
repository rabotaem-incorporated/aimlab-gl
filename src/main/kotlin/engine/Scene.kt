package engine

import graphics.GlfwContext

class Scene(val glfwContext: GlfwContext) {
    val entities = mutableListOf<Entity>()
    val systems = mutableListOf<System>()
    var tickContext: GameTickContext? = null

    fun start() {
        for (system in systems) system.onStart()
        for (entity in entities) entity.tick()
    }

    fun tick() {
        for (system in systems) system.beforeTick()
        for (entity in entities) entity.tick()
        for (system in systems) system.afterTick()
    }

    fun create(parent: Entity? = null, block: Entity.() -> Unit): Entity {
        val entity = Entity(this, parent)
        parent?.children?.add(entity) ?: entities.add(entity)
        entity.block()
        return entity
    }

    inline fun <reified T> query(): T {
        for (system in systems) if (system is T) return system
        throw IllegalStateException("System ${T::class} not found")
    }
}
