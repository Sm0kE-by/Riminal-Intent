package com.example.criminal_intent_kotlin.database

import androidx.room.TypeConverter
import java.util.*

//Чтобы научить Room преобразовывать типы данных, необходимо указать преобразователь типов.
// Преобразователь типа сообщает Room, как преобразовать
//Создание базы данных 263
//специальный тип в формат для хранения в базе данных. Вам понадобятся две
//функции, к которым мы добавим аннотации @TypeConverter для каждого типа:
//одна сообщает Room, как преобразовывать тип, чтобы сохранить его в базе данных,
// а другая — как выполнить обратное преобразование.
class CrimeTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

}