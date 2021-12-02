package com.yml.fundo.ui.wrapper

import java.io.Serializable
import java.util.*

data class Label(
    var name: String,
    var dateModified: Date?,
    var fid: String = "",
    var isChecked:Boolean = false
):Serializable
