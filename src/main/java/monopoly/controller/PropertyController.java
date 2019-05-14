package monopoly.controller;

import monopoly.model.Player;
import monopoly.model.spaces.PropertySpace;
import monopoly.model.spaces.StationSpace;
import monopoly.model.spaces.Space;
import monopoly.model.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import gui_main.GUI;

public class PropertyController {
	private HashMap<String, Space> nameToSpace = new HashMap<>();
	private Game game;
	private GameController controller;
	private GUI gooey = controller.view.getGUI();

	public PropertyController(GameController owner) {
		this.controller = owner;
	}

	/*
	 * Auction: Metode til at sælge en ejendom ved auktion.
	 * 
	 * @author Anders Brandt, s185016
	 * @author Cecilie Krog Drejer, s185032
	 * @author Helle Achari, s180317
	 * @author Joakim Bøegh Levorsen, s185023
	 */

	public void auction(StationSpace property) {
		gooey.showMessage("The station " + property.getName() + " will now be auctioned off.");
		int currentBidderIndex = 0;
		int topBid = 0;
		int noBidCounter = 0;
		Player topBidder = null;
		List<Player> players = controller.getGame().getPlayers();
		while ((noBidCounter < players.size() - 1 && topBid != 0) || noBidCounter < players.size()) {
			Player currentBidder = players.get(currentBidderIndex);
			int bid = gooey.getUserInteger("Do you want to bid Player " + (currentBidderIndex + 1)
					+ "? (enter 0 if no), current bid is " + topBid, 0, currentBidder.getAccountBalance());
			if (bid > topBid) {
				topBid = bid;
				topBidder = currentBidder;
				noBidCounter = 0;
			} else {
				if (bid != 0) {
					gooey.showMessage("Your bid was not valid, you have been skipped!");
				}
				noBidCounter++;
			}
			currentBidderIndex++;
			if (currentBidderIndex == players.size())
				currentBidderIndex = 0;
		}
		if (topBidder != null) {
			gooey.showMessage("Player " + (currentBidderIndex + 1) + " won with a price of " + topBid);
			controller.cashController.paymentToBank(topBidder, topBid);
			property.setOwner(topBidder);
			topBidder.addToOwnedProperties(property);
			return;
		}
	}

	/*
	 * Mortgage: Metode til at pantsætte en ejendom
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void mortgage(StationSpace property) {
		if (property instanceof PropertySpace && ((PropertySpace) property).getHousesBuilt() == 0) {
			Player owner = property.getOwner(controller.getGame());
			int amount = property.getPrice() / 2;
			controller.cashController.paymentFromBank(owner, amount);
			property.setMortgaged(true);
		} else {
			
		}
	}

	/*
	 * Unmortgage: Metode til at tilbagekøbe en pantsat ejendom
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void unmortgage(StationSpace property) {
		Player owner = property.getOwner(controller.getGame());
		int amount = (int) ((property.getPrice() / 2) * 1.1);
		controller.cashController.paymentToBank(owner, amount);
		property.setMortgaged(false);
	}

	/*
	 * OfferProperty: Metode som tilbyder spilleren at købe en ejendom og evt. køber den
	 * 
	 * @author Helle Achari, s180317
	 */

