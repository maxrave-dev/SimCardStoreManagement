import android.content.Context
import android.content.SharedPreferences


class SaveSharedPreference {
    object SaveSharedPreference {
        const val PREF_USER_NAME = "username"
        private fun getSharedPreferences(ctx: Context?): SharedPreferences {
            return getSharedPreferences(ctx)
        }

        fun setUserName(ctx: Context?, userName: String?) {
            val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
            editor.putString(PREF_USER_NAME, userName)
            editor.commit()
        }

        fun getUserName(ctx: Context?): String? {
            return getSharedPreferences(ctx).getString(PREF_USER_NAME, "")
        }
        fun clearUserName(ctx: Context?) {
            val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
            editor.clear() //clear all stored data
            editor.commit()
        }
    }
}