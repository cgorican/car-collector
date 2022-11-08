package si.um.feri.carcollector

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import si.um.feri.carcollector.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    private val TAG = AboutActivity::class.qualifiedName
    private lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
        setContentView(R.layout.activity_about)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = getString(R.string.activity_title_about)

        updateCount()

        // Display UUID
        binding.appSessionUUIDView.text = app.sharedPref.getString(getString(R.string.noun_uuid),"")
    }

    private fun updateCount() {
        val keyword: String = getString(R.string.label_analytics_activity_about)
        val count = app.sharedPref.getInt(keyword,0) + 1
        app.editor.putInt(keyword, count)
        app.editor.apply()
    }
}