package si.um.feri.carcollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import si.um.feri.carcollector.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    private val TAG = AboutActivity::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = "About the app"
    }
}