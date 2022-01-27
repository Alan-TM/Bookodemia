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
import mx.kodemia.bookodemia.tools.*

class FormRegistro : AppCompatActivity() {

    private var parent_view: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_registro)

        button_regresar_main.setOnClickListener {
            finish()
        }

        parent_view = findViewById(android.R.id.content)

        afterTextErrorWatcher(applicationContext, tiet_registro_user, til_registro_user)
        afterEmailTextErrorWatcher(applicationContext, tiet_registro_correo, til_registro_correo)
        afterTextErrorWatcher(applicationContext, tiet_registro_password, til_registro_password)
        errorMatchinPasswords()

        button_registro_crear.setOnClickListener {
            if (checkEmptyFields(
                    applicationContext,
                    til_registro_user,
                    til_registro_correo,
                    til_registro_password,
                    til_registro_confPassword
                ) &&
                checkFieldsErrors(til_registro_user, til_registro_correo, til_registro_password, til_registro_confPassword)
            ) {
                makeSnacks(parent_view!!, getString(R.string.success_register), getColor(R.color.blue_dark))
            }
        }

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