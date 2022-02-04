package mx.kodemia.bookodemia.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.kodemia.bookodemia.R

class AdapterGeneric(val listGeneric: MutableList<String>) :
    RecyclerView.Adapter<AdapterGeneric.CategoriasHolder>()  {
    class CategoriasHolder(val view: View): RecyclerView.ViewHolder(view) {

        val image_background: ImageView = view.findViewById(R.id.image_categorias_background)
        val text: TextView = view.findViewById(R.id.text_categoria)

        fun setInfo(info: String){
            text.text = info
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
        holder.setInfo(listGeneric[position])
    }

    override fun getItemCount(): Int = listGeneric.size

}