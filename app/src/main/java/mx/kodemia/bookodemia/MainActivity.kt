package mx.kodemia.bookodemia

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import mx.kodemia.bookodemia.tools.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var TAG = MainActivity::class.qualifiedName
    private var parent_view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        deleteTokenPreference(applicationContext) //recuerda borrar esto-----------------------

        if(validateSessionToken(applicationContext))
            launchActivity()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeComponents()

    }

    private fun launchActivity() {
        startActivity(Intent(this, Home::class.java))
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
                makeSnacks(parent_view!!, getString(R.string.error_connection), getColor(R.color.error))

        }

        afterEmailTextErrorWatcher(applicationContext, tiet_correo, til_correo)
        afterTextErrorWatcher(applicationContext, tiet_password, til_password)
    }

    private fun startLogIn() {
        if (checkEmptyFields(applicationContext, til_correo, til_password)) {
            if (checkFieldsErrors(til_correo, til_password))
                makeLoginRequest()
            else
                til_correo.requestFocus()
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
        val jsonObject = JSONObject()
        jsonObject.put("email", tiet_correo.text)
        jsonObject.put("password", tiet_password.text)
        jsonObject.put("device_name", "Alan's phone")

        val request = JsonObjectRequest(
            Request.Method.POST,
            getString(R.string.url_servidor) + getString(R.string.api_login),
            jsonObject,
            { response ->
                Log.d(TAG, response.toString())
                initSessionToken(applicationContext, response)
                launchActivityAfterRequest()
            },
            { error ->
                loadingComponents(pb_login, button_iniciar,false, getString(R.string.login), true)
                Log.e(TAG, error.toString())
                makeSnacks(
                    parent_view!!,
                    getString(R.string.error_dialog_body),
                    getColor(R.color.error)
                )
            })
        queue.add(request)
    }



    fun launchActivityAfterRequest(){
        var intent = Intent(this, Home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}