package si.um.feri.carcollection

class Person(
    var firstname: String,
    var lastname: String,
) {
    override fun toString(): String =
        String.format("%s %s", firstname, lastname)
}