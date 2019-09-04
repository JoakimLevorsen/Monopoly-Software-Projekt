package monopoly.controller

import gui_main.GUI
import monopoly.model.Player
import monopoly.model.spaces.PropertySpace
import monopoly.model.spaces.Space
import monopoly.model.spaces.StationSpace
import org.json.JSONObject
import resources.json.JSONKey

import javax.swing.*
import java.awt.*
import java.util.*

class PropertyController(private val controller: GameController) {
    private val gooey: GUI
    private val jsonData: JSONObject

    init {
        this.jsonData = controller.game.languageData
        this.gooey = controller.view.gui
    }

    /**
     * Auction: Metode til at sælge en ejendom ved auktion.
     *
     * @param property Ejendommen, som skal auktioneres bort
     *
     * @author Anders Brandt, s185016
     * @author Cecilie Krog Drejer, s185032
     * @author Helle Achari, s180317
     * @author Joakim Bøegh Levorsen, s185023
     */

    fun auction(property: StationSpace) {
        gooey.showMessage(property.name + jsonData.getString(JSONKey.AUCTIONED_OFF.key))
        var currentBidderIndex = 0
        var topBid = 0
        var noBidCounter = 0
        var topBidder: Player? = null
        val players = controller.game.getPlayers()
        while (noBidCounter < players.size - 1 && topBid != 0 || noBidCounter < players.size) {
            val currentBidder = players[currentBidderIndex]
            val bid = gooey.getUserInteger(currentBidder.name + jsonData.getString(JSONKey.BID.key) + topBid,
                    0, currentBidder.accountBalance)
            if (bid > topBid) {
                topBid = bid
                topBidder = currentBidder
                noBidCounter = 0
            } else {
                if (bid != 0) {
                    gooey.showMessage(jsonData.getString(JSONKey.BID_NOT_VALID.key))
                }
                noBidCounter++
            }
            currentBidderIndex++
            if (currentBidderIndex == players.size)
                currentBidderIndex = 0
        }
        if (topBidder != null) {
            gooey.showMessage(topBidder.name + jsonData.getString(JSONKey.AUCTION_WINNER.key) + topBid)
            controller.cashController.paymentToBank(topBidder, topBid)
            property.setOwner(topBidder)
            topBidder.addToOwnedProperties(property, controller.game)
            return
        }
    }

    /**
     * Mortgage: Metode til at pantsætte en ejendom
     *
     * @param property Ejendommen, som skal pantsættes
     *
     * @author Cecilie Krog Drejer, s185032
     */

    fun mortgage(property: StationSpace) {
        val owner = property.getOwner(controller.game)
        if (owner != null) {
            val amount = property.price / 2
            if (property is PropertySpace && property.housesBuilt > 0) {
                gooey.showMessage(jsonData.getString(JSONKey.HOUSES_ON_PROPERTY_SELL_ALL_HOUSES.key))
                sellHouses(property, property.housesBuilt)
            }
            controller.cashController.paymentFromBank(owner, amount)
            property.isMortgaged = true
        }
    }

    /**
     * Unmortgage: Metode til at tilbagekøbe en pantsat ejendom
     *
     * @param property Den pantsatte ejendom, som skal tilbagekøbes
     *
     * @author Cecilie Krog Drejer, s185032
     */

    fun unmortgage(property: StationSpace) {
        val owner = property.getOwner(controller.game)
        val amount = (property.price / 2 * 1.1).toInt()
        if (owner!!.accountBalance > amount) {
            controller.cashController.paymentToBank(owner, amount)
            property.isMortgaged = false
        }
    }

    /**
     * OfferProperty: Metode som tilbyder spilleren at købe en ejendom og evt. køber
     * den
     *
     * @param property Ejendom, som tilbydes
     * @param player   Spilleren, der tilbydes ejendommen
     *
     * @author Helle Achari, s180317
     */

    fun offerProperty(property: StationSpace, player: Player) {
        val didChoose: Boolean
        if (player.accountBalance >= property.price) {
            didChoose = gooey.getUserLeftButtonPressed(
                    jsonData.getString(JSONKey.OFFER_PROPERTY.key) + property.name
                            + jsonData.getString(JSONKey.PROPERTY_COST.key) + property.price,
                    jsonData.getString(JSONKey.YES.key), jsonData.getString(JSONKey.NO.key))
        } else
            didChoose = false
        if (didChoose) {
            gooey.showMessage(jsonData.getString(JSONKey.BOUGHT_PROPERTY.key) + property.name)
            controller.cashController.paymentToBank(player, property.price)
            property.setOwner(player)
            player.addToOwnedProperties(property, controller.game)
        } else {
            auction(property)
        }
    }

