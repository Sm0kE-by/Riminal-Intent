package com.example.criminal_intent_kotlin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

//private const val TAG = "MyLog"
private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    /**
     * Требуемый интерфейс
     */
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var crimeRecyclerView: RecyclerView
//    private var adapter: CrimeAdapter? = null
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }


    //Затем очистите CrimeListFragment, чтобы отразить тот факт, что CrimeListViewModel
    //сейчас открывает LiveData, возвращенный из хранилища (листинг 11.18). Удалите
    //реализацию OnCreate(...), так как она ссылается на CrimeListViewModel.crimes,
    // которого больше не существует. В updateUI() удалите ссылку на CrimeListViewModel.
    //crimes и добавьте параметр, позволяющий брать на вход список преступлений.
    //Наконец, удалите вызов updateUI() из onCreateView(...). Вызов updateUI() будет
    //выполняться из другого места.
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        //RecyclerView не отображает элементы на самом экране. Он передает эту задачу
        //объекту LayoutManager. LayoutManager располагает каждый элемент, а также определяет, как работает прокрутка.
        // Поэтому если RecyclerView пытается сделать
        //что-то подобное при наличии LayoutManager, он сломается.
        crimeRecyclerView =
            view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
       // updateUI()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Функция LiveData.observe(LifecycleOwner, Observer) используется для регистрации
        // наблюдателя за экземпляром LiveData и связи наблюдения с жизненным
        //циклом другого компонента.
        //Второй параметр функции observe(...) — это реализация Observer.
        // Этот объект отвечает за реакцию на новые данные из LiveData. В этом случае блок кода
        //наблюдателя выполняется всякий раз, когда обновляется список в LiveData.
        //Наблюдатель получает список преступлений из LiveData и печатает сообщение
        //журнала, если свойство не равно нулю
        //Если вы не снимали наблюдателя с прослушивания изменений в LiveData,
        // реализация наблюдателя может попытаться обновить элемент вашего фрагмента,
        //когда представление находится в нерабочем состоянии (например, когда элемент
        //уничтожается). И если вы пытаетесь обновить недопустимый элемент, ваше приложение может сломаться.
        //Здесь на сцену выходит параметр LifecycleOwner из LiveData.observe(...). Время
        //жизни наблюдателя длится столько же, сколько и у компонента, представленного
        //LifecycleOwner. В примере выше наблюдатель связан с фрагментом.
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            })
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    //Ф-ия настраивает интерфейс CrimeListFragment.
    //На данный момент она создает CrimeAdapter и устанавливает его на RecyclerView
 //   private fun updateUI() {
        private fun updateUI(crimes: List<Crime>) {
 //       val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    //RecyclerView ожидает, что элемент представления будет обернут в экземпляр
    //ViewHolder. ViewHolder хранит ссылку на представление элемента (а иногда и ссылки
    // на конкретные виджеты внутри этого представления).
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        //Так как каждый View имеет связанный с ним ViewHolder, вы можете создать OnClickListener
        //для всех View. И создадим ф-ию onClick
        init {
            itemView.setOnClickListener(this)
        }

        //В данный момент Adapter привязывает данные преступления непосредственно
        //к текстовым виджетам в функции Adapter.onBindViewHolder(...). Это работает
        //отлично, но лучше более четко разделить задачи между холдером и адаптером.
        //Адаптер должен знать как можно больше о внутренней кухне и данных холдера.
        //Мы рекомендуем поместить весь код, который будет выполнять привязку, внутрь
        //CrimeHolder.
        //Добавьте функцию bind(Crime) в CrimeHolder. В этой новой функции нужно
        //кэшировать привязываемые преступления в свойства и присвоить текстовые
        //значения свойствам titleTextView и dateTextView.
        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View) {

            callbacks?.onCrimeSelected(crime.id)
            //CrimeHolder сам по себе реализует интерфейс OnClickListener.
            //В окне itemView, которое представляет собой View для всей строки, CrimeHolder
            //устанавливается в качестве приемника событий нажатия.
        }
    }

    // Класс RecyclerView не создает ViewHolder
//сам по себе. Вместо этого используется адаптер. Адаптер представляет собой
//объект контроллера, который находится между RecyclerView и наборами данных,
//которые отображает RecyclerView.
    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {

        //Функция Adapter.onCreateViewHolder(...) отвечает за создание представления
        //на дисплее, оборачивает его в холдер и возвращает результат. В этом случае вы
        //наполняете list_item_view.xml и передаете полученное представление в новый
        //экземпляр CrimeHolder.
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        //Когда утилизатору нужно знать, сколько элементов в наборе данных поддерживают его
        // (например, когда он впервые создается), он будет просить свой адаптер
        //вызвать Adapter.getItemCount(). Функция getItemCount()возвращает количество
        //элементов в списке преступлений, отвечая на запрос утилизатора
        override fun getItemCount(): Int = crimes.size

        //Функция Adapter.onBindViewHolder(holder: CrimeHolder, position: Int) отвечает за заполнение
        // данного холдера holder преступлением из данной позиции position.
        // В этом случае преступления из списка преступлений окажутся в нужной позиции.
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}