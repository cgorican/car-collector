package si.um.feri.carcollector

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import si.um.feri.carcollection.*
import si.um.feri.carcollector.databinding.ActivityMainBinding


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

        val ownerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val firstName = result.data?.getStringExtra("first_name")
                val lastName = result.data?.getStringExtra("last_name")

                if(firstName != null && lastName != null) {
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


        binding.btnToInputActivity.setOnClickListener {
            toInputActivity(binding.btnToInputActivity)
        }

        binding.setOwnerBtn.setOnClickListener {
            ownerResult.launch(Intent(this, SetOwnerActivity::class.java))
        }

        updateSetOwnerBtn(binding.setOwnerBtn)
        updateOwnerDisplay(binding.ownerNameDisplay)
    }

    fun toInputActivity(view: View) {
        val intent = Intent(this, InputActivity::class.java)
        //intent.putExtra("COLLECTION",carCollection)
        startActivity(intent)
    }

    fun updateSetOwnerBtn(view: View) {
        view.visibility = when(carCollection.owner) {
            null -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }

    fun updateOwnerDisplay(view: View) {
        view.visibility = when(carCollection.owner) {
            null -> View.INVISIBLE
            else -> View.VISIBLE
        }
        view as TextView
        view.text = "${carCollection.owner.toString()}'s collection"
    }
}