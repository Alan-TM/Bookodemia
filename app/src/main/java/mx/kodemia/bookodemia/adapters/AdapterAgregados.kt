package mx.kodemia.bookodemia.adapters

import android.content.Context
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import mx.kodemia.bookodemia.DetallesLibro
import mx.kodemia.bookodemia.MainActivity
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.models.Libro


class AdapterAgregados(val listLibros: MutableList<Libro>) : RecyclerView.Adapter<AdapterAgregados.LibroHolder>() {
    class LibroHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cv : MaterialCardView = view.findViewById(R.id.cardView_recien_agregados)
        val img : ImageView = view.findViewById(R.id.image_recycler_agregados_portada)
        val titulo: TextView = view.findViewById(R.id.text_recycler_agregados_titulo)
        val autor: TextView = view.findViewById(R.id.text_recycler_agregados_autor)
        val categoria: TextView = view.findViewById(R.id.text_recycler_agregados_categoria)
        val btn_detalles: Button = view.findViewById(R.id.button_recycler_home_detalles)

        fun setInfo(libro: Libro){
            Glide.with(view).load(libro.img).diskCacheStrategy(DiskCacheStrategy.NONE).into(img)
            titulo.text = libro.titulo
            autor.text = "by ${libro.autor}"
            categoria.text = libro.categoria

            btn_detalles.setOnClickListener {
                val fragmentDetallesLibro = DetallesLibro()
                val activity = view.context as AppCompatActivity

                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.innerConstraint_home, fragmentDetallesLibro)
                    .addToBackStack("book")
                    .commit()
            }

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