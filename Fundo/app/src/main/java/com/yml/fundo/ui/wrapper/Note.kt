package com.yml.fundo.ui.wrapper

import java.io.Serializable
import java.util.*

data class Note(
    var title: String,
    var content: String,
    var dateModified:Date?,
    var key: String="",
    var id: Long = 0,
    var archived: Boolean = false,
    var reminder: Date? = null
): Serializable
