package mx.kodemia.bookodemia.models

import mx.kodemia.bookodemia.R

data class Libro(val img: Int = R.drawable.libro_1,
                 val titulo: String = "Título",
                 val autor: String = "Autor",
                 val categoria: String = "Categoría"
                 )
