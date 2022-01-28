package mx.kodemia.bookodemia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_detalles_usuario.*
import mx.kodemia.bookodemia.tools.deleteTokenPreference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [detalles_usuario.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetallesUsuario : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment detalles_usuario.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetallesUsuario().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = view.context as AppCompatActivity

        button_user_back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("user", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        image_user_logout.setOnClickListener{
            deleteTokenPreference(context)
            makeLogOutAlert(context, view)

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
        startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        context.finish()
    }
}