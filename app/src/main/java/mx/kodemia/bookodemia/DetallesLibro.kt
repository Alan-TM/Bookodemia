package mx.kodemia.bookodemia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_detalles_libro.*
import mx.kodemia.bookodemia.models.Book
import mx.kodemia.bookodemia.models.authors.AuthorsAll
import mx.kodemia.bookodemia.models.authors.AuthorsData
import mx.kodemia.bookodemia.models.categories.CategoriesAll
import mx.kodemia.bookodemia.models.categories.CategoriesData

class DetallesLibro : Fragment() {
    private lateinit var book: Book
    private lateinit var category: CategoriesAll
    private lateinit var author: AuthorsAll

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            book = it.getSerializable("book") as Book
            author = it.getSerializable("author") as AuthorsAll
            category = it.getSerializable("category") as CategoriesAll
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalles_libro, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String) =
            DetallesLibro().apply {
                arguments = Bundle().apply {
                    putSerializable("book", book)
                    putSerializable("author", author)
                    putSerializable("category", category)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()

    }

    fun initComponents(){
        arguments?.let{
            text_details_titulo.text = book.attributes.title
            text_details_autor.text = getString(R.string.by_author, author.data.attributes.name)
            text_details_categoria.text = category.data.attributes.name

            text_details_description.text = book.attributes.content

            button_details_detalles.setOnClickListener {
                text_details_description.text = getString(R.string.details_detalles, book.attributes.createdAt, book.attributes.updatedAt)
            }

            button_details_descripcion.setOnClickListener {
                text_details_description.text = book.attributes.content
            }

            text_details_nombre.text = author.data.attributes.name
            text_details_about_autor.text = getString(R.string.details_detalles, author.data.attributes.createdAt, author.data.attributes.updatedAt)
        }

        button_details_back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("book", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}