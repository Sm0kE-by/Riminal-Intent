package com.example.criminal_intent_kotlin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.util.*

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"

class CrimeFragment : Fragment() {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
        //В функции CrimeFragment.onCreate(...) нужно извлечь UUID из аргументов
        //фрагмента. Запишем идентификатор в журнал, чтобы убедиться, что аргумент
        //прикрепился правильно.
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        Log.d(TAG, "args bundle crime ID: $crimeId")
        // Загрузка преступления из базы данных
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //В функции onCreateView(...) мы явно заполняем представление фрагмента, вызывая LayoutInflater.inflate(...)
        // с передачей идентификатора ресурса макета.
        //Второй параметр определяет родителя представления, что обычно необходимо
        //для правильной настройки виджета. Третий параметр указывает, нужно ли
        //включать заполненное представление в родителя. Мы передаем false, потому что
        //представление будет добавлено в контейнере activity. Представление фрагмента
        //не нужно сразу добавлять в родительское представление — activity обработает
        //этот момент позже
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox

        dateButton.apply {
            //Блокировка кнопки гарантирует, что кнопка никак не среагирует на нажатие.
            //Кроме того, внешний вид кнопки изменяется в заблокированном состоянии.
            text = crime.date.toString()
            isEnabled = false
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        //Назначение слушателей во фрагменте работает точно так же, как в activity.
        //мы создаем анонимный класс, который реализует интерфейс
        //слушателя TextWatcher. Этот интерфейс содержит три функции, но нас интересует
        //только одна: onTextChanged(...).

        val titleWatcher = object : TextWatcher {

            //Обратите внимание, что слушатель TextWatcher настраивается в функции onStart().
            //Некоторые слушатели срабатывают не только при взаимодействии с ними, но
            //и при восстановлении состояния виджета, например при повороте. Слушатели,
            // которые реагируют на ввод данных, такие как TextWatcher для EditText или
            //OnCheckChangedListener для CheckBox, тоже так работают

            //            Состояние виджета восстанавливается после функции onCreateView(...) и перед
//            функцией onStart(). При восстановлении состояния содержимое EditText будет
//            установлено на любое значение, которое в данный момент находится в заголовке
//            crime.title. В этот момент, если вы уже установили слушателя на EditText (например, в onCreate(...) или onCreateView(...)), будут выполняться функции
//            TextWatcher-а beforeTextChanged(...), onTextChanged(...) и afterTextChanged(...).
//            200  Глава 8. UI-фрагменты и FragmentManager
//            Установка слушателя в onStart() позволяет избежать такого поведения, так как
//            слушатель подключается после восстановления состояния виджета.
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                //В функции onTextChanged(...) мы вызываем toString() для объекта CharSequence,
                //представляющего ввод пользователя. Эта функция возвращает строку, которая
                //затем используется для задания заголовка Crime
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                TODO("Not yet implemented")
            }
        }
        titleField.addTextChangedListener(titleWatcher)
        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }
        }

    }

    //Включите в CrimeFragment функцию newInstance(UUID), который получает UUID,
    //создает пакет аргументов, создает экземпляр фрагмента, а затем присоединяет
    //аргументы к фрагменту.
    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }
}