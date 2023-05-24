package engine

/**
 * Описывает игровую сцену.
 *
 * Одновременно может существовать только одна сцена.
 * Чтобы изменить текущую сцену, нужно записать новую сцену в [Game.scene].
 *
 * Сцена является гигиеничным объектом и не может быть переиспользована после вызова [stop],
 * то есть после замены сцены на другую.
 *
 * Сама сцена содержит в себе список сущностей [entities] и список систем [systems].
 *
 * Сущности представляют собой дерево, где каждая сущность может иметь несколько дочерних сущностей.
 * О них можно думать как о конкретных объектах в игре - окружение, шар, прицел, текст, и так далее.
 *
 * Сущность может иметь несколько компонентов, которые могут быть получены с помощью функции [Entity.query].
 * Системы представляют собой глобальные объекты сцены, которые существуют в единственном экземпляре,
 * например камера, источник света, движок рендеринга, и так далее.
 *
 * @see Entity
 * @see System
 * @see launchGame
 * @see Game
 */
class Scene(val game: Game) {
    /**
     * Список всех сущностей сцены.
     */
    val entities = mutableListOf<Entity>()
    val systems = mutableListOf<System>()

    /**
     * Вызывается при добавлении сцены в [Game]. Ручное использование не предполагается.
     */
    fun start() {
        for (system in systems) system.onStart()
    }

    /**
     * Вызывается при удалении сцены из [Game]. Ручное использование не предполагается.
     */
    fun stop() {
        while (entities.isNotEmpty()) entities[0].destroy()
        for (system in systems) system.onStop()
    }

    /**
     * Вызывается каждый кадр, должно вызываться в [launchGame]. Ручное использование не предполагается.
     *
     * @see [launchGame]
     */
    fun tick() {
        for (system in systems) system.beforeTick()
        for (entity in entities) entity.tick()
        for (system in systems) system.afterTick()
    }

    /**
     * Создает новую сущность на сцене.
     *
     * ```kt
     * scene.create {
     *      // transform добавляется автоматически и уже имеется
     *
     *      transform.position = Vec3(0f, 1f, 0f)
     *
     *      addComponent(Renderer(this, scene, SolidColorMaterial(Vec3(0.5, 0.7f, 1.0f)), Resources.ball))
     * }
     * ```
     *
     * @param parent родительская сущность, если не указана, то сущность будет создана на верхнем уровне,
     * то есть на самой сцене. [engine.components.Transform] родительской сущности будет примениться и на детей.
     *
     * @param block блок, в котором можно добавить компоненты сущности, создать дочерние сущности,
     * или проводить какие-то другие операции инициализации с сущностью.
     *
     * @return Ссылка на созданную сущность.
     */
    fun create(parent: Entity? = null, block: Entity.() -> Unit): Entity {
        val entity = Entity(this, parent)
        parent?.children?.add(entity) ?: entities.add(entity)
        entity.block()
        return entity
    }

    /**
     * Возвращает систему с указанным типом. Эта функция делает линейный поиск по списку систем,
     * с RTTI, поэтому не рекомендуется вызывать ее каждый кадр.
     */
    inline fun <reified T: System> query(): T {
        for (system in systems) if (system is T) return system
        throw IllegalStateException("System ${T::class} not found")
    }
}
