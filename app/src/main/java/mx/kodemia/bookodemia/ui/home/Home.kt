package mx.kodemia.bookodemia.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.ui.user.DetallesUsuario
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.ui.favorites.UserFavorites
import mx.kodemia.bookodemia.adapters.AdapterAgregados
import mx.kodemia.bookodemia.adapters.AdapterGeneric
import mx.kodemia.bookodemia.adapters.AdapterPopulares
import mx.kodemia.bookodemia.models.authors.AllAuthors
import mx.kodemia.bookodemia.models.authors.AuthorsData
import mx.kodemia.bookodemia.models.books.Book
import mx.kodemia.bookodemia.models.books.AllBooks
import mx.kodemia.bookodemia.models.user.User
import mx.kodemia.bookodemia.models.categories.AllCategories
import mx.kodemia.bookodemia.models.categories.CategoriesData
import mx.kodemia.bookodemia.tools.*
import mx.kodemia.bookodemia.ui.login.Login

class Home : AppCompatActivity() {

    private var parent_view: View? = null
    private var TAG = Home::class.qualifiedName
    val bundle = Bundle()
    var listCategory = ArrayList<String>()
    var listAuthors = ArrayList<String>()
    var listBooks = ArrayList<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initAllComponents()
    }

    fun initAllComponents() {
        parent_view = findViewById(android.R.id.content)

        if (verifyInternetConnection(applicationContext)) {

            runBlocking(Dispatchers.IO) {
                getUserInfoByRequest()
                getBooksInfoByRequest()
                getAuthorsByRequest()
                getCategoriesByRequest()
            }
        } else {
            pb_home.visibility = View.GONE
            dialogNoInternet()
        }

        button_home_categoria.setOnClickListener {
            val adapterCategories = AdapterGeneric(listCategory)
            recycler_home_agregados.adapter = adapterCategories
        }

        button_home_libros.setOnClickListener {
            val adapterBooks = AdapterAgregados(listBooks)
            recycler_home_agregados.adapter = adapterBooks
        }

        button_home_autores.setOnClickListener {
            val adapterAutores = AdapterGeneric(listAuthors)
            recycler_home_agregados.adapter = adapterAutores
        }

    }

    fun dialogNoInternet() {
        with(AlertDialog.Builder(this)) {
            setTitle(getString(R.string.error_dialog_title))
            setMessage(getString(R.string.error_connection))
            setPositiveButton(getString(R.string.error_dialog_out)) { dialog, with ->
                SharedPreferenceTools(applicationContext).deleteTokenPreference()
                launchLogin()
            }
            show()
        }
    }

    private fun launchLogin() {
        startActivity(
            Intent(
                this,
                Login::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }

    override fun onStart() {
        super.onStart()
        initBottomNavigationView()
    }

    fun initBottomNavigationView() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.popBackStack()
                    true
                }
                R.id.nav_list -> {
                    transitionToFragment(UserFavorites(), "favorites")
                    true
                }
                R.id.nav_user -> {
                    val fragmentUser = DetallesUsuario()
                    fragmentUser.arguments = bundle
                    transitionToFragment(fragmentUser, "user")
                    true
                }
                else -> false
            }
        }
    }

    fun transitionToFragment(fragment: Fragment, backStack: String) {
        //supportFragmentManager.popBackStack()
        if(!supportFragmentManager.popBackStackImmediate())
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_fragment, FragmentTransaction.TRANSIT_FRAGMENT_FADE, FragmentTransaction.TRANSIT_FRAGMENT_FADE,
                R.anim.slide_out_fragment
            )
            .replace(R.id.innerConstraint_home, fragment)
            .addToBackStack(backStack)
            .commit()

    }

    private fun initRecyclerAgregados(listBooks: MutableList<Book>) {
        val adapterAgregados = AdapterAgregados(listBooks)
        recycler_home_agregados.layoutManager = LinearLayoutManager(this)
        recycler_home_agregados.adapter = adapterAgregados
        recycler_home_agregados.setHasFixedSize(true)
    }

    private fun initRecyclerCategorias(listCategorias: MutableList<CategoriesData>) {
        val adapterCategorias = AdapterGeneric(listCategory)
        recycler_home_agregados.layoutManager = LinearLayoutManager(this)
        recycler_home_agregados.adapter = adapterCategorias
        recycler_home_agregados.setHasFixedSize(true)
    }

    private fun initRecyclerAutores(listAutores: MutableList<AuthorsData>){
        val adapterAutores = AdapterGeneric(listAuthors)
        recycler_home_agregados.layoutManager = LinearLayoutManager(this)
        recycler_home_agregados.adapter = adapterAutores
        recycler_home_agregados.setHasFixedSize(true)
    }

    private fun initCarouselPopulares(listBooks: MutableList<Book>) {
        val adapterPopulares = AdapterPopulares(listBooks)
        recycler_populares.adapter = adapterPopulares
        recycler_populares.setInfinite(true)
        recycler_populares.setIntervalRatio(0.6f)
    }

    private fun getUserInfoByRequest() {
        val queue = Volley.newRequestQueue(applicationContext)

        val request = object : JsonObjectRequest(
            Request.Method.GET,
            getString(R.string.url_servidor) + getString(R.string.api_user),
            null,
            { response ->
                Log.d(TAG, response.toString())
                val r = Json.decodeFromString<User>(response.toString())
                bundle.putSerializable("usuario", r)
                text_home_nombre.text = r.name
            },
            { error ->
                Log.e(TAG, error.toString())
                if (error.networkResponse.statusCode == 429) {
                    makeSnacks(parent_view!!, getString(R.string.error_many_request), getColor(R.color.error))
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${SharedPreferenceTools(applicationContext).getPreferenceTokenSession(getString(R.string.preference_token))}"
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        queue.add(request)
    }

    fun getBooksInfoByRequest() {
        val queue = Volley.newRequestQueue(applicationContext)
        val request =
            JsonObjectRequest(getString(R.string.url_servidor) + getString(R.string.api_books),
                null, { response ->
                    val r = Json.decodeFromString<AllBooks>(response.toString())
                    for(b in r.data){
                        listBooks.add(b)
                    }
                    initRecyclerAgregados(listBooks)
                    initCarouselPopulares(randomBookHelper(r.data))
                }, { error ->
                    Log.e(TAG, error.toString())
                    if (error.networkResponse.statusCode == 429) {
                        makeSnacks(parent_view!!, "Error, algo ocurrió", getColor(R.color.error))
                    }
                })

        queue.add(request)
    }

    fun getCategoriesByRequest() {
        val queue = Volley.newRequestQueue(applicationContext)
        val request =
            JsonObjectRequest(getString(R.string.url_servidor) + getString(R.string.api_categories),
                null, { response ->
                    val r = Json.decodeFromString<AllCategories>(response.toString())

                    for(c in r.data){
                        listCategory.add(c.attributes.name)
                    }

                    Log.d(TAG, response.toString())
                    pb_home.visibility = View.GONE
                    innerConstraint_home.visibility = View.VISIBLE
                }, { error ->
                    if (error.networkResponse.statusCode == 429)
                        makeSnacks(parent_view!!, "Error", getColor(R.color.error))
                    Log.e(TAG, error.toString())
                })
        queue.add(request)
    }

    fun getAuthorsByRequest() {
        val queue = Volley.newRequestQueue(applicationContext)
        val request =
            JsonObjectRequest(getString(R.string.url_servidor) + getString(R.string.api_authors),
                null, { response ->
                    val r = Json.decodeFromString<AllAuthors>(response.toString())

                    for(c in r.data){
                        listAuthors.add(c.attributes.name)
                    }

                    Log.d(TAG, response.toString())
                }, { error ->
                    if (error.networkResponse.statusCode == 429)
                        makeSnacks(parent_view!!, "Error", getColor(R.color.error))
                    Log.e(TAG, error.toString())
                })
        queue.add(request)
    }

    private fun randomBookHelper(listBooks: MutableList<Book>): MutableList<Book> {
        val mySet = mutableSetOf<Book>()

        while (mySet.size < 5) {
            mySet.add(listBooks[(0 until listBooks.size).random()])
        }
        return mySet.toMutableList()
    }
}
