package mx.kodemia.bookodemia.tools

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import mx.kodemia.bookodemia.R
import org.json.JSONObject

class SharedPreferenceTools(val context: Context) {
    fun getPreferences(): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context.getString(R.string.name_file_preference),
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }


    fun initSessionToken(jsonObject: JSONObject) {
        val sharedPreferences = getPreferences()

        with(sharedPreferences.edit()) {
            putString(
                context.getString(R.string.preference_token),
                jsonObject[context.getString(R.string.api_key_token)].toString()
            )
            apply()
        }
    }

    fun validateSessionToken(): Boolean {
        val sharedPreferences = getPreferences()
        val token = sharedPreferences.getString(context.getString(R.string.preference_token), "")
        return !token.isNullOrEmpty()
    }

    fun getPreferenceTokenSession(clave: String): String {
        val sharedPreferences = getPreferences()
        return sharedPreferences.getString(clave, "").toString()
    }

    fun deleteTokenPreference() {
        val sharedPreferences = getPreferences()
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}