package mx.kodemia.bookodemia.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.DetallesLibro
import mx.kodemia.bookodemia.MainActivity
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.models.Book
import mx.kodemia.bookodemia.models.Libro
import mx.kodemia.bookodemia.models.authors.AuthorsAll
import mx.kodemia.bookodemia.models.categories.CategoriesAll
import mx.kodemia.bookodemia.models.categories.CategoriesData


class AdapterAgregados(val listLibros: MutableList<Book>) :
    RecyclerView.Adapter<AdapterAgregados.LibroHolder>() {

    class LibroHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val AUTOR = "author"
        private val CATEGORY = "category"

        var bundle = Bundle()

        val cv: MaterialCardView = view.findViewById(R.id.cardView_recien_agregados)
        val img: ImageView = view.findViewById(R.id.image_recycler_agregados_portada)
        val titulo: TextView = view.findViewById(R.id.text_recycler_agregados_titulo)
        val autor: TextView = view.findViewById(R.id.text_recycler_agregados_autor)
        val categoria: TextView = view.findViewById(R.id.text_recycler_agregados_categoria)
        val btn_detalles: Button = view.findViewById(R.id.button_recycler_home_detalles)

        fun setInfo(libro: Book) {
            //Glide.with(view).load(libro.img).diskCacheStrategy(DiskCacheStrategy.NONE).into(img)


            img.setImageResource(R.drawable.libro_1)
            titulo.text = libro.attributes.title
            getCategoriesOrAuthorsByRequest(libro.relationships.authors.links.related, autor, AUTOR)
            getCategoriesOrAuthorsByRequest(libro.relationships.categories.links.related, categoria, CATEGORY)

            btn_detalles.setOnClickListener {
                bundle.putSerializable("book", libro)
                val fragmentDetallesLibro = DetallesLibro()
                fragmentDetallesLibro.arguments = bundle
                val activity = view.context as AppCompatActivity

                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.innerConstraint_home, fragmentDetallesLibro)
                    .addToBackStack("book")
                    .commit()
            }

        }

        private fun getCategoriesOrAuthorsByRequest(url: String, textView: TextView, type: String) {
            val queue = Volley.newRequestQueue(view.context)

            val request = object : JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    if(type == CATEGORY) {
                        val r = Json.decodeFromString<CategoriesAll>(response.toString())
                        textView.text = r.data.attributes.name
                        bundle.putSerializable("category", r)
                    }
                    else{
                        val r = Json.decodeFromString<AuthorsAll>(response.toString())
                        textView.text = view.context.getString(R.string.by_author, r.data.attributes.name)
                        bundle.putSerializable("author", r)
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
        /*holder.view.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val activity = v!!.context as AppCompatActivity
                val fragmentDetallesLibro = DetallesLibro()

                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.innerConstraint_home, fragmentDetallesLibro)
                    .addToBackStack(null)
                    .commit()
            }


        })*/
    }

    override fun getItemCount(): Int = listLibros.size
}