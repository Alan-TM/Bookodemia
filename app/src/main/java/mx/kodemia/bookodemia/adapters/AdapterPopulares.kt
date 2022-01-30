package mx.kodemia.bookodemia.adapters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.DetallesLibro
import mx.kodemia.bookodemia.MainActivity
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.models.Book
import mx.kodemia.bookodemia.models.Libro
import mx.kodemia.bookodemia.models.authors.AuthorsAll
import mx.kodemia.bookodemia.models.categories.CategoriesAll

class AdapterPopulares(val listLibros: MutableList<Book>):
    RecyclerView.Adapter<AdapterPopulares.LibroHolder>() {
    class LibroHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val AUTHOR = "author"
        private val CATEGORY = "category"

        var bundle = Bundle()

        val cl : ConstraintLayout = view.findViewById(R.id.cl_populares)
        val img: ImageView = view.findViewById(R.id.imagen_libro_populares)

        fun setImagen(libro: Book, position: Int){
            //Glide.with(view).load(libro.img).diskCacheStrategy(DiskCacheStrategy.NONE).into(img)

            with(img){
                if(position % 2 == 0)
                    setImageResource(R.drawable.libro_2)
                else
                    setImageResource(R.drawable.libro_1)
            }

            getCategoriesOrAuthorsByRequest(libro.relationships.authors.links.related, AUTHOR)
            getCategoriesOrAuthorsByRequest(libro.relationships.categories.links.related, CATEGORY)

            img.setOnClickListener { v ->
                bundle.putSerializable("book", libro)
                val activity = v!!.context as AppCompatActivity
                val fragmentDetallesLibro = DetallesLibro()
                fragmentDetallesLibro.arguments = bundle

                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.innerConstraint_home, fragmentDetallesLibro)
                    .addToBackStack("book")
                    .commit()
            }
        }

        private fun getCategoriesOrAuthorsByRequest(url: String, type: String) {
            val queue = Volley.newRequestQueue(view.context)

            val request = object : JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    if(type == CATEGORY) {
                        val r = Json.decodeFromString<CategoriesAll>(response.toString())
                        bundle.putSerializable("category", r)
                    }
                    else{
                        val r = Json.decodeFromString<AuthorsAll>(response.toString())
                        bundle.putSerializable("author", r)
                    }
                },
                { error ->
                    Log.e("RecyclerPop", error.toString())
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
        return LibroHolder(layoutInflater.inflate(R.layout.layout_mas_populares, parent, false))
    }

    override fun onBindViewHolder(holder: LibroHolder, position: Int) {
        holder.setImagen(listLibros[position], position)
    }

    override fun getItemCount(): Int = 5


}