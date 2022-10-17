import si.um.feri.carcollection.CarCollection
import si.um.feri.carcollection.Person
import java.time.LocalDate

fun main() {
    val collection: CarCollection = CarCollection.generate(6)
    collection.owner = Person("Crt", "Gorican")
    collection.print()
}
