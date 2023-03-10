package si.um.feri.carcollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import si.um.feri.carcollector.databinding.ActivitySettingsBinding
import si.um.feri.carcollector.enums.ViewTypeEnum

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val TAG = SettingsActivity::class.qualifiedName
    private lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
        setContentView(R.layout.activity_about)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = getString(R.string.activity_title_settings)

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            app.editor.putBoolean(getString(R.string.shared_pref_notifications), isChecked)
            app.editor.apply()
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            app.editor.putBoolean(getString(R.string.shared_pref_dark_mode), isChecked)
            app.editor.apply()
            when(isChecked) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        updateCount()

        // Spinner setup
        setupViewTypeSpinner()

        // Recover state from sharedPreferences
        applySharedPref()
    }

    private fun applySharedPref() {
        val darkMode = app.sharedPref.getBoolean(getString(R.string.shared_pref_dark_mode), false)
        val notifications = app.sharedPref.getBoolean(getString(R.string.shared_pref_notifications), true)
        val viewTypeValue = app.sharedPref.getInt(getString(R.string.shared_pref_view_type), ViewTypeEnum.LIST.value)
        val viewType: ViewTypeEnum = when(viewTypeValue) {
            ViewTypeEnum.GRID.value -> ViewTypeEnum.GRID
            else -> ViewTypeEnum.LIST
        }

        binding.switchDarkMode.isChecked = darkMode
        binding.switchNotifications.isChecked = notifications
        binding.spinnerViewType.setSelection(viewType.value)
    }

    private fun setupViewTypeSpinner() {
        val adapter = ArrayAdapter
            .createFromResource(this, R.array.view_types, android.R.layout.simple_spinner_item)
        adapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerViewType.adapter = adapter
        setupViewTypeSpinnerListener()
    }

    private fun setupViewTypeSpinnerListener() {
        binding.spinnerViewType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2) {
                    ViewTypeEnum.GRID.value -> app.editor.putInt(getString(R.string.shared_pref_view_type),
                        ViewTypeEnum.GRID.value)
                    else ->  app.editor.putInt(getString(R.string.shared_pref_view_type),
                        ViewTypeEnum.LIST.value)
                }
                app.editor.apply()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                app.editor.putInt(getString(R.string.shared_pref_view_type), ViewTypeEnum.LIST.value)
                app.editor.apply()
            }
        }
    }

    private fun updateCount() {
        val keyword: String = getString(R.string.label_analytics_activity_settings)
        val count = app.sharedPref.getInt(keyword,0) + 1
        app.editor.putInt(keyword, count)
        app.editor.apply()
    }
}