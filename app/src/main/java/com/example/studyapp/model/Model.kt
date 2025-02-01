package com.example.studyapp.model

data class Task(
    val text: String,
    var isCompleted: Boolean = false
)

data class Subject(
    var name: String,
    var tasks: MutableList<Task> = mutableListOf()
)