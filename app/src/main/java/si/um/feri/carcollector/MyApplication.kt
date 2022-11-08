package si.um.feri.carcollector

import android.app.Application
import android.content.Context
import si.um.feri.carcollection.CarCollection
import java.util.*

class MyApplication: Application() {
    val id: UUID = UUID.randomUUID()
    lateinit var data: CarCollection
    override fun onCreate() {
        super.onCreate()
        data = CarCollection.generate(20)
    }

    fun save() {
        getSharedPreferences("setting_1", Context.MODE_PRIVATE)
        getSharedPreferences("setting_2", Context.MODE_PRIVATE)
    }

    fun read() {

    }
}