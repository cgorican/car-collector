package si.um.feri.carcollection

import io.github.serpro69.kfaker.Faker
import java.time.Year
import java.util.*

class CarCollection {
    val id: UUID = UUID.randomUUID()
    var cars: MutableList<Car> = mutableListOf()

    companion object {
        @JvmStatic
        fun generate(n: Int): CarCollection {
            val result = CarCollection()

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
                    BodyTypeEnum.values()[(0..BodyTypeEnum.values().size-1).random()],
                    FuelTypeEnum.values()[(0..FuelTypeEnum.values().size-1).random()],
                    faker.vehicle.colors(),
                    faker.vehicle.licensePlate(),
                    null,
                    null)

                result.cars.add(tmpCar)
            }

            result.order()

            return result
        }
    }

    fun order() {
        this.cars = this.cars.sortedWith(compareBy({ it.manufacturer }, { it.model })).toMutableList()
    }

    fun print() {
        for(car in cars) {
            println("${car.toString()}\n")
        }
    }
}