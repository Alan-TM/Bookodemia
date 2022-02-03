package mx.kodemia.bookodemia.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.models.Book
import mx.kodemia.bookodemia.models.Categories
import mx.kodemia.bookodemia.models.categories.CategoriesData

class AdapterCategorias(val listCategorias: MutableList<String>) :
    RecyclerView.Adapter<AdapterCategorias.CategoriasHolder>()  {
    class CategoriasHolder(val view: View): RecyclerView.ViewHolder(view) {

        val image_background: ImageView = view.findViewById(R.id.image_categorias_background)
        val categoria: TextView = view.findViewById(R.id.text_categoria)

        fun setInfo(categoriaInfo: String){
            categoria.text = categoriaInfo
            val currentColor = Color.argb(100, (0..256).random(), (0..256).random(), (0..256).random());
            image_background.setBackgroundColor(currentColor)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriasHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CategoriasHolder(layoutInflater.inflate(R.layout.layout_categorias, parent, false))
    }

    override fun onBindViewHolder(holder: CategoriasHolder, position: Int) {
        holder.setInfo(listCategorias[position])
    }

    override fun getItemCount(): Int = listCategorias.size

}