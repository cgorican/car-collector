package si.um.feri.carcollection

import java.time.Year
import java.util.*

class Car(
    val manufacturer: String,     // Porsche
    val model: String,            // 964 Turbo
    val yearOfProduction: Year,   // 1990

    var power: UInt,              // 360 hp
    var mileage: UInt,            // 43.812 km
    val bodyType: BodyTypeEnum,   // Coupe
    val fuelType: FuelTypeEnum,   // Gas
    var color: String,

    var licensePlate: String,     // MBPG850
    var insurance: Insurance?,
    var tyres: Tyres?,
) : Comparable<Car> {
    val id: UUID = UUID.randomUUID()

    init {
        if(this.yearOfProduction.value < 1886 ||
            this.yearOfProduction.value > Year.now().value) {
            throw IllegalStateException("Invalid year of production.")
        }
    }

    override fun compareTo(other: Car): Int {
        var score: Int = 0
        var scoreOther: Int = 0

        score += this.power.toInt()
        score -= this.mileage.toInt()
        score += this.yearOfProduction.value

        scoreOther += other.power.toInt()
        scoreOther -= other.mileage.toInt()
        scoreOther += other.yearOfProduction.value

        if(scoreOther > score)  {
            return 1
        }
        return 0
    }

    override fun toString(): String {
        var result = String.format("%s %s, %d",
            manufacturer,
            model,
            yearOfProduction.value)

        result += String.format("\n%-10s %-10d hp", "Power:", power.toInt())
        result += String.format("\n%-10s %-10d km", "Mileage:", mileage.toInt())
        result += String.format("\n%-10s %-10s", "Body type:", bodyType)
        result += String.format("\n%-10s %-10s", "Fuel:", fuelType)
        result += String.format("\n%-10s %-10s", "Color:", color)
        result += String.format("\n%-10s %-10s", "License:", licensePlate)

        if(insurance != null) {
            result += "\n${insurance.toString()}"
        }
        if(tyres != null) {
            result += "\n${tyres.toString()}"
        }

        return result
    }
}