    /**
     * SellToBank: Metode til at sælge ejendom til banken
     *
     * @param property Ejendom, som skal sælges til banken
     *
     * @author Cecilie Krog Drejer, s185032
     */

    fun sellToBank(property: StationSpace) {
        val owner = property.getOwner(controller.game)
        if (owner != null) {
            if (property is PropertySpace && property.housesBuilt > 0) {
                gooey.showMessage(jsonData.getString(JSONKey.HOUSES_ON_PROPERTY_SELL_ALL_HOUSES.key))
                sellHouses(property, property.housesBuilt)
            }
            val amount = property.price / 2
            controller.cashController.paymentFromBank(owner, amount)
            owner!!.removeFromOwnedProperties(property, controller.game)
            property.removeOwner()
            property.isMortgaged = false
        }
    }

    /**
     * BuildHouses: Metode til at bygge huse på ejendomme af typen PropertySpace
     *
     * @param property Ejendom, som der skal bygges på
     * @param houses   Antal huse, der skal bygges
     *
     * @author Cecilie Krog Drejer, s185032
     */

    fun buildHouses(property: PropertySpace, houses: Int) {
        val owner = property.getOwner(controller.game)
        if (owner != null) {
            val houseValue = property.price / 2
            // Hvis man prøver at bygge så mange huse, at det samlede antal overstiger 5,
            // bygges der op til 5 huse i alt
            if (property.housesBuilt + houses <= 5) {
                val amount = houseValue * houses
                controller.cashController.paymentToBank(owner, amount)
                property.setBuildLevel(property.housesBuilt + houses)
            } else {
                val amount = houseValue * (5 - property.housesBuilt)
                controller.cashController.paymentToBank(owner, amount)
                property.setBuildLevel(5)
            }
        }
    }

    /**
     * SellHouses: Metode til at sælge huse på ejendomme af typen PropertySpace
     *
     * @param property Ejendom, hvorfra huse skal sælges
     * @param houses   Antal huse, der skal sælges
     *
     * @author Cecilie Krog Drejer, s185032
     */

    fun sellHouses(property: PropertySpace, houses: Int) {
        val owner = property.getOwner(controller.game)
        if (owner != null) {
            val currentBuildLevel = property.housesBuilt
            val houseValue = property.price / 2
            // Hvis man prøver at sælge flere huse, end der er, sælges alle huse
            if (property.housesBuilt >= houses) {
                val amount = houseValue / 2 * houses
                controller.cashController.paymentFromBank(owner, amount)
                property.setBuildLevel(currentBuildLevel - houses)
            } else {
                val amount = houseValue / 2 * currentBuildLevel
                controller.cashController.paymentFromBank(owner, amount)
                property.setBuildLevel(0)
            }
        }
    }

    /**
     * Trade: Metode til at foretage byttehandler med andre spillere. OBS: Ejendomme
     * tages kun i bytte for penge.
     *
     * @param trader Spiller, som skal handle (den spiller, der pt. har tur)
     *
     * @author Cecilie Krog Drejer, s185032
     */

