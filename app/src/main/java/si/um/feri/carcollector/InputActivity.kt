package si.um.feri.carcollector

import android.content.Intent
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import si.um.feri.carcollection.Car
import si.um.feri.carcollector.databinding.ActivityInputBinding
import java.math.BigDecimal
import java.time.Year


class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    private val TAG = InputActivity::class.qualifiedName
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = "Add a car"

        setVibrator()


        val getQECodeData = registerForActivityResult(ScanContract()) {
            result: ScanIntentResult ->
            if (result.contents == null) {
                Toast.makeText(applicationContext,"Scan failed.", Toast.LENGTH_SHORT).show()
            }
            else {
                try {
                    val gson = GsonBuilder().create()
                    val car = gson.fromJson(result.contents, Car::class.java)
                    setCarResult(car)
                    this.finish()
                }
                catch(e: Exception) {
                    Log.e(TAG, "Invalid QR code")
                    if(vibrator.hasVibrator()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE))
                        }
                        else {
                            vibrator.vibrate(500)
                        }
                    }
                    Toast.makeText(applicationContext,"Invalid QR code.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.closeActivityBtn.setOnClickListener {
            this.finish()
        }

        binding.addCarBtn.setOnClickListener {
            addNewCar(binding.addCarBtn)
        }

        binding.scanQRCode.setOnClickListener {
            val options = ScanOptions()
            options.setBeepEnabled(false)
            options.createScanIntent(this)
            options.addExtra("SCAN_MODE", "QR_CODE_MODE")
            options.setOrientationLocked(false)
            options.setPrompt("Scan the QR code")

            getQECodeData.launch(options)
        }
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
                    carYear.toInt(),
                    carPower.toUInt(),
                    carMileage.toUInt(),
                    carPrice.toBigDecimal()
                )

                setCarResult(car)
                clearInputFields()
                this.finish()

                clearInputFields()
            }
        }
        catch(err: Exception) {
            Log.e(TAG, err.toString())
        }
    }

    fun setCarResult(car: Car): Intent {
        val intent = Intent()

        intent.putExtra("new_car", car)
        setResult(RESULT_OK, intent)
        return intent
    }

    fun clearInputFields() {
        binding.inputCarMake.text.clear()
        binding.inputCarModel.text.clear()
        binding.inputCarYearOfProduction.text.clear()
        binding.inputCarPower.text.clear()
        binding.inputCarMileage.text.clear()
        binding.inputCarPrice.text.clear()
    }

    fun setVibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            this.vibrator = vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            this.vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        if(!vibrator.hasVibrator()) {
            Log.i(TAG, "Device does not have a vibrator!")
        }
    }
}
