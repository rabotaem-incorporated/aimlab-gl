package engine

abstract class Component(val entity: Entity, val scene: Scene) {
    open fun onCreate() {}
    open fun onTick() {}
    open fun onDestroy() {}
}
