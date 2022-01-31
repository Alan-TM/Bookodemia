package mx.kodemia.bookodemia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.adapters.AdapterAgregados
import mx.kodemia.bookodemia.adapters.AdapterCategorias
import mx.kodemia.bookodemia.adapters.AdapterPopulares
import mx.kodemia.bookodemia.models.Book
import mx.kodemia.bookodemia.models.Data
import mx.kodemia.bookodemia.models.Libro
import mx.kodemia.bookodemia.models.User
import mx.kodemia.bookodemia.models.authors.AuthorsAll
import mx.kodemia.bookodemia.models.categories.AllCategories
import mx.kodemia.bookodemia.models.categories.CategoriesAll
import mx.kodemia.bookodemia.models.categories.CategoriesBook
import mx.kodemia.bookodemia.models.categories.CategoriesData
import mx.kodemia.bookodemia.tools.deleteTokenPreference
import mx.kodemia.bookodemia.tools.getPreferenceTokenSession
import mx.kodemia.bookodemia.tools.makeSnacks
import mx.kodemia.bookodemia.tools.verifyInternetConnection
import org.json.JSONObject

class Home : AppCompatActivity() {

    private var parent_view: View? = null
    private var TAG = Home::class.qualifiedName
    private var objectsMap = HashMap<String, JSONObject>()

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
                getCategoriesByRequest()
            }
        } else {
            pb_home.visibility = View.GONE
            dialogNoInternet()
        }

    }

    fun dialogNoInternet() {
        with(AlertDialog.Builder(this)) {
            setTitle(getString(R.string.error_dialog_title))
            setMessage(getString(R.string.error_connection))
            setPositiveButton(getString(R.string.error_dialog_out), { dialog, with ->
                deleteTokenPreference(applicationContext)
                launchLogin()
            })
            show()
        }
    }

    private fun launchLogin() {
        startActivity(
            Intent(
                this,
                MainActivity::class.java
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
                    val bundle = Bundle()
                    bundle.putSerializable(
                        "usuario",
                        Json.decodeFromString<User>(objectsMap["usuario"].toString())
                    )
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
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().replace(R.id.innerConstraint_home, fragment)
            .addToBackStack(backStack).commit()
    }

    private fun initRecyclerAgregados(listBooks: MutableList<Book>) {
        val adapterAgregados = AdapterAgregados(listBooks)
        recycler_home_agregados.layoutManager = LinearLayoutManager(this)
        recycler_home_agregados.adapter = adapterAgregados
        recycler_home_agregados.setHasFixedSize(true)
    }

    private fun initRecyclerCategorias(listCategorias: MutableList<CategoriesData>) {
        val adapterCategorias = AdapterCategorias(listCategorias)
        recycler_home_agregados.layoutManager = LinearLayoutManager(this)
        recycler_home_agregados.adapter = adapterCategorias
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
                objectsMap["usuario"] = response
                val r = Json.decodeFromString<User>(response.toString())
                text_home_nombre.text = r.name
            },
            { error ->
                Log.e(TAG, error.toString())
                if (error.networkResponse.statusCode == 429) {
                    makeSnacks(parent_view!!, "Error, algo ocurrió", getColor(R.color.error))
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${
                    getPreferenceTokenSession(
                        applicationContext,
                        getString(R.string.preference_token)
                    )
                }"
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

                    objectsMap["libros"] = response
                    val r = Json.decodeFromString<Data>(response.toString())

                    initRecyclerAgregados(r.data)
                    initCarouselPopulares(randomNumberHelper(r.data))
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
                    objectsMap["categorias"] = response
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

    private fun randomNumberHelper(listBooks: MutableList<Book>): MutableList<Book> {
        val mySet = mutableSetOf<Book>()

        while (mySet.size < 5) {
            mySet.add(listBooks[(0 until listBooks.size).random()])
        }
        return mySet.toMutableList()
    }
}