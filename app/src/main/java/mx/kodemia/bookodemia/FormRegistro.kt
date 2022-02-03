package mx.kodemia.bookodemia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_form_registro.*
import kotlinx.android.synthetic.main.activity_main.*
import mx.kodemia.bookodemia.tools.*
import org.json.JSONObject

class FormRegistro : AppCompatActivity() {

    private var parent_view: View? = null
    private var TAG = FormRegistro::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_registro)

        initializeComponents()

    }

    fun initializeComponents() {
        parent_view = findViewById(android.R.id.content)

        afterTextErrorWatcher(applicationContext, til_registro_user)
        afterEmailTextErrorWatcher(applicationContext, til_registro_correo)
        errorMatchinPasswords(
            tiet_registro_password,
            til_registro_password,
            til_registro_confPassword
        )
        errorMatchinPasswords(
            tiet_registro_confPassword,
            til_registro_confPassword,
            til_registro_password
        )

        button_regresar_main.setOnClickListener {
            finish()
        }

        button_registro_crear.setOnClickListener {
            if (verifyInternetConnection(applicationContext))
                makeRegisterRequest()
            else
                dialogNoInternet()
        }
    }

    fun dialogNoInternet(){
        with(AlertDialog.Builder(this)){
            setTitle(getString(R.string.error_dialog_title))
            setMessage(getString(R.string.error_connection))
            setPositiveButton(getString(R.string.error_dialog_out), { dialog, with->
                deleteTokenPreference(applicationContext)
                launchLogin()
            })
            show()
        }
    }

    private fun launchLogin() {
        startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        finish()
    }

    private fun makeRegisterRequest() {

        if (checkEmptyOrErrorFields(
                applicationContext,
                til_registro_user,
                til_registro_correo,
                til_registro_password,
                til_registro_confPassword
            )
        ) {

            loadingComponents(pb_registro, button_registro_crear, true, "", false)
            button_regresar_main.isEnabled = false

            val queue = Volley.newRequestQueue(applicationContext)
            val jsonObject = makeJsonBodyRequest()

            val request = object : JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.url_servidor) + getString(R.string.api_register),
                jsonObject,
                { response ->
                    Log.d(TAG, response.toString())

                    initSessionToken(applicationContext, response)

                    makeSnacks(
                        parent_view!!,
                        getString(R.string.success_register),
                        getColor(R.color.blue_dark)
                    )

                    loadingComponents(
                        pb_registro,
                        button_registro_crear,
                        false,
                        getString(R.string.create_account),
                        true
                    )
                    button_regresar_main.isEnabled = true
                    launchActivityAfterRequest()

                },
                { error ->
                    Log.e(TAG, error.toString())
                    if (error.networkResponse.statusCode == 422)
                        makeSnacks(
                            parent_view!!,
                            getString(R.string.error_email_taken),
                            getColor(R.color.error)
                        )
                    loadingComponents(
                        pb_registro,
                        button_registro_crear,
                        false,
                        getString(R.string.create_account),
                        true
                    )
                    button_regresar_main.isEnabled = true
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            queue.add(request)
        }
    }

    private fun launchActivityAfterRequest() {
        startActivity(
            Intent(
                this,
                Home::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }

    private fun makeJsonBodyRequest(): JSONObject {
        val jsonRegister = JSONObject()
        jsonRegister.put("name", tiet_registro_user.text)
        jsonRegister.put("email", tiet_registro_correo.text)
        jsonRegister.put("password", tiet_registro_password.text)
        jsonRegister.put("password_confirmation", tiet_registro_confPassword.text)
        jsonRegister.put("device_name", getString(R.string.device_name))

        return jsonRegister
    }

    private fun errorMatchinPasswords(
        tiet: TextInputEditText,
        til: TextInputLayout,
        matchTil: TextInputLayout
    ) {
        tiet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editText: Editable?) {
                if (editText.toString().trim().isEmpty())
                    til.error = getString(R.string.error_empty)
                else {
                    if (matchTil.editText!!.text.toString().isNotEmpty() && editText.toString().trim() != matchTil.editText!!.text.toString().trim()) {
                        til.error = getString(R.string.error_matching_passwords)
                    } else {
                        til.isErrorEnabled = false
                        if (matchTil.isErrorEnabled)
                            matchTil.isErrorEnabled = false
                    }
                }
            }

        })
    }
}