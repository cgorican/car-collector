package si.um.feri.carcollection

import java.io.Serializable
import java.math.BigDecimal
import java.time.Year
import java.util.*

class Car(
    val make: String,       // Porsche
    val model: String,      // 964 Turbo
    val year: Year,         // 1990

    var power: UInt,        // 360 hp
    var mileage: UInt,      // 43.812 km
    var price: BigDecimal   // 154.000 €
) : Comparable<Car>, Serializable {
    val id: UUID = UUID.randomUUID()

    init {
        if(this.year.value < 1886 ||
            this.year.value > Year.now().value) {
            throw IllegalStateException("Invalid year of production.")
        }
    }

    override fun compareTo(other: Car): Int {
        var score: Int = 0
        var scoreOther: Int = 0

        score += this.power.toInt()
        score -= this.mileage.toInt()
        score += this.year.value
        score += this.price.toInt()

        scoreOther += other.power.toInt()
        scoreOther -= other.mileage.toInt()
        scoreOther += other.year.value
        scoreOther += other.price.toInt()

        if(scoreOther > score)  {
            return 1
        }
        return 0
    }

    override fun toString(): String {
        var result = String.format("%s %s, %d",
            make,
            model,
            year.value)

        result += String.format("\n%-10s %-10d hp", "Power:", power.toInt())
        result += String.format("\n%-10s %-10d km", "Mileage:", mileage.toInt())
        result += String.format("\n%-10s %-8.2f €", "Price:", price.setScale(2).toDouble())

        return result
    }
}