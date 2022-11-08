package si.um.feri.carcollector

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import si.um.feri.carcollection.CarCollection
import java.io.File
import java.io.FileReader
import java.util.*

class MyApplication: Application() {
    val id: UUID = UUID.randomUUID()
    private val TAG = MyApplication::class.qualifiedName
    lateinit var data: CarCollection
    lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        //data/data/si.um.feri.carcollector/shared_prefs
        sharedPref = getSharedPreferences(getString(R.string.path_shared_pref), Context.MODE_PRIVATE)
        addSessionIdToSharedPref()
        data = CarCollection.generate(20)
    }

    private fun addSessionIdToSharedPref() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(getString(R.string.noun_uuid),id.toString())
        editor.apply()
    }

    private fun writeToFile(jsonString: String, path: String = getString(R.string.path_saved_data_file)) {
        val file = File(getString(R.string.path_saved_data_file))
        file.writeText(jsonString)
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

    private fun deserialize(path: String = getString(R.string.path_saved_data_file)): CarCollection? {
        val gson = GsonBuilder().create()
        val fileReader = FileReader(path)
        var carCollection: CarCollection? = null
        try {
            carCollection = gson.fromJson(fileReader, CarCollection::class.java)

        }
        catch(e: Exception) {
            Log.e(TAG, "Deserialization failed")
        }
        return carCollection
    }

    fun save() {
        val jsonString: String? = serialize(data)
        if(jsonString != null) {
            writeToFile(jsonString)
        }
    }

    fun read() {
        val fileData: CarCollection? = deserialize()
        if(fileData != null) data = fileData
    }
}