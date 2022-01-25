package mx.kodemia.bookodemia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var parent_view: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parent_view = findViewById(android.R.id.content)

        textView_registro.setOnClickListener {
            startActivity(Intent(this, FormRegistro::class.java))
        }

        button_iniciar.setOnClickListener{
            if(til_correo.editText?.text.toString().trim().isNotEmpty() || til_password.editText?.text.toString().trim().isNotEmpty()){
                startActivity(Intent(this, Home::class.java))
            } else{
                Snackbar.make(parent_view!!, resources.getText(R.string.error_login_button), Snackbar.LENGTH_SHORT).show()
                til_correo.requestFocus()
            }

        }

        initTietsRegistro(tiet_correo, til_correo)
        initTietsRegistro(tiet_password, til_password)

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