package si.um.feri.carcollector

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import si.um.feri.carcollector.databinding.ActivitySetOwnerBinding

class SetOwnerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetOwnerBinding
    private val TAG = SetOwnerActivity::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_owner)

        binding = ActivitySetOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = getString(R.string.activity_title_set_owner)

        binding.closeActivityBtn.setOnClickListener {
            clearInputFields()

            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)

            this.finish()
        }

        binding.setOwnerBtn.setOnClickListener {
            setOwnerData(binding.setOwnerBtn)
        }
    }

    fun setOwnerData(view: View) {
        val returnIntent = Intent()

        val firstName = binding.inputOwnerFirstName.text.trim().toString()
        val lastName = binding.inputOwnerLastName.text.trim().toString()

        if(firstName.isEmpty() && lastName.isEmpty()) {
            Toast.makeText(applicationContext,"Please fill out the fields!", Toast.LENGTH_SHORT).show()
        }
        else if(firstName.isEmpty()) {
            binding.inputOwnerFirstName.error = "Invalid first name!"
            Toast.makeText(applicationContext,"Invalid first name!", Toast.LENGTH_SHORT).show()
        }
        else if(lastName.isEmpty()) {
            binding.inputOwnerLastName.error = "Invalid last name!"
            Toast.makeText(applicationContext,"Invalid last name!", Toast.LENGTH_SHORT).show()
        }
        else {
            returnIntent.putExtra("first_name", firstName)
            returnIntent.putExtra("last_name", lastName)
            setResult(Activity.RESULT_OK, returnIntent)
            clearInputFields()
            this.finish()
        }
        clearInputFields()
    }

    fun clearInputFields() {
        binding.inputOwnerFirstName.text.clear()
        binding.inputOwnerLastName.text.clear()
    }
}