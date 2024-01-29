package com.example.criminal_intent_kotlin

import androidx.lifecycle.ViewModel
import com.example.criminal_intent_kotlin.database.CrimeRepository

class CrimeListViewModel : ViewModel(){

//    val crimes = mutableListOf<Crime>()
//    init {
//        //заполнение листа преступлений
//        for (i in 0 until 100) {
//            val crime = Crime()
//            crime.title = "Crime #$i"
//            crime.isSolved = i % 2 == 0
//            crimes += crime
//        }
//    }

    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()

}