package si.um.feri.carcollector

import android.content.Intent
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import si.um.feri.carcollection.Car
import si.um.feri.carcollector.databinding.ActivityInputBinding
import si.um.feri.carcollector.enums.InputTypeEnum
import java.math.BigDecimal
import java.time.Year


class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    private val TAG = InputActivity::class.qualifiedName
    private lateinit var vibrator: Vibrator
    private lateinit var app: MyApplication
    private lateinit var mode: InputTypeEnum
    private lateinit var editCar: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
        setContentView(R.layout.activity_input)

        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = getString(R.string.activity_title_input)

        val inputMode = intent.getIntExtra("MODE", InputTypeEnum.INSERT.value)
        mode = when(inputMode) {
            InputTypeEnum.EDIT.value -> InputTypeEnum.EDIT
            else -> InputTypeEnum.INSERT
        }

        setVibrator()

        updateCount()

        handleEditMode()

        val getQRCodeData = registerForActivityResult(ScanContract()) {
            result: ScanIntentResult ->
            if (result.contents == null) {
                Toast.makeText(applicationContext,getString(R.string.error_scan_failed), Toast.LENGTH_SHORT).show()
            }
            else {
                try {
                    val gson = GsonBuilder().create()
                    val car = gson.fromJson(result.contents, Car::class.java)
                    val intent = Intent()
                    intent.putExtra("new_car", car)
                    setResult(RESULT_OK, intent)
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
                    Toast.makeText(applicationContext,getString(R.string.error_invalid_qr_code), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.closeActivityBtn.setOnClickListener {
            this.finish()
        }

        binding.addCarBtn.setOnClickListener {
            inputHandle(binding.addCarBtn)
        }

        binding.scanQRCode.setOnClickListener {
            val options = ScanOptions()
            options.setBeepEnabled(false)
            options.createScanIntent(this)
            options.addExtra("SCAN_MODE", "QR_CODE_MODE")
            options.setOrientationLocked(false)
            options.setPrompt(getString(R.string.prompt_scan_qr_code))

            getQRCodeData.launch(options)
        }
    }

    fun handleEditMode() {
        if(mode != InputTypeEnum.EDIT) return

        binding.addCarBtn.text = getString(R.string.noun_update)
        binding.scanQRCode.visibility = View.INVISIBLE

        editCar = intent.getSerializableExtra("edit_car") as Car

        binding.inputCarMake.setText(editCar.make)
        binding.inputCarModel.setText(editCar.model)
        binding.inputCarYearOfProduction.setText(editCar.year.toString())
        binding.inputCarPower.setText(editCar.power.toString())
        binding.inputCarMileage.setText(editCar.mileage.toString())
        binding.inputCarPrice.setText(editCar.price.toFloat().toString())
    }

    fun inputHandle(view: View) {
        val carMake = binding.inputCarMake.text.trim().toString()
        val carModel = binding.inputCarModel.text.trim().toString()
        val carYear = binding.inputCarYearOfProduction.text.trim().toString()
        val carPower = binding.inputCarPower.text.trim().toString()
        val carMileage = binding.inputCarMileage.text.trim().toString()
        val carPrice = binding.inputCarPrice.text.trim().toString()

        try {
            if(carMake.isEmpty() || carModel.isEmpty() || carYear.isEmpty() ||
                carPower.isEmpty() || carMileage.isEmpty() || carPrice.isEmpty()) {
                Toast.makeText(applicationContext,getString(R.string.error_empty_fields_all), Toast.LENGTH_SHORT).show()
            }
            else if(Year.of(carYear.toInt()) < Year.of(1886) ||
                Year.of(carYear.toInt() - 1) > Year.now()) {
                binding.inputCarYearOfProduction.error = getString(R.string.error_invalid_car_year)
            }
            else if(carPower.toInt() < 0 || carPower.toInt() > 1900) {
                binding.inputCarPower.error = getString(R.string.error_invalid_car_hp)
            }
            else if(carMileage.toInt() < 0) {
                binding.inputCarMileage.error = getString(R.string.error_invalid_car_mileage)
            }
            else if(carPrice.toBigDecimal() < BigDecimal.ZERO) {
                binding.inputCarPrice.error = getString(R.string.error_invalid_car_price)
            }
            else if(mode == InputTypeEnum.EDIT){
                Log.i(TAG,"Editing a car id:${editCar.id}")

                editCar.make = carMake
                editCar.model = carModel
                editCar.year = carYear.toInt()
                editCar.power = carPower.toUInt()
                editCar.mileage = carMileage.toUInt()
                editCar.price = carPrice.toBigDecimal()

                val intent = Intent()
                intent.putExtra("edit_car", editCar)
                setResult(RESULT_OK, intent)
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

                val intent = Intent()
                intent.putExtra("new_car", car)
                setResult(RESULT_OK, intent)
            }
            clearInputFields()
            this.finish()
        }
        catch(err: Exception) {
            Log.e(TAG, err.toString())
        }
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

    private fun updateCount() {
        val keyword: String = getString(R.string.label_analytics_activity_car_input)
        val count = app.sharedPref.getInt(keyword,0) + 1
        app.editor.putInt(keyword, count)
        app.editor.apply()
    }
}
