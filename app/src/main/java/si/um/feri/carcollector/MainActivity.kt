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
import java.util.*


class MainActivity : AppCompatActivity() {
    private var carCollection = CarCollection(null)
    private lateinit var binding: ActivityMainBinding
    private val TAG = MainActivity::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = "Car collection"

        val setOwner = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                var firstName = result.data?.getStringExtra("first_name")
                var lastName = result.data?.getStringExtra("last_name")

                if(firstName != null && lastName != null) {

                    firstName = firstName.lowercase().replaceFirstChar { it.uppercaseChar() }
                    lastName = lastName.lowercase().replaceFirstChar { it.uppercaseChar() }

                    carCollection.owner = Person(firstName,lastName)
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

                carCollection.add(car)
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
        view.text = when(carCollection.cars.size) {
            0 -> "No cars"
            1 -> "1 car"
            else -> "${carCollection.cars.size} cars"
        }
    }

    fun toAboutActivity(view: View) {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    fun updateSetOwnerBtn(view: View) {
        view.visibility = when(carCollection.owner) {
            null -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }

    fun updateOwnerDisplay(view: View) {
        when(carCollection.owner) {
            null -> view.visibility = View.INVISIBLE
            else -> {
                view.visibility = View.VISIBLE
                view as TextView
                view.text = String.format("%s %s\' collection",  carCollection.owner!!.firstname, carCollection.owner!!.lastname)
            }
        }
    }
}