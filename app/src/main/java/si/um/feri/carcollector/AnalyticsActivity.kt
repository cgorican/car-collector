package si.um.feri.carcollector

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import si.um.feri.carcollector.databinding.ActivityAboutBinding
import si.um.feri.carcollector.databinding.ActivityAnalyticsBinding

class AnalyticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyticsBinding
    private val TAG = AnalyticsActivity::class.qualifiedName
    private lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
        setContentView(R.layout.activity_about)

        binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = getString(R.string.activity_title_analytics)

        updateCount()

        // Entire app
        binding.countLaunches.text = app.sharedPref.getInt(getString(R.string.label_analytics_launches),0).toString()
        binding.countSentToBackground.text = app.sharedPref.getInt(getString(R.string.label_analytics_sent_to_background),0).toString()
        // By activities
        binding.countAbout.text = app.sharedPref.getInt(getString(R.string.label_analytics_activity_about),0).toString()
        binding.countAnalytics.text = app.sharedPref.getInt(getString(R.string.label_analytics_activity_analytics),0).toString()
        binding.countCarInput.text = app.sharedPref.getInt(getString(R.string.label_analytics_activity_car_input),0).toString()
        binding.countMain.text = app.sharedPref.getInt(getString(R.string.label_analytics_activity_main),0).toString()
        binding.countSetOwner.text = app.sharedPref.getInt(getString(R.string.label_analytics_activity_set_owner),0).toString()
        binding.countSettings.text = app.sharedPref.getInt(getString(R.string.label_analytics_activity_settings),0).toString()
    }

    private fun updateCount() {
        val keyword: String = getString(R.string.label_analytics_activity_analytics)
        val count = app.sharedPref.getInt(keyword,0) + 1
        app.editor.putInt(keyword, count)
        app.editor.apply()
    }

    override fun onResume() {
        super.onResume()
        binding.countSentToBackground.text = app.sharedPref.getInt(getString(R.string.label_analytics_sent_to_background),0).toString()
    }
}