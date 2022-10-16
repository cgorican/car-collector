package si.um.feri.carcollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import si.um.feri.carcollection.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CarCollection.generate(3)
    }
}