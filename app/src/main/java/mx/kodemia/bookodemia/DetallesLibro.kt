package mx.kodemia.bookodemia

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_detalles_libro.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.models.Book
import mx.kodemia.bookodemia.models.authors.AuthorsAll
import mx.kodemia.bookodemia.models.authors.AuthorsData
import mx.kodemia.bookodemia.models.categories.CategoriesAll
import mx.kodemia.bookodemia.models.categories.CategoriesData
import mx.kodemia.bookodemia.tools.deleteTokenPreference
import mx.kodemia.bookodemia.tools.makeSnacks
import mx.kodemia.bookodemia.tools.verifyInternetConnection

class DetallesLibro : Fragment() {
    private lateinit var book: Book
    private lateinit var category: CategoriesAll
    private lateinit var author: AuthorsAll

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            book = it.getSerializable("book") as Book
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
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(verifyInternetConnection(view.context))
            initComponents(view)
        else{
            innerConstraint_detalles_libro.visibility = View.GONE
            dialogNoInternet(view)
        }
    }

    fun dialogNoInternet(view: View){
        with(AlertDialog.Builder(view.context)){
            setTitle(getString(R.string.error_dialog_title))
            setMessage(getString(R.string.error_connection))
            setPositiveButton(getString(R.string.error_dialog_out), { dialog, with->
                deleteTokenPreference(view.context)
                activity?.onBackPressed()
            })
            show()
        }
    }

    fun initComponents(view: View){
        arguments?.let{
            innerConstraint_detalles_libro.visibility = View.GONE
            text_details_titulo.text = book.attributes.title

            runBlocking {
                getCategoryByRequest(view.context, book.relationships.categories.links.related)
                getAuthorByRequest(view.context, book.relationships.authors.links.related)
            }

            innerConstraint_detalles_libro.visibility = View.VISIBLE

            text_details_description.text = book.attributes.content

            button_details_detalles.setOnClickListener {
                text_details_description.text = getString(R.string.details_detalles, book.attributes.createdAt, book.attributes.updatedAt)
            }
            button_details_descripcion.setOnClickListener {
                text_details_description.text = book.attributes.content
            }
        }
        button_details_back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("book", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun getAuthorByRequest(context: Context, url: String){
        val queue = Volley.newRequestQueue(context)

        val request = JsonObjectRequest(Request.Method.GET, url, null, {
            response->

            val author = Json.decodeFromString<AuthorsAll>(response.toString())

            text_details_autor.text = getString(R.string.by_author, author.data.attributes.name)
            text_details_nombre.text = author.data.attributes.name
            text_details_about_autor.text = getString(R.string.details_detalles, author.data.attributes.createdAt, author.data.attributes.updatedAt)

            constraintLayout_banner_details.visibility = View.VISIBLE
            linear_detalles_libro.visibility = View.VISIBLE
        }, {
            error->
            makeSnacks(requireView(), getString(R.string.error_something_happened), requireView().context.getColor(R.color.error))
        })

        queue.add(request)
    }

    fun getCategoryByRequest(context: Context, url: String){
        val queue = Volley.newRequestQueue(context)

        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response->

            val category = Json.decodeFromString<CategoriesAll>(response.toString())

            text_details_categoria.text = category.data.attributes.name
        }, {
                error->
            makeSnacks(requireView(), getString(R.string.error_something_happened), requireView().context.getColor(R.color.error))
        })

        queue.add(request)
    }
}