package mx.kodemia.bookodemia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_form_registro.*
import kotlinx.android.synthetic.main.activity_main.*

class FormRegistro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_registro)

        button_regresar_main.setOnClickListener {
            finish()
        }

        initTietsRegistro(tiet_registro_user, til_registro_user)
        initTietsRegistro(tiet_registro_correo, til_registro_correo)

        tiet_registro_confPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editText: Editable?) {
                if(editText.toString().trim() != tiet_registro_password.text.toString().trim()){
                    til_registro_confPassword.setError(resources.getText(R.string.error_matching_passwords).toString())
                } else{
                    til_registro_password.isErrorEnabled = false
                    til_registro_password.setError("")

                    til_registro_confPassword.isErrorEnabled = false
                    til_registro_confPassword.setError("")
                }
            }

        })

    }

    private fun initTietsRegistro(tiet: TextInputEditText, til: TextInputLayout) {

        tiet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {            }

            override fun afterTextChanged(editText: Editable?) {
                if(editText.toString().trim().isEmpty()){
                    til.setError(resources.getText(R.string.error_empty).toString())
                } else {
                    til.isErrorEnabled = false
                    til.setError("")
                }
            }

        })

    }
}