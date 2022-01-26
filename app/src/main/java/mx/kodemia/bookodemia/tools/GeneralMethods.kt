package mx.kodemia.bookodemia.tools

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getColor
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import mx.kodemia.bookodemia.R

fun makeSnacks(v: View, text: CharSequence, color: Int){
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

fun hasLetter(text: String, letter: Char): Boolean {
    for(c in text){
        if(c == letter)
            return true
    }
    return false
}

fun isEmptyOrError(vararg tiet: TextInputLayout): Boolean{
    for(i in tiet){
        if(i.editText!!.text.toString().isEmpty() || i.isErrorEnabled){
            return true
        }
    }
    return false
}