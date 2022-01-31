package mx.kodemia.bookodemia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.models.Book
import mx.kodemia.bookodemia.models.Categories
import mx.kodemia.bookodemia.models.categories.CategoriesData

class AdapterCategorias(val listCategorias: MutableList<CategoriesData>) :
    RecyclerView.Adapter<AdapterCategorias.CategoriasHolder>()  {
    class CategoriasHolder(val view: View): RecyclerView.ViewHolder(view) {
        val categoria: TextView = view.findViewById(R.id.text_categoria)

        fun setInfo(categoriaInfo: CategoriesData){
            categoria.text = categoriaInfo.attributes.name
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