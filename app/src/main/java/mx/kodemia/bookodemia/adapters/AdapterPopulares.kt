package mx.kodemia.bookodemia.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import mx.kodemia.bookodemia.DetallesLibro
import mx.kodemia.bookodemia.MainActivity
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.models.Libro

class AdapterPopulares(val listLibros: MutableList<Libro>):
    RecyclerView.Adapter<AdapterPopulares.LibroHolder>() {
    class LibroHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cl : ConstraintLayout = view.findViewById(R.id.cl_populares)
        val img: ImageView = view.findViewById(R.id.imagen_libro_populares)

        fun setImagen(libro: Libro){
            Glide.with(view).load(libro.img).diskCacheStrategy(DiskCacheStrategy.NONE).into(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LibroHolder(layoutInflater.inflate(R.layout.layout_mas_populares, parent, false))
    }

    override fun onBindViewHolder(holder: LibroHolder, position: Int) {
        holder.setImagen(listLibros[position])
        holder.img.setOnClickListener { v ->
            val activity = v!!.context as AppCompatActivity
            val fragmentDetallesLibro = DetallesLibro()

            activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.innerConstraint_home, fragmentDetallesLibro)
                .addToBackStack("book")
                .commit()
        }
    }

    override fun getItemCount(): Int = listLibros.size


}