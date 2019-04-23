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
	private GameController gameController;
	private CashController MrMonopoly;
	private GUI gooey;
	private Game game;

	public PropertyController(GameController owner) {
		this.gameController = owner;
	}

	/*
	 * Auction: Metode til at sælge en ejendom ved auktion.
	 * 
	 * @author Anders Brandt, s185016
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 * 
	 * @author Helle Achari, s180317
	 * 
	 * @author Joakim Bøegh Levorsen, s185023
	 */

	public void auctionProperty(PropertySpace property) {
		gooey.showMessage("The property " + property.getName() + " will now be auctioned off.");
		int currentBidderIndex = 0;
		int topBid = 0;
		int noBidCounter = 0;
		Player topBidder = null;
		List<Player> players = game.getPlayers();
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
			MrMonopoly.paymentToBank(topBidder, topBid);
			property.setOwner(topBidder);
			topBidder.addToOwnedProperties(property);
			return;
		}
	}

	/*
	 * Auction: Metode til at sælge en station ved auktion.
	 * 
	 * @author Anders Brandt, s185016
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 * 
	 * @author Helle Achari, s180317
	 * 
	 * @author Joakim Bøegh Levorsen, s185023
	 */

	public void auctionStation(StationSpace station) {
		gooey.showMessage("The station " + station.getName() + " will now be auctioned off.");
		int currentBidderIndex = 0;
		int topBid = 0;
		int noBidCounter = 0;
		Player topBidder = null;
		List<Player> players = game.getPlayers();
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
			MrMonopoly.paymentToBank(topBidder, topBid);
			station.setOwner(topBidder);
			topBidder.addToOwnedStations(station);
			return;
		}
	}

	/*
	 * PlayerBroke: Metode til konkurshåndtering
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void playerBroke(Player failure) {
		ArrayList<PropertySpace> ownedProperties = failure.getOwnedProperties();
		ArrayList<StationSpace> ownedStations = failure.getOwnedStations();
		do {
			String[] names = new String[ownedProperties.size() + ownedStations.size()];
			for (int i = 0; i < ownedProperties.size(); i++) {
				names[i] = ownedProperties.get(i).getName();
				nameToSpace.put(names[i], ownedProperties.get(i));
			}
			for (int i = ownedProperties.size() - 1; i < names.length; i++) {
				names[i] = ownedStations.get(i).getName();
				nameToSpace.put(names[i], ownedStations.get(i));
			}
			String selection = JOptionPane.showInputDialog(null, "You are currently broke, but you own several spaces on the board. Which space do you want to process?", "Player Broke: Save Yourself", JOptionPane.QUESTION_MESSAGE, null, names, names[0]).toString();
			Space selectedSpace = nameToSpace.get(selection);
			String action;
			if (selectedSpace instanceof StationSpace) {
				action = gooey.getUserButtonPressed("You have chosen " + selection + ". What do you want to do?", "Sell station", "Mortgage station");
				if (action == "Sell station") {
					((StationSpace) selectedSpace).removeOwner(game);
					failure.removeFromOwnedStations((StationSpace) selectedSpace);
				} else if (action == "Mortgage station") {
					// TODO: implement
				}
			} else if (selectedSpace instanceof PropertySpace
					&& ((PropertySpace) selectedSpace).getHousesBuilt() == 0) {
				action = gooey.getUserButtonPressed("You have chosen " + selection + ". What do you want to do?", "Sell property", "Mortgage property");
				if (action == "Sell property") {
					((PropertySpace) selectedSpace).removeOwner(game);
					failure.removeFromOwnedProperties((PropertySpace) selectedSpace);
				} else if (action == "Mortgage property") {
					// TODO: implement
				}
			} else if (selectedSpace instanceof PropertySpace && ((PropertySpace) selectedSpace).getHousesBuilt() > 0) {
				int currentBuildLevel = ((PropertySpace) selectedSpace).getHousesBuilt();
				int housesToSell = gooey.getUserInteger("You have chosen " + selection + ". The property cannot be sold or mortgaged as there are houses on it. How many houses do you want to sell?", 0, currentBuildLevel);
				((PropertySpace) selectedSpace).setBuildLevel(currentBuildLevel - housesToSell);
			}
		} while (failure.isBroke() && (!ownedProperties.isEmpty() || !ownedStations.isEmpty()));
		if (failure.isBroke()) {
			//TODO: remove failure from game
		}
	}
}
