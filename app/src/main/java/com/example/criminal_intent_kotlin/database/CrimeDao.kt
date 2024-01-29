package com.example.criminal_intent_kotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.criminal_intent_kotlin.Crime
import java.util.*

//Аннотация @Dao сообщает Room, что CrimeDao — это один из ваших объектов доступа к данным.
// Когда вы прикрепляете CrimeDao к вашему классу базы данных,
//Room будет генерировать реализации функций, которые вы добавляете к этому
//интерфейсу.
@Dao
interface CrimeDao {

    //В этой главе мы сосредоточились на использовании функции межпоточной
    //связи LiveData для выполнения запросов к базе данных. Для начала откройте
    //файл CrimeDao.kt и измените возвращаемый тип у функций запроса, так как они
    //должны возвращать объект LiveData, который оборачивает оригинальный возвращаемый тип.
    @Query("SELECT * FROM crime")
//    fun getCrimes(): List<Crime>
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
//    fun getCrime(id: UUID): Crime?
    fun getCrime(id: UUID): LiveData<Crime?>

}