    public void offerProperty(StationSpace property, Player player) {
        boolean didChoose = gooey.getUserLeftButtonPressed(" Do you want to buy this property? "
                + property.getBoardPosition() + "at the cost of: " + property.getPrice(), "Yes", "No");
        if (didChoose) {
            gooey.showMessage("Yeah, you bought the" + property.getName() + "property!");
            controller.cashController.paymentToBank(player, property.getPrice());
            property.setOwner(player);
        }
    }
	/*
	 * SellToBank: Metode til at sælge ejendom til banken
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void sellToBank(StationSpace property) {
		Player owner = property.getOwner(controller.getGame());
		if (property instanceof PropertySpace && ((PropertySpace) property).getHousesBuilt() > 0) {
			gooey.showMessage("Houses built on property must be sold in order to sell property. All houses on this property will now be sold.");
			sellHouses((PropertySpace) property, ((PropertySpace) property).getHousesBuilt());
		}
		int amount = property.getPrice() / 2;
		controller.cashController.paymentFromBank(owner, amount);
		owner.removeFromOwnedProperties(property);
		property.removeOwner(controller.getGame());
	}

	/*
	 * BuildHouses: Metode til at bygge huse på ejendomme af typen PropertySpace
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void buildHouses(PropertySpace property, int houses) {
		Player owner = property.getOwner(controller.getGame());
		int houseValue = property.getPrice() / 2;
		//Hvis man prøver at bygge så mange huse, at det samlede antal overstiger 5, bygges der op til 5 huse i alt
		if (property.getHousesBuilt() + houses <= 5) {
			int amount = houseValue * houses;
			controller.cashController.paymentToBank(owner, amount);
			property.setBuildLevel(property.getHousesBuilt() + houses);
		} else {
			int amount = houseValue * (5 - property.getHousesBuilt());
			controller.cashController.paymentToBank(owner, amount);
			property.setBuildLevel(5);
		}
	}

	/*
	 * sellHouses: Metode til at sælge huse på ejendomme af typen PropertySpace
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void sellHouses(PropertySpace property, int houses) {
		Player owner = property.getOwner(controller.getGame());
		int currentBuildLevel = property.getHousesBuilt();
		int houseValue = property.getPrice() / 2;
		//Hvis man prøver at sælge flere huse, end der er, sælges alle huse
		if (property.getHousesBuilt() >= houses) {
			int amount = (houseValue / 2) * houses;
			controller.cashController.paymentFromBank(owner, amount);
			property.setBuildLevel(currentBuildLevel - houses);
		} else {
			int amount = (houseValue / 2) * currentBuildLevel;
			controller.cashController.paymentFromBank(owner, amount);
			property.setBuildLevel(0);
		}
	}

	/*
	 * PlayerBroke: Metode til konkurshåndtering
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void playerBroke(Player failure) {
		ArrayList<StationSpace> ownedProperties = failure.getOwnedProperties();
		do {
			String[] names = new String[ownedProperties.size()];
			for (int i = 0; i < ownedProperties.size(); i++) {
				names[i] = ownedProperties.get(i).getName();
				nameToSpace.put(names[i], ownedProperties.get(i));
			}
			//Spilleren bliver promptet for at vælge en ejendom at bearbejde og mulighederne afhænger af ejendomstypen
			String selection = JOptionPane.showInputDialog(null,
					"You are currently broke, but you own several spaces on the board. Which space do you want to process?",
					"Player Broke: Save Yourself", JOptionPane.QUESTION_MESSAGE, null, names, names[0]).toString();
			Space selectedSpace = nameToSpace.get(selection);
			String action;
			//Hvis den valgte ejendom er af typen PropertySpace og der ikke er bygget huse på ejendommen
			if (selectedSpace instanceof PropertySpace && ((PropertySpace) selectedSpace).getHousesBuilt() == 0) {
				action = gooey.getUserButtonPressed("You have chosen " + selection + ". What do you want to do?", "Sell property", "Mortgage property");
				if (action == "Sell property") {
					sellToBank((PropertySpace) selectedSpace);
				} else if (action == "Mortgage property") {
					mortgage((PropertySpace) selectedSpace);
				}
			//Hvis den valgte ejendom er af typen PropertySpace og der er bygget huse på ejendommen
			} else if (selectedSpace instanceof PropertySpace && ((PropertySpace) selectedSpace).getHousesBuilt() > 0) {
				int currentBuildLevel = ((PropertySpace) selectedSpace).getHousesBuilt();
				int housesToSell = gooey.getUserInteger("You have chosen " + selection
						+ ". The property cannot be sold or mortgaged as there are houses on it. How many houses do you want to sell?",
						0, currentBuildLevel);
				sellHouses((PropertySpace) selectedSpace, housesToSell);
			//Hvis den valgte ejendom er af typen StationSpace
			} else if (selectedSpace instanceof StationSpace) {
				action = gooey.getUserButtonPressed("You have chosen " + selection + ". What do you want to do?", "Sell station", "Mortgage station");
				if (action == "Sell station") {
					sellToBank((StationSpace) selectedSpace);
				} else if (action == "Mortgage station") {
					mortgage((StationSpace) selectedSpace);
				}
			}
		} while (failure.hasOverdrawnAccount() && (!ownedProperties.isEmpty() || !ownedProperties.isEmpty()));
		if (failure.hasOverdrawnAccount()) {
			failure.setBrokeStatus(true);
		}
	}
}
