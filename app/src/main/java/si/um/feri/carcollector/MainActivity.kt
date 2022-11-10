package si.um.feri.carcollector

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import si.um.feri.carcollection.*
import si.um.feri.carcollector.databinding.ActivityMainBinding
import si.um.feri.carcollector.enums.InputTypeEnum
import si.um.feri.carcollector.enums.ViewTypeEnum


class MainActivity : AppCompatActivity(), CarAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private val TAG = MainActivity::class.qualifiedName
    private lateinit var app: MyApplication
    private lateinit var adapter: CarAdapter

    private val setOwner = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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

    private val addCar = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if(result.resultCode == RESULT_OK) {
            val car = result.data?.getSerializableExtra("new_car") as Car

            insertCar(car)
            Log.i(TAG, "New car added to collection")
        }
    }

    private val editCar = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if(result.resultCode == RESULT_OK) {
            val car = result.data?.getSerializableExtra("edit_car") as Car

            updateCar(car)
            Log.i(TAG, "Car updated id:${car.id}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
        adapter = CarAdapter(app.data.cars, this)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateCount()

        this.title = getString(R.string.activity_title_main)

        binding.btnToAboutActivity.setOnClickListener {
            toAboutActivity(binding.btnToAboutActivity)
        }

        binding.btnToInputActivity.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra("MODE", InputTypeEnum.INSERT.value)
            addCar.launch(intent)
        }

        binding.setOwnerBtn.setOnClickListener {
            setOwner.launch(Intent(this, SetOwnerActivity::class.java))
        }

        // Recycler View
        setupCarRecycler()

        updateSetOwnerBtn(binding.setOwnerBtn)
        updateOwnerDisplay(binding.ownerNameDisplay)
    }

    private fun setupCarRecycler() {
        updateCarRecyclerLayout()
        binding.recyclerCars.adapter = adapter
        binding.recyclerCars.setHasFixedSize(false)
    }

    private fun updateCarRecyclerLayout() {
        val viewTypeValue = app.sharedPref
            .getInt(getString(R.string.shared_pref_view_type), ViewTypeEnum.LIST.value)
        when(viewTypeValue) {
            ViewTypeEnum.GRID.value -> {
                binding.recyclerCars.layoutManager = GridLayoutManager(this, 2)
            }
            else -> {
                binding.recyclerCars.layoutManager = LinearLayoutManager(this)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun insertCar(car: Car) {
        val index = app.data.addCar(car)
        //adapter.notifyItemInserted(index) -- reordering
        adapter.notifyDataSetChanged()
        app.saveToFile()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCar(car: Car) {
        Log.i(TAG, car.toString())
        val index = app.data.updateCar(car)
        //adapter.notifyItemChanged(index) -- reordering
        adapter.notifyDataSetChanged()
        app.saveToFile()
    }

    private fun removeCar(car: Car) {
        val index = app.data.removeCar(car.id)
        adapter.notifyItemRemoved(index)
        app.saveToFile()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.analytics -> {
                val intent = Intent(this, AnalyticsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
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
                view.text = String.format("%s %s ${getString(R.string.noun_collection)}",  app.data.owner!!.firstname, app.data.owner!!.lastname)
            }
        }
    }

    private fun updateCount() {
        val keyword: String = getString(R.string.label_analytics_activity_main)
        val count = app.sharedPref.getInt(keyword,0) + 1
        app.editor.putInt(keyword, count)
        app.editor.apply()
    }

    override fun onItemClick(p0: View?, position: Int) {
        val intent = Intent(this, InputActivity::class.java)
        intent.putExtra("MODE", InputTypeEnum.EDIT.value)
        intent.putExtra("edit_car", app.data.cars[position])
        editCar.launch(intent)
    }

    override fun onItemLongClick(p0: View?, position: Int) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.noun_delete))
        builder.setMessage(app.data.cars[position].toString())
        //builder.setIcon(R.drawable._64turbo)
        builder.setPositiveButton(getString(R.string.noun_delete)) { dialogInterface, it ->
            removeCar(app.data.cars[position])
        }
        .setNegativeButton(getString(R.string.noun_cancel)) { dialogInterface, it ->
            dialogInterface.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        updateCarRecyclerLayout()
    }
}