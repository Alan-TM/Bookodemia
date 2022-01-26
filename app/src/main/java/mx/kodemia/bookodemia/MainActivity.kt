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
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import mx.kodemia.bookodemia.tools.hasLetter
import mx.kodemia.bookodemia.tools.makeSnacks
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var TAG = MainActivity::class.qualifiedName
    private var parent_view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parent_view = findViewById(android.R.id.content)

        textView_registro.setOnClickListener {
            startActivity(Intent(this, FormRegistro::class.java))
        }

        button_iniciar.setOnClickListener {
            startLogIn()
        }

        initTietsErrorLogin(tiet_correo, til_correo)
        initTietsErrorLogin(tiet_password, til_password)

    }

    private fun startLogIn() {
        if (til_correo.editText?.text.toString().trim()
                .isNotEmpty() && til_password.editText?.text.toString().trim().isNotEmpty()
        ) {
            if(!hasLetter(tiet_correo.text.toString(), '@')) {
                makeSnacks(parent_view!!, getText(R.string.error_invalid_email), getColor(R.color.error))
                tiet_correo.requestFocus()
            }
            else {
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
                        startActivity(Intent(this, Home::class.java))
                    },
                    { error ->
                        Log.e(TAG, error.toString())
                        with(AlertDialog.Builder(this)) {
                            setTitle(getText(R.string.error_dialog_title))
                            setMessage(getText(R.string.error_dialog_body))
                            setCancelable(true)
                            setNegativeButton(getText(android.R.string.ok), null)
                            show()
                        }
                    })
                queue.add(request)
            }
        } else {
            makeSnacks(parent_view!!, resources.getText(R.string.error_login_button), getColor(R.color.error))

            if(tiet_correo.text!!.isEmpty() && tiet_password.text!!.isEmpty())
                til_correo.requestFocus()
            else if(tiet_password.text!!.isEmpty())
                til_password.requestFocus()
            else
                til_correo.requestFocus()
        }
    }

    private fun initTietsErrorLogin(tiet: TextInputEditText, til: TextInputLayout) {

        tiet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editText: Editable?) {
                if (editText.toString().trim().isEmpty()) {
                    til.setError(resources.getText(R.string.error_empty).toString())
                } else {
                    til.isErrorEnabled = false
                    til.setError("")
                }
            }

        })

    }
}