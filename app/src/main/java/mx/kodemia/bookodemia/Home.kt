package mx.kodemia.bookodemia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import mx.kodemia.bookodemia.adapters.AdapterAgregados
import mx.kodemia.bookodemia.adapters.AdapterPopulares
import mx.kodemia.bookodemia.models.Libro

class Home : AppCompatActivity() {

    val listLibros: MutableList<Libro> = mutableListOf()
    var adapterPopulares = AdapterPopulares(listLibros)
    var adapterAgregados = AdapterAgregados(listLibros)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        addLibros()
        initCarouselPopulares()
        initRecyclerAgregados()
    }

    private fun initRecyclerAgregados() {
        val myLinearLayoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        recycler_home_agregados.layoutManager = myLinearLayoutManager
        recycler_home_agregados.adapter = adapterAgregados
        recycler_home_agregados.setHasFixedSize(true)

    }

    private fun initCarouselPopulares() {
        recycler_populares.adapter = adapterPopulares
        recycler_populares.setInfinite(true)
        recycler_populares.setIntervalRatio(0.6f)
    }

    private fun addLibros(){
        listLibros.add(Libro(R.drawable.libro_1, "El principito", "Desconocido", "Fantasia"))
        listLibros.add(Libro(R.drawable.libro_2, "La metamorfosis", "Franz Kafka", "Metafora"))
        listLibros.add(Libro())
        listLibros.add(Libro())
        listLibros.add(Libro(R.drawable.libro_2))
    }
}