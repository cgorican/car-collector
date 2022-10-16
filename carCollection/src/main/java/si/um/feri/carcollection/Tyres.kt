package si.um.feri.carcollection

import java.time.Year

class Tyres(
    val front: String,
    val rear: String,
    val year: Year
) {
    init {
        if(this.year.value < 1886 ||
            this.year.value > Year.now().getValue()) {
            throw IllegalStateException("Invalid tyre year of production.")
        }
    }
}