package monopoly.model.spaces

import monopoly.model.Game
import monopoly.model.Player

/**
 * PropertySpace: Et objekt til at repræsentere ejendoms felter
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Cecilie Krog Drejer, s185032
 * @author Anders Brandt, s185016
 */
class PropertySpace : StationSpace() {

    /**
     * GetHousesBuilt: Henter antallet af huse der er bygget på ejendommen
     *
     * @author Anders Brandt, s185016
     *
     * @return Returnerer antallet af huse bygget på ejendommen
     */
    val housesBuilt: Int
        get() = this.getInteger(Properties.BUILD_LEVEL.property)!!

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        BOARD_POSITION("boardPosition"), NAME("name"), MORTGAGED("mortgaged"), PRICE("price"), BASE_RENT("baseRent"),
        OWNER("owner"), BUILD_LEVEL("buildLevel"), COLOR("color")
    }

    /**
     * Equals: Bestemmer om et PropertySpace ligner et andet
     *
     * @param obj Det objekt, som det pågældende PropertySpace skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om de to ojekter er ens eller ej
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is PropertySpace)
            return false
        val other = obj as PropertySpace?
        val myOwner = this.parent<Player>(Player::class.java)
        val otherOwner = other!!.parent<Player>(Player::class.java)
        if (myOwner == null || otherOwner == null) {
            // This means atleast one of these do not have an owner.
            if (!(myOwner == null && otherOwner == null)) {
                // Since only one space has an owner, they can´t be the same.
                return false
            }
        } else {
            // This means both have an owner
            if (myOwner.longId != otherOwner.longId) {
                // Not the same owner, not the same field.
                return false
            }
        }
        return true
    }

    /**
     * GetRent: Henter lejen for ejendommen
     *
     * @author Anders Brandt, s185016
     * @author Joakim Levorsen, s185023
     */
    override fun getRent(game: Game): Int {
        var baseRent = this.getInteger(Properties.BASE_RENT.property)!!
        baseRent += this.housesBuilt * baseRent / 2
        return baseRent
    }

    /**
     * SetBuildLevel: Sætter antallet af huse bygget på ejendommen
     *
     * @param amount Antallet af huse, der skal være på ejendommen
     *
     * @author Cecilie Krog Drejer, s185032
     */
    fun setBuildLevel(amount: Int) {
        this.set<PropertySpace>(PropertySpace.Properties.BUILD_LEVEL.property, amount)
    }

    companion object {

        /**
         * Create: Opretter et PropertySpace
         *
         * @param position Feltets placering på spillebrættet
         * @param name Feltets navn
         * @param price Ejendommens pris
         * @param baseRent Ejendommens basisleje
         * @param color Feltets farve
         *
         * @author Joakim Bøegh Levorsen, s185023
         * @author Cecilie Krog Drejer, s185032
         *
         * @return Returnerer et PropertySpace
         */
        fun create(position: Int, name: String, price: Int, baseRent: Int, color: String): PropertySpace {
            var space = PropertySpace()
            space = Space.setValues(space, name, color) as PropertySpace
            space.set<PropertySpace>(PropertySpace.Properties.BOARD_POSITION.property, position)
            space.set<PropertySpace>(PropertySpace.Properties.MORTGAGED.property, false)
            space.set<PropertySpace>(PropertySpace.Properties.PRICE.property, price)
            space.set<PropertySpace>(PropertySpace.Properties.BASE_RENT.property, baseRent)
            space.set<PropertySpace>(PropertySpace.Properties.BUILD_LEVEL.property, 0)
            return space
        }
    }
}
