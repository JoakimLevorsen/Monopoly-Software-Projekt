package monopoly.controller;

import monopoly.model.Player;
import monopoly.model.spaces.PropertySpace;
import monopoly.model.spaces.StationSpace;
import monopoly.model.Game;

import java.util.List;

import gui_main.GUI;

public class PropertyController {
    private GameController gameController;
    private CashController MrMonopoly;
    private GUI gooey;
    private Game game;

    public PropertyController(GameController owner) {
        this.gameController = owner;
    }

    /*
    Auction:
	Metode til at sælge en ejendom ved auktion.
	 
	@author Anders Brandt, s185016, Cecilie Krog Drejer, s185032, Helle Achari, s180317, Joakim Bøegh Levorsen, s185023
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
			int bid = gooey.getUserInteger("Do you want to bid Player " + (currentBidderIndex + 1) + "? (enter 0 if no), current bid is " + topBid, 0, currentBidder.getAccountBalance());
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
			if (currentBidderIndex == players.size()) currentBidderIndex = 0;
		}
		if (topBidder != null) {
			gooey.showMessage("Player " + (currentBidderIndex + 1) + " won with a price of " + topBid);
			MrMonopoly.paymentToBank(topBidder, topBid);
            property.setOwner(topBidder);
            topBidder.addToOwnedProperties(property);
			return;
		}
    }

    public void auctionStation(StationSpace station) {
        gooey.showMessage("The station " + station.getName() + " will now be auctioned off.");
		int currentBidderIndex = 0;
		int topBid = 0;
		int noBidCounter = 0;
		Player topBidder = null;
		List<Player> players = game.getPlayers();
		while ((noBidCounter < players.size() - 1 && topBid != 0) || noBidCounter < players.size()) {
			Player currentBidder = players.get(currentBidderIndex);
			int bid = gooey.getUserInteger("Do you want to bid Player " + (currentBidderIndex + 1) + "? (enter 0 if no), current bid is " + topBid, 0, currentBidder.getAccountBalance());
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
			if (currentBidderIndex == players.size()) currentBidderIndex = 0;
		}
		if (topBidder != null) {
			gooey.showMessage("Player " + (currentBidderIndex + 1) + " won with a price of " + topBid);
			MrMonopoly.paymentToBank(topBidder, topBid);
            station.setOwner(topBidder);
            topBidder.addToOwnedStations(station);
			return;
		}
    }

    public void playerBroke(Player brokePlayer) {
        if (true) {}
        if (brokePlayer.getOwnedProperties() != null) {

        }
        if (brokePlayer.getOwnedStations() != null) {

        }
    }

    public void playerBrokeTo(Player player) {
        // TODO: Implementering
    }

    public void playerBrokeToBank() {
        // TODO: Implementering

    }
}
