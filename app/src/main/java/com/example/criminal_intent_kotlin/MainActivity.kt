package com.example.criminal_intent_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),CrimeListFragment.Callbacks  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Добавления фрагмента в код activity выполняются явные вызовы во FragmentManager activity.
        // Доступ к менеджеру фрагментов осуществляется с помощью
        //свойства supportFragmentManager. Мы будем использовать supportFragmentManager,
        //так как используем Jetpack-библиотеку и класс AppCompatActivity.
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = CrimeListFragment.newInstance()
            //операцию add(...) и окружающий ее код. Этот код создает и закрепляет транзакцию
            //Функция FragmentManager.beginTransaction() создает и возвращает экземпляр FragmentTransaction.
            // Класс FragmentTransaction использует динамичный интерфейс: функции, настраивающие FragmentTransaction, возвращают
            //FragmentTransaction вместо Unit, что позволяет объединять их вызовы в цепочку.
            //Таким образом, выделенный код в приведенном выше листинге означает: «Создать новую транзакцию фрагмента,
            // включить в нее одну операцию add, а затем
            //закрепить».
            //Функция add(...) отвечает за основное содержание транзакции. Она получает два
            //параметра: идентификатор контейнерного представления и недавно созданный
            //объект CrimeFragment. Идентификатор контейнерного представления должен быть
            //вам знаком: это идентификатор ресурса элемента FrameLayout, определенного
            //в файле activity_crime.xml
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

    }
    override fun onCrimeSelected(crimeId: UUID) {
       // val fragment = CrimeFragment()
        val fragment = CrimeFragment.newInstance(crimeId)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
                //ользователи будут ожидать, что нажатие кнопки «Назад» на экране подробностей
            // преступления вернет их обратно к списку преступлений. Чтобы реализовать
            //это поведение, добавьте транзакцию замены в обратный стек.
            .addToBackStack(null)
            .commit()
    }

}