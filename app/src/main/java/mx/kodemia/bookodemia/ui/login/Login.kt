package mx.kodemia.bookodemia.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import mx.kodemia.bookodemia.ui.home.Home
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.tools.*
import mx.kodemia.bookodemia.ui.registro.FormRegistro
import org.json.JSONObject

class Login : AppCompatActivity() {

    private var TAG = Login::class.qualifiedName
    private var parent_view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //deleteTokenPreference(applicationContext) //borrar esta linea ----------

        if (SharedPreferenceTools(applicationContext).validateSessionToken())
            launchActivity()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeComponents()

    }

    fun dialogNoInternet(){
        with(androidx.appcompat.app.AlertDialog.Builder(this)){
            setTitle(getString(R.string.error_dialog_title))
            setMessage(getString(R.string.error_connection))
            setPositiveButton(getString(R.string.error_dialog_out)) { dialog, with ->
                dialog.dismiss()
            }
            show()
        }
    }

    override fun onResume() {
        clearAllFields()
        super.onResume()
    }

    private fun clearAllFields() {
        tiet_correo.text?.clear()
        tiet_correo.clearFocus()
        tiet_password.text?.clear()
        tiet_password.clearFocus()
        til_correo.isErrorEnabled = false
        til_password.isErrorEnabled = false
    }

    private fun launchActivity() {
        startActivity(
            Intent(
                this,
                Home::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }

    fun initializeComponents() {
        parent_view = findViewById(android.R.id.content)

        textView_registro.setOnClickListener {
            startActivity(Intent(this, FormRegistro::class.java))
        }

        button_iniciar.setOnClickListener {
            if (verifyInternetConnection(applicationContext))
                startLogIn()
            else
                dialogNoInternet()
        }

        afterEmailTextErrorWatcher(applicationContext, til_correo)
        afterTextErrorWatcher(applicationContext, til_password)
    }

    private fun startLogIn() {
        if (checkEmptyOrErrorFields(applicationContext, til_correo, til_password)) {
            makeLoginRequest()
        } else {
            if (tiet_correo.text!!.isEmpty() && tiet_password.text!!.isEmpty())
                til_correo.requestFocus()
            else if (tiet_password.text!!.isEmpty())
                til_password.requestFocus()
            else
                til_correo.requestFocus()
        }
    }

    fun makeLoginRequest() {

        loadingComponents(pb_login, button_iniciar, true, "", false)

        val queue = Volley.newRequestQueue(applicationContext)
        val jsonObject = makeJsonBodyRequest()

        val request = JsonObjectRequest(
            Request.Method.POST,
            getString(R.string.url_servidor) + getString(R.string.api_login),
            jsonObject,
            { response ->
                Log.d(TAG, response.toString())
                SharedPreferenceTools(applicationContext).initSessionToken(response)
                launchActivityAfterRequest()
            },
            { error ->
                loadingComponents(pb_login, button_iniciar, false, getString(R.string.login), true)
                Log.e(TAG, error.toString())
                makeSnacks(
                    parent_view!!,
                    getString(R.string.error_dialog_body),
                    getColor(R.color.error)
                )
            })
        queue.add(request)
    }

    private fun makeJsonBodyRequest(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("email", tiet_correo.text)
        jsonObject.put("password", tiet_password.text)
        jsonObject.put("device_name", getString(R.string.device_name))

        return jsonObject
    }


    fun launchActivityAfterRequest() {
        val intent = Intent(this, Home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}