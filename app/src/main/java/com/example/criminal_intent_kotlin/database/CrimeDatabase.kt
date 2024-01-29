package com.example.criminal_intent_kotlin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.criminal_intent_kotlin.Crime

//Аннотация @Database сообщает Room о том, что этот класс представляет собой
//базу данных в приложении. Самой аннотации требуется два параметра. Первый
//параметр — это список классов-сущностей, который сообщает Room, какие использовать
// классы при создании и управлении таблицами для этой базы данных
//Второй параметр — версия базы данных. При первом создании базы данных
//версия должна быть равна 1.
@Database(entities = [Crime::class], version = 1)
//Добавив аннотацию @TypeConverters и передав класс CrimeTypeConverters, вы
//инструктируете базу данных использовать функции в этом классе при преобразовании типов.
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {

    //Теперь нам нужно связать класс DAO с классом базы данных. Поскольку CrimeDao
//представляет собой интерфейс, Room будет сам генерировать конкретную версию
//класса. Но для этого вы должны приказать классу базы данных создать экземпляр DAO.
    abstract fun crimeDao(): CrimeDao

}