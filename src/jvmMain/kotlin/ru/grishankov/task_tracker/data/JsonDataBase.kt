package ru.grishankov.task_tracker.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object JsonDataBase : KoinComponent {

    const val DATABASE = "database.json"
    val fileSystemService: FileSystemService by inject()

    suspend inline fun <reified T : DataBaseObject> saveAll(values: List<T>) = withContext(Dispatchers.IO) {
        val jsonValues = arrayListOf<JsonElement>()
        values.forEach {
            jsonValues += Json.encodeToJsonElement(it)
        }
        val json = JsonArray(jsonValues)
        fileSystemService.saveFileJson(DATABASE, json.toString())
    }

    suspend inline fun <reified T : DataBaseObject> loadAll() = withContext(Dispatchers.IO) {
        val json = fileSystemService.readFileJson(DATABASE)
        Json.decodeFromString<List<T>>(json)
    }

    suspend inline fun <reified T : DataBaseObject> getById(id: Int) = withContext(Dispatchers.IO) {
        loadAll<T>().find { it.id == id }
    }

    suspend inline fun <reified T : DataBaseObject> update(id: Int, crossinline value: (T) -> T) = withContext(Dispatchers.IO) {
        val list = loadAll<T>().toMutableList()
        val index = list.indexOfLast { it.id == id }
        if (index >= 0) {
            val oldValue = list[index]
            list[index] = value(oldValue)
        }
        saveAll(list)
    }

    suspend inline fun <reified T : DataBaseObject> create(crossinline value: (lastId: Int) -> T) = withContext(Dispatchers.IO) {
        val list = loadAll<T>().toMutableList()
        val lastId = list.lastOrNull()?.id ?: 0
        val newValue = value(lastId)
        list += newValue
        saveAll(list)
    }

    suspend inline fun <reified T : DataBaseObject> delete(id: Int) {
        val list = loadAll<T>().toMutableList()
        list.removeIf { it.id == id }
        saveAll(list)
    }

    suspend inline fun <reified T : DataBaseObject> updateAll(crossinline value: (T) -> T) = withContext(Dispatchers.IO) {
        val list = loadAll<T>()
        val mapped = list.map(value)
        saveAll(mapped)
    }
}

interface DataBaseObject {
    val id: Int
}