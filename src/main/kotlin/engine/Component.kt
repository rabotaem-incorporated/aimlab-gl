package engine

/**
 * Компоненты наполняют сущности функционалом.
 *
 * Чтобы добавить компонент сущности, нужно вызвать [Entity.addComponent].
 *
 * Этот класс необходимо расширить, чтобы создать свой компонент.
 *
 * ```kt
 * class GoUp(entity: Entity) : Component(entity) {
 *     override fun onTick() {
 *         entity.transform.position.plusAssign(Vec3(0.0f, time.delta, 0.0f))
 *     }
 * }
 * ```
 *
 * Чтобы получить компонент сущности изнутри компонента, нужно вызвать [Entity.query].
 *
 * @see System
 * @see Entity
 */
abstract class Component(val entity: Entity) {
    /**
     * Вызывается при добавлении компонента к сущности.
     */
    open fun onCreate() {}

    /**
     * Вызывается каждый кадр.
     */
    open fun onTick() {}

    /**
     * Вызывается при удалении компонента из сущности.
     *
     * В том числе при удалении сущности или удалении сцены.
     */
    open fun onDestroy() {}

    val scene get() = entity.scene
    val input get() = scene.game.input
    val time get() = scene.game.time
    val game get() = scene.game
}
