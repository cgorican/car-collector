package si.um.feri.carcollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import si.um.feri.carcollection.*
import java.time.Year

class MainActivity : AppCompatActivity() {
    lateinit var closeAppBtn: Button
    lateinit var infoBtn: Button
    lateinit var addCarBtn: Button
    private var carCollection = CarCollection(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        closeAppBtn  = findViewById(R.id.closeAppBtn1)
        infoBtn = findViewById(R.id.infoBtn1)
        addCarBtn = findViewById(R.id.addCarBtn)

        closeAppBtn.setOnClickListener {
            this.finish()
            System.exit(0)
        }

        infoBtn.setOnClickListener {
            Log.i("Amount of cars in the list", carCollection.cars.size.toString())
        }

        addCarBtn.setOnClickListener {
            val carMake: EditText = findViewById(R.id.inputCarMake)
            val carModel: EditText = findViewById(R.id.inputCarModel)
            val carYear: EditText = findViewById(R.id.inputCarYearOfProduction)
            val carPower: EditText = findViewById(R.id.inputCarPower)
            val carMileage: EditText = findViewById(R.id.inputCarMileage)
            val carPrice: EditText = findViewById(R.id.inputCarPrice)

            var newCar = Car(
                carMake.text.toString(),
                carModel.text.toString(),
                Year.of(carYear.text.toString().toInt()),
                carPower.text.toString().toUInt(),
                carMileage.text.toString().toUInt(),
                carPrice.text.toString().toBigDecimal()
            )

            carCollection.cars.add(newCar)

            carMake.setText("")
            carModel.setText("")
            carYear.setText("")
            carPower.setText("")
            carMileage.setText("")
            carPrice.setText("")
        }



    }
}