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
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
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

fun afterEmailTextErrorWatcher(context: Context, til: TextInputLayout) {
    til.editText?.doAfterTextChanged { text ->
        if(helperValidateEmailField(text.toString()))
            til.isErrorEnabled = false
        else
            til.error = context.getString(R.string.error_invalid_email)
    }
}

fun helperValidateEmailField(text: String): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()

fun helperValidateEmptyField(text: String): Boolean = text.isNotEmpty()

fun afterTextErrorWatcher(context: Context, til: TextInputLayout) {
    til.editText?.doAfterTextChanged { text ->
        if(helperValidateEmptyField(text.toString()))
            til.isErrorEnabled = false
        else
            til.error = context.getString(R.string.error_empty)
    }
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
