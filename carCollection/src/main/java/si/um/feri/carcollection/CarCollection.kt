package si.um.feri.carcollection

import io.github.serpro69.kfaker.Faker
import java.time.Year
import java.util.*

class CarCollection(
    var owner: Person?
) {
    val id: UUID = UUID.randomUUID()
    var cars: MutableList<Car> = mutableListOf()

    companion object {
        @JvmStatic
        fun generate(n: Int): CarCollection {
            val result = CarCollection(null)

            // Random
            val faker = Faker()
            faker.unique.configuration {
                //enable(faker::vehicle)
            }

            for (i in 1..n) {
                val tmpMake = faker.vehicle.makes()
                val tmpCar = Car(
                    tmpMake,
                    faker.vehicle.modelsByMake(tmpMake),
                    Year.of((1964..2022).random()),
                    (80..840).random().toUInt(),
                    (0..735301).random().toUInt(),
                    (13829..73301).random().toBigDecimal()
                )

                result.cars.add(tmpCar)
            }

            result.order()

            return result
        }
    }

    fun add(x: Car) {
        cars.add(x)
        this.order()
    }

    fun order() {
        this.cars = this.cars.sortedWith(compareBy({ it.make }, { it.model })).toMutableList()
    }

    fun print() {
        println("Owner: ${owner.toString()}\nCars:")
        for(car in cars) {
            println("${car}\n")
        }
    }
}