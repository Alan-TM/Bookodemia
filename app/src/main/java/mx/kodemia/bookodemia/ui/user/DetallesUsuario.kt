package mx.kodemia.bookodemia.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_detalles_usuario.*
import mx.kodemia.bookodemia.R
import mx.kodemia.bookodemia.models.user.User
import mx.kodemia.bookodemia.tools.SharedPreferenceTools
import mx.kodemia.bookodemia.ui.login.Login

class DetallesUsuario : Fragment() {
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("usuario") as User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalles_usuario, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetallesUsuario().apply {
                arguments = Bundle().apply {
                    putSerializable("usuario", user)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = view.context as AppCompatActivity

        initComponents()

        image_user_logout.setOnClickListener{
            SharedPreferenceTools(context).deleteTokenPreference()
            makeLogOutAlert(context, view)
        }
    }

    fun initComponents(){

        arguments?.let{
            text_user_name.text = user.name
            text_user_id.text = user.id
            text_user_email.text = user.email
            text_user_created.text = getString(R.string.user_created_at, user.createdAt)
        }

    }
    fun makeLogOutAlert(context: AppCompatActivity, view: View){
        MaterialAlertDialogBuilder(view.context, R.style.MaterialAlertDialog_Material3)
            .setMessage("Desea cerrar sesión?")
            .setTitle("Salir")
            .setNegativeButton(getString(android.R.string.cancel)) { dialog, which ->
            }
            .setPositiveButton(getString(android.R.string.ok)) { dialog, which ->
                launchLogInActivity(context)
                dialog.dismiss()
            }
            .show()
    }

    fun launchLogInActivity(context: AppCompatActivity){
        startActivity(Intent(context, Login::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        context.finish()
    }
}