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
    private lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
        setContentView(R.layout.activity_set_owner)

        binding = ActivitySetOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = getString(R.string.activity_title_set_owner)

        updateCount()

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
            Toast.makeText(applicationContext, getString(R.string.error_empty_fields_all), Toast.LENGTH_SHORT).show()
        }
        else if(firstName.isEmpty()) {
            binding.inputOwnerFirstName.error = getString(R.string.error_invalid_first_name)
            Toast.makeText(applicationContext,getString(R.string.error_invalid_first_name), Toast.LENGTH_SHORT).show()
        }
        else if(lastName.isEmpty()) {
            binding.inputOwnerLastName.error = getString(R.string.error_invalid_last_name)
            Toast.makeText(applicationContext,getString(R.string.error_invalid_last_name), Toast.LENGTH_SHORT).show()
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

    private fun updateCount() {
        val keyword: String = getString(R.string.label_analytics_activity_set_owner)
        val count = app.sharedPref.getInt(keyword,0) + 1
        app.editor.putInt(keyword, count)
        app.editor.apply()
    }
}