    fun trade(trader: Player) {
        if (!trader.getOwnedProperties(controller.game).isEmpty()) {
            while (gooey.getUserLeftButtonPressed(jsonData.getString(JSONKey.TRADE.key),
                            jsonData.getString(JSONKey.YES.key), jsonData.getString(JSONKey.NO.key))) {
                val buy = gooey.getUserLeftButtonPressed(jsonData.getString(JSONKey.BUY_OR_SELL_PROPERTY.key),
                        jsonData.getString(JSONKey.BUY_PROPERTY.key),
                        jsonData.getString(JSONKey.SELL_PROPERTY.key))
                if (buy) {
                    // Vælg spiller at handle med
                    val nameToOwnableSpace = HashMap<String, StationSpace>()
                    val nameToPlayer = HashMap<String, Player>()
                    val otherPlayers = controller.game.getPlayers()
                    otherPlayers.minus(trader)
                    val otherPlayersNames = arrayOfNulls<String>(otherPlayers.size)
                    for (i in otherPlayers.indices) {
                        val otherPlayer = otherPlayers[i]
                        otherPlayersNames[i] = otherPlayers[i].name
                        nameToPlayer[otherPlayer.name] = otherPlayers[i]
                    }
                    val playerToTrade = JOptionPane.showInputDialog(null,
                            jsonData.getString(JSONKey.CHOOSE_PLAYER.key),
                            jsonData.getString(JSONKey.BUY_PROPERTY.key), JOptionPane.QUESTION_MESSAGE, null,
                            otherPlayersNames, otherPlayersNames[0])
                    if (playerToTrade != null) {
                        val playerToTradeString = playerToTrade.toString()
                        val tradee = nameToPlayer[playerToTradeString]
                        // Hvis den spiller ikke har ejendom skip
                        if (tradee != null) {
                            val tradeeOwnedProperties = tradee.getOwnedProperties(controller.game)
                            if (tradeeOwnedProperties.size == 0) {
                                gooey.showMessage(jsonData.getString(
                                        tradee?.name + jsonData.getString(JSONKey.PLAYER_OWNS_NO_PROPERTIES.key)))
                            } else {
                                val propertyNames = arrayOfNulls<String>(tradeeOwnedProperties.size)
                                for (i in tradeeOwnedProperties.indices) {
                                    propertyNames[i] = tradeeOwnedProperties[i].name
                                    nameToOwnableSpace[tradeeOwnedProperties[i].name] = tradeeOwnedProperties[i]
                                }
                                val selection = JOptionPane.showInputDialog(null,
                                        jsonData.getString(JSONKey.CHOOSE_PROPERTY.key),
                                        jsonData.getString(JSONKey.BUY_PROPERTY.key), JOptionPane.QUESTION_MESSAGE, null, propertyNames, propertyNames[0])
                                if (selection != null) {
                                    val selectionString = selection.toString()
                                    val selectedSpace = nameToOwnableSpace[selectionString]
                                    if (selectedSpace != null) {
                                        val amount = gooey.getUserInteger(trader.name
                                                + jsonData.getString(JSONKey.AMOUNT_TO_PAY.key) + selection, 0,
                                                trader.accountBalance)
                                        val accept = gooey.getUserLeftButtonPressed(
                                                tradee.name + jsonData.getString(JSONKey.ACCEPT.key),
                                                jsonData.getString(JSONKey.YES.key),
                                                jsonData.getString(JSONKey.NO.key))
                                        if (accept) {
                                            if (selectedSpace is PropertySpace && selectedSpace.housesBuilt > 0) {
                                                gooey.showMessage(jsonData
                                                        .getString(JSONKey.HOUSES_ON_PROPERTY_SELL_ALL_HOUSES.key))
                                                sellHouses(selectedSpace,
                                                        selectedSpace.housesBuilt)
                                            }
                                            tradee.removeFromOwnedProperties(selectedSpace, controller.game)
                                            selectedSpace.removeOwner()
                                            trader.addToOwnedProperties(selectedSpace, controller.game)
                                            selectedSpace.setOwner(trader)
                                            controller.cashController.payment(trader, amount, tradee)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Tjek om denne spiller har noget ejendom at sælge.
                    val nameToOwnableSpace = HashMap<String, StationSpace>()
                    val nameToPlayer = HashMap<String, Player>()
                    val traderOwnedProperties = trader.getOwnedProperties(controller.game)
                    if (traderOwnedProperties.size == 0) {
                        gooey.showMessage(jsonData.getString(JSONKey.YOU_OWN_NO_PROPERTIES.key))
                    } else {
                        val propertyNames = arrayOfNulls<String>(traderOwnedProperties.size)
                        for (i in traderOwnedProperties.indices) {
                            propertyNames[i] = traderOwnedProperties[i].name
                            nameToOwnableSpace[traderOwnedProperties[i].name] = traderOwnedProperties[i]
                        }
                        val selection = JOptionPane.showInputDialog(null,
                                jsonData.getString(JSONKey.CHOOSE_PROPERTY.key),
                                jsonData.getString(JSONKey.SELL_PROPERTY.key), JOptionPane.QUESTION_MESSAGE, null,
                                propertyNames, propertyNames[0])
                        if (selection != null) {
                            val selectionString = selection.toString()
                            val selectedSpace = nameToOwnableSpace[selectionString]
                            if (selectedSpace is PropertySpace && selectedSpace.housesBuilt > 0) {
                                gooey.showMessage(
                                        jsonData.getString(JSONKey.HOUSES_ON_PROPERTY_SELL_ALL_HOUSES.key))
                                sellHouses(selectedSpace,
                                        selectedSpace.housesBuilt)
                            }
                            val otherPlayers = controller.game.getPlayers()
                            otherPlayers.minus(trader)
                            val otherPlayersNames = arrayOfNulls<String>(otherPlayers.size)
                            for (i in otherPlayers.indices) {
                                otherPlayersNames[i] = otherPlayers[i].name
                                nameToPlayer[otherPlayers[i].name] = otherPlayers[i]
                            }
                            val playerToTrade = JOptionPane.showInputDialog(null,
                                    jsonData.getString(JSONKey.CHOOSE_PLAYER.key),
                                    jsonData.getString(JSONKey.SELL_PROPERTY.key), JOptionPane.QUESTION_MESSAGE, null, otherPlayersNames, otherPlayersNames[0])
                            if (playerToTrade != null) {
                                val playerToTradeString = playerToTrade.toString()
                                val tradee = nameToPlayer[playerToTradeString]
                                if (tradee != null && selectedSpace != null) {
                                    val tradeeAccept = gooey.getUserLeftButtonPressed(
                                            tradee.name + jsonData.getString(JSONKey.ACCEPT.key),
                                            jsonData.getString(JSONKey.YES.key),
                                            jsonData.getString(JSONKey.NO.key))
                                    if (tradeeAccept) {
                                        val amount = gooey.getUserInteger(tradee.name
                                                + jsonData.getString(JSONKey.AMOUNT_TO_PAY.key) + selection, 0,
                                                tradee.accountBalance)
                                        val traderAccept = gooey.getUserLeftButtonPressed(
                                                trader.name + jsonData.getString(JSONKey.ACCEPT.key),
                                                jsonData.getString(JSONKey.YES.key),
                                                jsonData.getString(JSONKey.NO.key))
                                        if (traderAccept) {
                                            trader.removeFromOwnedProperties(selectedSpace, controller.game)
                                            selectedSpace.removeOwner()
                                            tradee.addToOwnedProperties(selectedSpace, controller.game)
                                            selectedSpace.setOwner(tradee)
                                            controller.cashController.payment(tradee, amount, trader)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * OfferToBuild: Metode der håndterer at tilbyde spilleren at bygge på sine
     * ejendomme.
     *
     * @param player Spiller, der tilbydes at bygge huse
     *
     * @author Anders Brandt, s185016
     */

    fun offerToBuild(player: Player) {
        val colors = HashSet<Color>()
        for (property in player.getOwnedProperties(controller.game)) {
            if (property is PropertySpace) {
                colors.add(property.color)
            }
        }
        val propertiesYouCanBuildOn = ArrayList<PropertySpace>()
        for (color in colors) {
            val matchedProperties = ArrayList<PropertySpace>()
            for (property in controller.game.getSpacesForType(PropertySpace::class)) {
                if (property.color == color) {
                    val owner = property.getOwner(controller.game)
                    if (owner != null && owner == player && !property.isMortgaged) {
                        matchedProperties.add(property)
                    } else {
                        matchedProperties.clear()
                        break
                    }
                }
            }
            propertiesYouCanBuildOn.addAll(matchedProperties)
        }
        if (!propertiesYouCanBuildOn.isEmpty()) {
            while (gooey.getUserLeftButtonPressed(jsonData.getString(JSONKey.BUILD_ON_PROPERTY.key),
                            jsonData.getString(JSONKey.YES.key), jsonData.getString(JSONKey.NO.key))) {
                val chosen = controller.view.whichPropertyDoWantToBuildOn(propertiesYouCanBuildOn)
                if (chosen != null) {
                    var max = chosen.housesBuilt + player.accountBalance / (chosen.price / 2)
                    if (max > 5)
                        max = 5
                    val houseChosen = controller.view.gui
                            .getUserInteger(jsonData.getString(JSONKey.MAX_BUILT_HOUSES.key), 0, max)
                    if (houseChosen > chosen.housesBuilt) {
                        buildHouses(chosen, houseChosen - chosen.housesBuilt)
                    } else if (houseChosen < chosen.housesBuilt) {
                        sellHouses(chosen, chosen.housesBuilt - houseChosen)
                    }
                }
            }
        }
    }

    /**
     * PlayerBroke: Metode til konkurshåndtering
     *
     * @param failure Spiller, der har en negativ balance
     *
     * @author Cecilie Krog Drejer, s185032
     */

    fun playerBroke(failure: Player) {
        val nameToSpace = HashMap<String, Space>()
        val ownedProperties = failure.getOwnedProperties(controller.game)
        while (failure.hasOverdrawnAccount() && !ownedProperties.isEmpty()) {
            val names = arrayOfNulls<String>(ownedProperties.size)
            for (i in ownedProperties.indices) {
                names[i] = ownedProperties[i].name
                nameToSpace[ownedProperties[i].name] = ownedProperties[i]
            }
            // Spilleren bliver promptet for at vælge en ejendom at bearbejde og
            // mulighederne afhænger af ejendomstypen
            var selection: Any? = null
            do {
                selection = JOptionPane.showInputDialog(null, jsonData.getString(JSONKey.CURRENTLY_BROKE.key),
                        jsonData.getString(JSONKey.PLAYER_BROKE_TITLE.key), JOptionPane.QUESTION_MESSAGE, null,
                        names, names[0])
            } while (selection == null)
            val selectionString = selection.toString()
            val selectedSpace = nameToSpace[selectionString]
            val action: String
            // Hvis den valgte ejendom er af typen PropertySpace og der ikke er bygget huse
            // på ejendommen
            if (selectedSpace is PropertySpace && selectedSpace.housesBuilt == 0) {
                action = gooey.getUserButtonPressed(jsonData.getString(JSONKey.WHAT_DO.key),
                        jsonData.getString(JSONKey.SELL_PROPERTY.key),
                        jsonData.getString(JSONKey.MORTGAGE_PROPERTY.key))
                if (action == jsonData.getString(JSONKey.SELL_PROPERTY.key)) {
                    sellToBank(selectedSpace)
                } else if (action == jsonData.getString(JSONKey.MORTGAGE_PROPERTY.key)) {
                    mortgage(selectedSpace)
                }
                // Hvis den valgte ejendom er af typen PropertySpace og der er bygget huse på
                // ejendommen
            } else if (selectedSpace is PropertySpace && selectedSpace.housesBuilt > 0) {
                val currentBuildLevel = selectedSpace.housesBuilt
                val housesToSell = gooey.getUserInteger(
                        jsonData.getString(JSONKey.HOUSES_ON_PROPERTY_CHOOSE_HOUSES.key), 0, currentBuildLevel)
                sellHouses(selectedSpace, housesToSell)
                // Hvis den valgte ejendom er af typen StationSpace
            } else if (selectedSpace is StationSpace) {
                action = gooey.getUserButtonPressed(jsonData.getString(jsonData.getString(JSONKey.WHAT_DO.key)),
                        jsonData.getString(JSONKey.SELL_PROPERTY.key),
                        jsonData.getString(JSONKey.MORTGAGE_PROPERTY.key))
                if (action == jsonData.getString(JSONKey.SELL_PROPERTY.key)) {
                    sellToBank(selectedSpace)
                } else if (action == jsonData.getString(JSONKey.MORTGAGE_PROPERTY.key)) {
                    mortgage(selectedSpace)
                }
            }
        }
        if (failure.hasOverdrawnAccount()) {
            failure.setBrokeStatus(true)
        }
    }

    /**
     * UnmortgageProperties: Afpantsæt ens ejendomme
     *
     * @param player Spiller, som har pantsatte ejendomme og tilbydes at tilbagekøbe
     * dem
     *
     * @author Cecilie Krog Drejer, s185032
     */

    fun unmortgageProperties(player: Player) {
        // Tjek at spilleren har noget at afpantsætte
        val ownedProperty = player.getOwnedProperties(controller.game)
        var mortgagedProperty: MutableList<StationSpace> = ArrayList()
        for (space in ownedProperty) {
            // Check property is mortgaged, and you can afford to unmorgage them.
            if (space.isMortgaged && space.price / 2 <= player.accountBalance)
                mortgagedProperty.add(space)
        }
        while (mortgagedProperty.size > 0 && controller.view.gui.getUserLeftButtonPressed(
                        jsonData.getString(JSONKey.UNMORTGAGE_PROPERTY.key), jsonData.getString(JSONKey.YES.key),
                        jsonData.getString(JSONKey.NO.key))) {
            val names = arrayOfNulls<String>(mortgagedProperty.size)
            val options = HashMap<Any, StationSpace>()
            for (i in mortgagedProperty.indices) {
                names[i] = mortgagedProperty[i].name
                options[mortgagedProperty[i].name] = mortgagedProperty[i]
            }
            val selection = JOptionPane.showInputDialog(null, jsonData.getString(JSONKey.CURRENTLY_BROKE.key),
                    jsonData.getString(JSONKey.PLAYER_BROKE_TITLE.key), JOptionPane.QUESTION_MESSAGE, null, names,
                    names[0])
            if (selection != null) {
                val selectedProperty = options[selection]
                if (selectedProperty != null) {
                    unmortgage(selectedProperty)
                    mortgagedProperty = ArrayList()
                    for (space in ownedProperty) {
                        // Check property is mortgaged, and you can afford to unmorgage them.
                        if (space.isMortgaged && space.price / 2 <= player.accountBalance)
                            mortgagedProperty.add(space)
                    }
                }
            }
        }
    }
}
