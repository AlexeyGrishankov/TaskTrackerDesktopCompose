package ru.grishankov.task_tracker.data

import java.io.File

class FileSystemService {

    companion object {
        const val FILES_FOLDER = "files"
    }

    fun saveFileJson(path: String, json: String) {
        val file = File(FILES_FOLDER, path)
        file.createNewFile()
        file.writeText(json)
    }

    fun readFileJson(path: String): String {
        val folder = File(FILES_FOLDER)
        folder.mkdir()
        val file = File(folder, path)
        if (!file.isFile) {
            file.createNewFile()
            file.writeText("[]")
        }
        return file.readText()
    }
}