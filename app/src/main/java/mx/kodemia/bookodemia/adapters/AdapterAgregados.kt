package mx.kodemia.bookodemia.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.card.MaterialCardView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.ui.book.DetallesLibro
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.models.books.Book
import mx.kodemia.bookodemia.models.authors.SingleAuthor
import mx.kodemia.bookodemia.models.categories.SingleCateogory


class AdapterAgregados(val listLibros: MutableList<Book>) :
    RecyclerView.Adapter<AdapterAgregados.LibroHolder>() {

    var bundle = Bundle()

    class LibroHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val AUTOR = "author"
        private val CATEGORY = "category"

        val cv: MaterialCardView = view.findViewById(R.id.cardView_recien_agregados)
        val img: ImageView = view.findViewById(R.id.image_recycler_agregados_portada)
        val titulo: TextView = view.findViewById(R.id.text_recycler_agregados_titulo)
        val autor: TextView = view.findViewById(R.id.text_recycler_agregados_autor)
        val categoria: TextView = view.findViewById(R.id.text_recycler_agregados_categoria)
        val btn_detalles: Button = view.findViewById(R.id.button_recycler_home_detalles)

        private fun getRequests(libro: Book){
            getCategoriesOrAuthorsByRequest(libro.relationships.authors.links.related, autor, AUTOR)
            getCategoriesOrAuthorsByRequest(libro.relationships.categories.links.related, categoria, CATEGORY)
        }

        fun setInfo(libro: Book) {
            getRequests(libro)
            img.setImageResource(R.drawable.libro_1)
            titulo.text = libro.attributes.title
        }

        private fun getCategoriesOrAuthorsByRequest(url: String, textView: TextView, type: String) {
            val queue = Volley.newRequestQueue(view.context)

            val request = object : JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    if(type == CATEGORY) {
                        val r = Json.decodeFromString<SingleCateogory>(response.toString())
                        textView.text = r.data.attributes.name
                    }
                    else{
                        val r = Json.decodeFromString<SingleAuthor>(response.toString())
                        textView.text = view.context.getString(R.string.by_author, r.data.attributes.name)
                    }
                },
                { error ->
                    Log.e("Recycler", error.toString())
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            queue.add(request)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LibroHolder(layoutInflater.inflate(R.layout.layout_recien_agregados, parent, false))
    }

    override fun onBindViewHolder(holder: LibroHolder, position: Int) {
        holder.setInfo(listLibros[position])
        holder.btn_detalles.setOnClickListener {
                bundle.putSerializable("book", listLibros[position])
                val activity = holder.view.context as AppCompatActivity
                val fragmentDetallesLibro = DetallesLibro()
                fragmentDetallesLibro.arguments = bundle

                activity.supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.innerConstraint_home, fragmentDetallesLibro)
                    .addToBackStack("book")
                    .commit()
        }
    }

    override fun getItemCount(): Int = listLibros.size
}