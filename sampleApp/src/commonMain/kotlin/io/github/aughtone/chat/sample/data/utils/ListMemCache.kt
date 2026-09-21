package io.github.aughtone.chat.sample.data.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ListMemCache<Id, Item> {
    private val cache = MutableStateFlow<Map<Id, Item>>(emptyMap())

    fun read(id: Id): Item? = cache.value[id]

    fun write(id: Id, item: Item) {
        cache.value = cache.value + (id to item)
    }

    fun delete(id: Id) {
        cache.value = cache.value - id
    }

    fun clear() {
        cache.value = emptyMap()
    }

    fun readAll(): List<Item> = cache.value.values.toList()

    fun flow(): Flow<List<Item>> = cache.map { it.values.toList() }
}