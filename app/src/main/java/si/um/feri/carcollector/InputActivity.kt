package si.um.feri.carcollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Input
import android.util.Log
import android.view.View
import android.widget.Toast
import si.um.feri.carcollection.Car
import si.um.feri.carcollector.databinding.ActivityInputBinding
import si.um.feri.carcollector.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.time.Year

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    private val TAG = InputActivity::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = "Add a car"

        binding.closeActivityBtn.setOnClickListener {
            this.finish()
        }

        binding.infoBtn.setOnClickListener {
            logInfo(binding.infoBtn)
        }

        binding.addCarBtn.setOnClickListener {
            addNewCar(binding.addCarBtn)
        }
    }

    fun logInfo(view: View) {
        /*
        when(carCollection.cars.size) {
            0 -> Log.i(TAG, "There is no cars in the list.")
            1 -> Log.i(TAG, "There is ${carCollection.cars.size} car in the list.")
            else -> Log.i(TAG, "There are ${carCollection.cars.size} cars in the list.")
        }
        */
    }


    fun addNewCar(view: View) {
        val carMake = binding.inputCarMake.text.trim().toString()
        val carModel = binding.inputCarModel.text.trim().toString()
        val carYear = binding.inputCarYearOfProduction.text.trim().toString()
        val carPower = binding.inputCarPower.text.trim().toString()
        val carMileage = binding.inputCarMileage.text.trim().toString()
        val carPrice = binding.inputCarPrice.text.trim().toString()

        try {
            if(carMake.isEmpty() || carModel.isEmpty() || carYear.isEmpty() ||
                carPower.isEmpty() || carMileage.isEmpty() || carPrice.isEmpty()) {
                Toast.makeText(applicationContext,"Please fill out all of the fields!", Toast.LENGTH_SHORT).show()
            }
            else if(Year.of(carYear.toInt()) < Year.of(1886) ||
                Year.of(carYear.toInt() - 1) > Year.now()) {
                binding.inputCarYearOfProduction.error = "Invalid car year! -  Cars did not exist before 1886"
            }
            else if(carPower.toInt() < 0 || carPower.toInt() > 1900) {
                binding.inputCarPower.error = "Invalid car horse power!"
            }
            else if(carMileage.toInt() < 0) {
                binding.inputCarMileage.error = "Invalid car mileage!"
            }
            else if(carPrice.toBigDecimal() < BigDecimal.ZERO) {
                binding.inputCarPrice.error = "Invalid car price!"
            }
            else {
                Log.i(TAG,"Adding a new car to the list.")

                val car = Car(
                    carMake,
                    carModel,
                    Year.of(carYear.toInt()),
                    carPower.toUInt(),
                    carMileage.toUInt(),
                    carPrice.toBigDecimal()
                )

                //carCollection.add(car)

                binding.inputCarMake.text.clear()
                binding.inputCarModel.text.clear()
                binding.inputCarYearOfProduction.text.clear()
                binding.inputCarPower.text.clear()
                binding.inputCarMileage.text.clear()
                binding.inputCarPrice.text.clear()
            }
        }
        catch(err: Exception) {
            Log.e(TAG, err.toString())
        }
    }
}