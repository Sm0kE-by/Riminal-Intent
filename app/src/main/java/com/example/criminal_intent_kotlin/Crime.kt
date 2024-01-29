package com.example.criminal_intent_kotlin

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Crime(
    //UUID — вспомогательный класс Java, входящий в инфраструктуру Android, —
    //предоставляет простой способ генерирования универсально-уникальных идентификаторов.
    // В конструкторе такой идентификатор генерируется вызовом UUID.
    //randomUUID().
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false
) {
}