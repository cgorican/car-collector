package si.um.feri.carcollector

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import si.um.feri.carcollection.CarCollection
import java.io.File
import java.io.FileReader
import java.util.*

class MyApplication: Application(), DefaultLifecycleObserver {
    private val TAG = MyApplication::class.qualifiedName
    private lateinit var gson: Gson
    private lateinit var file: File

    lateinit var data: CarCollection
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    val id: UUID = UUID.randomUUID()

    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        //data/data/si.um.feri.carcollector/shared_prefs
        sharedPref = getSharedPreferences(getString(R.string.path_shared_pref), Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        addSessionIdToSharedPref()
        data = CarCollection.generate(100)

        gson = Gson()
        //data/data/si.um.feri.carcollector/files
        file = File(filesDir, getString(R.string.path_saved_data_file))
        if(file.exists()) {
            readFromFile()
        }
        else {
            saveToFile()
        }

        handleDarkModeSettings()

        updateCount()
    }

    private fun updateCount() {
        val keyword: String = getString(R.string.label_analytics_launches)
        val count = sharedPref.getInt(keyword,0) + 1
        editor.putInt(keyword, count)
        editor.apply()
    }

    private fun addSessionIdToSharedPref() {
        editor.putString(getString(R.string.noun_uuid),id.toString())
        editor.apply()
    }

    private fun serialize(value: Any): String? {
        val gson = GsonBuilder().create()
        var jsonString: String? = null
        try {
            jsonString = gson.toJson(data)
        }
        catch(e: Exception) {
            Log.e(TAG, "Serialization failed")
        }
        return jsonString
    }

    private fun deserialize(): CarCollection? {
        if(!file.exists()) return null
        val fileReader = FileReader(file)
        var carCollection: CarCollection? = null
        try {
            carCollection = gson.fromJson(fileReader, CarCollection::class.java)
        }
        catch(e: Exception) {
            Log.e(TAG, "Deserialization failed")
        }
        return carCollection
    }

    fun saveToFile() {
        val jsonString: String? = serialize(data)
        if(jsonString != null) {
            file.writeText(jsonString)
        }
    }

    private fun handleDarkModeSettings() {
        when(sharedPref.getBoolean(getString(R.string.shared_pref_dark_mode), false)) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun readFromFile() {
        val fileData: CarCollection? = deserialize()
        if(fileData != null) data = fileData
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        handleDarkModeSettings()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        val keyword: String = getString(R.string.label_analytics_sent_to_background)
        val count = sharedPref.getInt(keyword,0) + 1
        editor.putInt(keyword, count)
        editor.apply()
    }
}