package si.um.feri.carcollector

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import si.um.feri.carcollection.Car

class CarAdapter(
    private var data: MutableList<Car>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(p0: View?, position: Int)
        fun onItemLongClick(p0: View?, position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnClickListener, View.OnLongClickListener {
        val image: ImageView = itemView.findViewById(R.id.displayCarImage)
        val makeModelYear: TextView = itemView.findViewById(R.id.displayMakeModelYear)
        val power: TextView = itemView.findViewById(R.id.displayPower)
        val mileage: TextView = itemView.findViewById(R.id.displayMileage)
        val price: TextView = itemView.findViewById(R.id.displayPrice)
        val line: CardView = itemView.findViewById(R.id.cvLine)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(p0, position)
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemLongClick(p0, position)
                return true
            }
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = data[position]

        holder.makeModelYear.text = String.format("%s %s (%d)",
            itemsViewModel.make,
            itemsViewModel.model,
            itemsViewModel.year)
        holder.power.text = String.format("%d hp", itemsViewModel.power.toInt())
        holder.mileage.text = String.format("%d km", itemsViewModel.mileage.toInt())
        holder.price.text = String.format("%.0f €", itemsViewModel.price.toFloat())
        //holder.image.setImageResource(R.drawable.car_solid)

        Picasso.get()
            .load("https://www.carlogos.org/car-logos/${itemsViewModel.make.lowercase()}-logo.png")
            .placeholder(R.drawable.car_solid)
            .error(R.drawable.car_solid)
            .fit() // fit image into imageView
            .noFade() // disable fade animation
            .into(holder.image)
    }

    override fun getItemCount() = data.size
}