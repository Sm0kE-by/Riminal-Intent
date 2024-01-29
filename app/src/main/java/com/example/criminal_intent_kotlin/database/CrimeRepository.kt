package com.example.criminal_intent_kotlin.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminal_intent_kotlin.Crime
import java.util.*

//Класс репозитория инкапсулирует логику для доступа к данным из одного источника или совокупности источников.
// Он определяет, как захватывать и хранить определенный набор данных — локально, в базе данных или с удаленного
//сервера. Ваш код UI будет запрашивать все данные из репозитория, потому что
//интерфейсу неважно, как фактически хранятся или извлекаются данные. Это
//детали реализации самого репозитория.

//Вы также можете пометить
//конструктор как приватный, чтобы убедиться в отсутствии компонентов, которые
//могут пойти против системы и создать собственный экземпляр.

//Теперь нужно добавить два свойства классу CrimeRepository, чтобы он умел хранить ссылки на базу данных и объекты DAO.

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {

    //Функция Room.databaseBuilder() создает конкретную реализацию вашего абстрактного класса
    // CrimeDatabase с использованием трех параметров. Сначала
    //ему нужен объект Context, так как база данных обращается к файловой системе.
    //Контекст приложения нужно передавать, так как синглтон, скорее всего, существует дольше,
    // чем любой из ваших классов activity.
    //Второй параметр — это класс базы данных, которую Room должен создать.
    //Третий — имя файла базы данных, которую создаст Room. Нужно использовать
    //приватную константу, определенную в том же файле, поскольку никакие другие
    //компоненты не должны получать к ней доступ
    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val crimeDao = database.crimeDao()

    //Затем нужно заполнить ваш CrimeRepository, чтобы остальные компоненты могли
    //выполнять любые операции, необходимые для работы с базой данных. Добавьте
    //функцию в репозиторий для каждой функции в вашем DAO.

    //Кроме того, CrimeRepository должен возвращать объект LiveData из своих функций запроса.
    //    fun getCrimes(): List<Crime> = crimeDao.getCrimes()
    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    //    fun getCrime(id: UUID): Crime? = crimeDao.getCrime(id)
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    //CrimeRepository — это одноэлементный класс (синглтон). Это означает, что
    //в вашем процессе приложения единовременно существует только один его
    //экземпляр.
    //Синглтон существует до тех пор, пока приложение находится в памяти, поэтому
    //хранение в нем любых свойств позволяет получить к ним доступ в течение жизненного
    // цикла вашей activity и фрагмента. Будьте осторожны с синглтонами, так
    //как они уничтожаются, когда Android удаляет приложение из памяти
    companion object {
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}