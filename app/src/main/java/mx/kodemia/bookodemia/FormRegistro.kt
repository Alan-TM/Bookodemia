package mx.kodemia.bookodemia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_form_registro.*
import kotlinx.android.synthetic.main.activity_main.*
import mx.kodemia.bookodemia.tools.hasLetter
import mx.kodemia.bookodemia.tools.hideKeyboard
import mx.kodemia.bookodemia.tools.isEmptyOrError
import mx.kodemia.bookodemia.tools.makeSnacks

class FormRegistro : AppCompatActivity() {

    private var parent_view: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_registro)

        button_regresar_main.setOnClickListener {
            finish()
        }

        parent_view = findViewById(android.R.id.content)

        initTietsRegistro(tiet_registro_user, til_registro_user)
        initTietsRegistro(tiet_registro_correo, til_registro_correo)
        initTietsRegistro(tiet_registro_password, til_registro_password)
        errorMatchinPasswords()

        button_registro_crear.setOnClickListener {
            if (!isEmptyOrError(
                    til_registro_user,
                    til_registro_correo,
                    til_registro_password,
                    til_registro_confPassword
                )
            ) {
                if (hasLetter(tiet_registro_correo.text.toString(), '@')) {
                    makeSnacks(
                        parent_view!!,
                        "El usuario a sido creado con exito!",
                        getColor(R.color.blue_dark)
                    )

                    //TODO------ Request a registro !

                    hideKeyboard(this)
                } else{
                    makeSnacks(parent_view!!, getText(R.string.error_invalid_email), getColor(R.color.error))
                }
            } else
                makeSnacks(
                    parent_view!!,
                    "El usuario no pudo ser creado, por favor llena todos los campos",
                    getColor(R.color.error)
                )
        }

    }

    private fun initTietsRegistro(tiet: TextInputEditText, til: TextInputLayout) {

        tiet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

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

    private fun errorMatchinPasswords() {
        tiet_registro_confPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editText: Editable?) {
                if (editText.toString().trim() != tiet_registro_password.text.toString().trim()) {
                    til_registro_confPassword.setError(
                        resources.getText(R.string.error_matching_passwords).toString()
                    )
                } else {
                    til_registro_confPassword.isErrorEnabled = false
                    til_registro_confPassword.setError("")
                }
            }

        })
    }
}