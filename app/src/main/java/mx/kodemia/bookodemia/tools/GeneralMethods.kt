package mx.kodemia.bookodemia.tools

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import mx.kodemia.bookodemia.R
import org.json.JSONObject

fun makeSnacks(v: View, text: CharSequence, color: Int) {
    Snackbar.make(
        v,
        text,
        Snackbar.LENGTH_SHORT
    )
        .setBackgroundTint(color)
        .show()
}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Check if no view has focus
    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun checkEmptyOrErrorFields(context: Context, vararg til: TextInputLayout): Boolean {
    var count = 0
    for (i in til) {
        if (i.editText!!.text.toString().isEmpty()) {
            i.error = context.getString(R.string.error_empty)
            count++
        } else if (i.isErrorEnabled)
            count++
    }
    return count == 0
}

fun afterEmailTextErrorWatcher(context: Context, tiet: TextInputEditText, til: TextInputLayout) {

    tiet.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editText: Editable?) {
            if (editText.toString().trim().isEmpty()) {
                til.error = context.getString(R.string.error_empty)
            } else {
                if (editText!!.contains('@') && editText!!.contains('.')) {
                    til.isErrorEnabled = false
                } else {
                    til.error = context.getString(R.string.error_invalid_email)
                }
            }
        }

    })

}

fun afterTextErrorWatcher(context: Context, tiet: TextInputEditText, til: TextInputLayout) {
    tiet.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editText: Editable?) {
            if (editText.toString().trim().isEmpty()) {
                til.error = context.getString(R.string.error_empty)
            } else {
                til.isErrorEnabled = false
            }
        }

    })
}

//verificar si tiene acceso a internet
fun verifyInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

fun loadingComponents(
    pb: LottieAnimationView,
    button: Button,
    visible: Boolean,
    buttonText: String,
    enable: Boolean
) {
    pb.visibility = when (visible) {
        true -> View.VISIBLE
        false -> View.GONE
    }
    button.text = buttonText
    button.isEnabled = enable
}
