package si.um.feri.carcollector

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import si.um.feri.carcollection.*
import si.um.feri.carcollector.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = MainActivity::class.qualifiedName
    private lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = getString(R.string.activity_title_main)

        val setOwner = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                var firstName = result.data?.getStringExtra("first_name")
                var lastName = result.data?.getStringExtra("last_name")

                if(firstName != null && lastName != null) {

                    firstName = firstName.lowercase().replaceFirstChar { it.uppercaseChar() }
                    lastName = lastName.lowercase().replaceFirstChar { it.uppercaseChar() }

                    app.data.owner = Person(firstName,lastName)
                    updateSetOwnerBtn(binding.setOwnerBtn)
                    updateOwnerDisplay(binding.ownerNameDisplay)
                }
                else if(firstName == null){
                    Log.e(TAG,"first_name is null")
                }
                else {
                    Log.e(TAG,"last_name is null")
                }
            }
        }

        val addCar = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if(result.resultCode == RESULT_OK) {
                val car = result.data?.getSerializableExtra("new_car") as Car

                app.data.cars.add(car)
                updateCarCount(binding.carCountDisplay)
                Log.i(TAG, "New car added to collection")
            }
        }

        binding.btnToAboutActivity.setOnClickListener {
            toAboutActivity(binding.btnToAboutActivity)
        }

        binding.btnToInputActivity.setOnClickListener {
            addCar.launch(Intent(this, InputActivity::class.java))
        }

        binding.setOwnerBtn.setOnClickListener {
            setOwner.launch(Intent(this, SetOwnerActivity::class.java))
        }

        updateCarCount(binding.carCountDisplay)
        updateSetOwnerBtn(binding.setOwnerBtn)
        updateOwnerDisplay(binding.ownerNameDisplay)
    }

    fun updateCarCount(view: View) {
        view as TextView
        view.text = when(app.data.cars.size) {
            0 -> getString(R.string.no_cars)
            1 -> getString(R.string.noun_car_single)
            2 -> "${app.data.cars.size} ${getString(R.string.noun_car_double)}"
            else -> "${app.data.cars.size} ${getString(R.string.noun_car_plural)}"
        }
    }

    fun toAboutActivity(view: View) {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    fun updateSetOwnerBtn(view: View) {
        view.visibility = when(app.data.owner) {
            null -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }

    fun updateOwnerDisplay(view: View) {
        when(app.data.owner) {
            null -> view.visibility = View.INVISIBLE
            else -> {
                view.visibility = View.VISIBLE
                view as TextView
                view.text = String.format("%s %s ${R.string.noun_collection}",  app.data.owner!!.firstname, app.data.owner!!.lastname)
            }
        }
    }
}