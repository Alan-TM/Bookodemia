package mx.kodemia.bookodemia.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import mx.kodemia.bookodemia.ui.book.DetallesLibro
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.models.books.Book

class AdapterPopulares(val listLibros: MutableList<Book>):
    RecyclerView.Adapter<AdapterPopulares.LibroHolder>() {

    var bundle = Bundle()

    class LibroHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val AUTHOR = "author"
        private val CATEGORY = "category"

        val cl : ConstraintLayout = view.findViewById(R.id.cl_populares)
        val img: ImageView = view.findViewById(R.id.imagen_libro_populares)

        fun setImagen(libro: Book, position: Int, bundle: Bundle){
            //Glide.with(view).load(libro.img).diskCacheStrategy(DiskCacheStrategy.NONE).into(img)

            with(img){
                if(position % 2 == 0)
                    setImageResource(R.drawable.libro_2)
                else
                    setImageResource(R.drawable.libro_1)
            }

            /*runBlocking(Dispatchers.IO) {
                getCategoriesOrAuthorsByRequest(libro.relationships.authors.links.related, AUTHOR, bundle)
                getCategoriesOrAuthorsByRequest(libro.relationships.categories.links.related, CATEGORY, bundle)
            }*/



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LibroHolder(layoutInflater.inflate(R.layout.layout_mas_populares, parent, false))
    }

    override fun onBindViewHolder(holder: LibroHolder, position: Int) {
        holder.setImagen(listLibros[position], position, bundle)
        holder.img.setOnClickListener { v ->

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

    override fun getItemCount(): Int = 5


}