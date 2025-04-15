package com.joris.Backgammon.dto

import org.bson.codecs.pojo.annotations.BsonId

data class User(
    @BsonId
    val userId : String,
    val email : String,
    val name : String,
    val surname : String,
    val userName: String,
    var password : String,
    val configs : List<userSettings>
)

enum class Themes {
    WHITE, BLACK
}

data class userSettings (
    val defaultDifficulty : DifficultyLevel,
    val defaultTheme : Themes
)

