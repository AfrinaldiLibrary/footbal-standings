package com.klikgazz.footballstandings.model

data class ClubsModel(
    val name: String? = "",
    val city: String? = "",
    val match: String? = "0",
    var win: String? = "0",
    var draw: String? = "0",
    var lose: String? = "0",
    var goalWin: String? = "0",
    var goalLose: String? = "0",
    var point: String? = "0",
)