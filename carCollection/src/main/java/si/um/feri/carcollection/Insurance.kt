package si.um.feri.carcollection

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Insurance(
    var expirationDate: LocalDateTime
) {
    fun isInsured(): Boolean =
        expirationDate > LocalDateTime.now()

    override fun toString(): String {
        var result = "Insurance: "
        if(expirationDate <= LocalDateTime.now()) {
            result += "expired"
        }
        else {
            result += expirationDate.format(DateTimeFormatter.ofPattern("dd.MM.y"))
        }
        return result
    }
}