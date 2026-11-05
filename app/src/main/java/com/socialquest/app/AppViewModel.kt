package com.socialquest.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

enum class ExperienceMode(val label: String, val blurb: String, val available: Boolean) {
    STANDARD("Standard", "Voice practice with a 2D scene on your phone or tablet.", true),
    IMMERSIVE("Immersive 3D", "A 3D room on your screen. Coming after the core app.", false),
}

/** UI state only. Replace the TODO parts with repository/API calls once the backend exists. */
class AppViewModel : ViewModel() {
    var email by mutableStateOf("")
    var nickname by mutableStateOf("")
    val situations = mutableStateListOf<String>()
    val difficulties = mutableStateListOf<String>()
    var goal by mutableStateOf("")
    var mode by mutableStateOf(ExperienceMode.STANDARD)
    var captions by mutableStateOf(true)
    var reduceMotion by mutableStateOf(false)
    var keepSummary by mutableStateOf(false)

    fun toggle(list: SnapshotStateList<String>, item: String) {
        if (!list.remove(item)) list.add(item)
    }

    fun reset() {
        email = ""; nickname = ""; goal = ""
        situations.clear(); difficulties.clear()
        mode = ExperienceMode.STANDARD
        captions = true; reduceMotion = false; keepSummary = false
    }
}
