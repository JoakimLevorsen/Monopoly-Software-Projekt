package monopoly.controller;

import gui_main.GUI;
import monopoly.model.Player;
import monopoly.model.spaces.PropertySpace;
import monopoly.model.spaces.Space;
import monopoly.model.spaces.StationSpace;
import org.json.JSONObject;
import resources.json.JSONKey;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class PropertyController {
	private GameController controller;
	private GUI gooey;
	private JSONObject jsonData;

	public PropertyController(GameController owner) {
		this.controller = owner;
		this.jsonData = owner.getGame().getLanguageData();
		this.gooey = owner.view.getGUI();
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

	public void auction(StationSpace property) {
		gooey.showMessage(property.getName() + jsonData.getString(JSONKey.AUCTIONED_OFF.getKey()));
		int currentBidderIndex = 0;
		int topBid = 0;
		int noBidCounter = 0;
		Player topBidder = null;
		List<Player> players = controller.getGame().getPlayers();
		while ((noBidCounter < players.size() - 1 && topBid != 0) || noBidCounter < players.size()) {
			Player currentBidder = players.get(currentBidderIndex);
			int bid = gooey.getUserInteger(currentBidder.getName() + jsonData.getString(JSONKey.BID.getKey()) + topBid,
					0, currentBidder.getAccountBalance());
			if (bid > topBid) {
				topBid = bid;
				topBidder = currentBidder;
				noBidCounter = 0;
			} else {
				if (bid != 0) {
					gooey.showMessage(jsonData.getString(JSONKey.BID_NOT_VALID.getKey()));
				}
				noBidCounter++;
			}
			currentBidderIndex++;
			if (currentBidderIndex == players.size())
				currentBidderIndex = 0;
		}
		if (topBidder != null) {
			gooey.showMessage(topBidder.getName() + jsonData.getString(JSONKey.WINNER.getKey()) + topBid);
			controller.cashController.paymentToBank(topBidder, topBid);
			property.setOwner(topBidder);
			topBidder.addToOwnedProperties(property, controller.getGame());
			return;
		}
	}

	/*
	 * Mortgage: Metode til at pantsætte en ejendom
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void mortgage(StationSpace property) {
		Player owner = property.getOwner(controller.getGame());
		int amount = property.getPrice() / 2;
		if (property instanceof PropertySpace && ((PropertySpace) property).getHousesBuilt() > 0) {
			gooey.showMessage(jsonData.getString(JSONKey.HOUSES_ON_PROPERTY_SELL_ALL_HOUSES.getKey()));
			sellHouses((PropertySpace) property, ((PropertySpace) property).getHousesBuilt());
		}
		controller.cashController.paymentFromBank(owner, amount);
		property.setMortgaged(true);
	}

	/*
	 * Unmortgage: Metode til at tilbagekøbe en pantsat ejendom
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void unmortgage(StationSpace property) {
		Player owner = property.getOwner(controller.getGame());
		int amount = (int) ((property.getPrice() / 2) * 1.1);
		if (owner.getAccountBalance() > amount) {
			controller.cashController.paymentToBank(owner, amount);
			property.setMortgaged(false);
		}
	}

	/*
	 * OfferProperty: Metode som tilbyder spilleren at købe en ejendom og evt. køber
	 * den
	 * 
	 * @author Helle Achari, s180317
	 */

	public void offerProperty(StationSpace property, Player player) {
		boolean didChoose;
		if (player.getAccountBalance() >= property.getPrice()) {
			didChoose = gooey.getUserLeftButtonPressed(
					jsonData.getString(JSONKey.OFFER_PROPERTY.getKey()) + property.getName()
							+ jsonData.getString(JSONKey.PROPERTY_COST.getKey()) + property.getPrice(),
					jsonData.getString(JSONKey.YES.getKey()), jsonData.getString(JSONKey.NO.getKey()));
		} else
			didChoose = false;
		if (didChoose) {
			gooey.showMessage(jsonData.getString(JSONKey.BOUGHT_PROPERTY.getKey()) + property.getName());
			controller.cashController.paymentToBank(player, property.getPrice());
			property.setOwner(player);
			player.addToOwnedProperties(property, controller.getGame());
		} else {
			auction(property);
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
			gooey.showMessage(jsonData.getString(JSONKey.HOUSES_ON_PROPERTY_SELL_ALL_HOUSES.getKey()));
			sellHouses((PropertySpace) property, ((PropertySpace) property).getHousesBuilt());
		}
		int amount = property.getPrice() / 2;
		controller.cashController.paymentFromBank(owner, amount);
		owner.removeFromOwnedProperties(property, controller.getGame());
		property.removeOwner(controller.getGame());
		property.setMortgaged(false);
	}

	/*
	 * BuildHouses: Metode til at bygge huse på ejendomme af typen PropertySpace
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void buildHouses(PropertySpace property, int houses) {
		Player owner = property.getOwner(controller.getGame());
		int houseValue = property.getPrice() / 2;
		// Hvis man prøver at bygge så mange huse, at det samlede antal overstiger 5,
		// bygges der op til 5 huse i alt
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
	 * SellHouses: Metode til at sælge huse på ejendomme af typen PropertySpace
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void sellHouses(PropertySpace property, int houses) {
		Player owner = property.getOwner(controller.getGame());
		int currentBuildLevel = property.getHousesBuilt();
		int houseValue = property.getPrice() / 2;
		// Hvis man prøver at sælge flere huse, end der er, sælges alle huse
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
	 * Trade: Metode til at foretage byttehandler med andre spillere. OBS: Ejendomme
	 * tages kun i bytte for penge.
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void trade(Player trader) {
		if (!trader.getOwnedProperties(controller.getGame()).isEmpty()) {
			while (gooey.getUserLeftButtonPressed(jsonData.getString(JSONKey.TRADE.getKey()),
					jsonData.getString(JSONKey.YES.getKey()), jsonData.getString(JSONKey.NO.getKey()))) {
				boolean buy = gooey.getUserLeftButtonPressed(jsonData.getString(JSONKey.BUY_OR_SELL_PROPERTY.getKey()),
						jsonData.getString(JSONKey.BUY_PROPERTY.getKey()),
						jsonData.getString(JSONKey.SELL_PROPERTY.getKey()));
				if (buy) {
					// Vælg spiller at handle med
					HashMap<String, StationSpace> nameToOwnableSpace = new HashMap<>();
					HashMap<String, Player> nameToPlayer = new HashMap<>();
					List<Player> otherPlayers = controller.getGame().getPlayers();
					otherPlayers.remove(trader);
					String[] otherPlayersNames = new String[otherPlayers.size()];
					for (int i = 0; i < otherPlayers.size(); i++) {
						otherPlayersNames[i] = otherPlayers.get(i).getName();
						nameToPlayer.put(otherPlayersNames[i], otherPlayers.get(i));
					}
					Object playerToTrade = JOptionPane.showInputDialog(null,
							jsonData.getString(JSONKey.CHOOSE_PLAYER.getKey()),
							jsonData.getString(JSONKey.BUY_PROPERTY.getKey()), JOptionPane.QUESTION_MESSAGE, null,
							otherPlayersNames, otherPlayersNames[0]);
					if (playerToTrade != null) {
						String playerToTradeString = playerToTrade.toString();
						Player tradee = nameToPlayer.get(playerToTradeString);
						// Hvis den spiller ikke har ejendom skip
						ArrayList<StationSpace> tradeeOwnedProperties = tradee.getOwnedProperties(controller.getGame());
						if (tradeeOwnedProperties.size() == 0) {
							gooey.showMessage(jsonData.getString(tradee.getName()
									+ jsonData.getString(JSONKey.PLAYER_OWNS_NO_PROPERTIES.getKey())));
						} else {
							String[] propertyNames = new String[tradeeOwnedProperties.size()];
							for (int i = 0; i < tradeeOwnedProperties.size(); i++) {
								propertyNames[i] = tradeeOwnedProperties.get(i).getName();
								nameToOwnableSpace.put(propertyNames[i], tradeeOwnedProperties.get(i));
							}
							Object selection = JOptionPane.showInputDialog(null,
									jsonData.getString(JSONKey.CHOOSE_PROPERTY.getKey()),
									jsonData.getString(JSONKey.BUY_PROPERTY.getKey()), JOptionPane.QUESTION_MESSAGE,
									null, propertyNames, propertyNames[0]);
							if (selection != null) {
								String selectionString = selection.toString();
								StationSpace selectedSpace = nameToOwnableSpace.get(selectionString);
								int amount = gooey.getUserInteger(trader.getName()
										+ jsonData.getString(JSONKey.AMOUNT_TO_PAY.getKey()) + selection, 0,
										trader.getAccountBalance());
								boolean accept = gooey.getUserLeftButtonPressed(
										tradee.getName() + jsonData.getString(JSONKey.ACCEPT.getKey()),
										jsonData.getString(JSONKey.YES.getKey()),
										jsonData.getString(JSONKey.NO.getKey()));
								if (accept) {
									if (selectedSpace instanceof PropertySpace
											&& ((PropertySpace) selectedSpace).getHousesBuilt() > 0) {
										gooey.showMessage(jsonData
												.getString(JSONKey.HOUSES_ON_PROPERTY_SELL_ALL_HOUSES.getKey()));
										sellHouses((PropertySpace) selectedSpace,
												((PropertySpace) selectedSpace).getHousesBuilt());
									}
									tradee.removeFromOwnedProperties(selectedSpace, controller.getGame());
									selectedSpace.removeOwner(controller.getGame());
									trader.addToOwnedProperties(selectedSpace, controller.getGame());
									selectedSpace.setOwner(trader);
									controller.cashController.payment(trader, amount, tradee);
								}
							}
						}
					}
				} else {
					// Tjek om denne spiller har noget ejendom at sælge.
					HashMap<String, StationSpace> nameToOwnableSpace = new HashMap<>();
					HashMap<String, Player> nameToPlayer = new HashMap<>();
					ArrayList<StationSpace> traderOwnedProperties = trader.getOwnedProperties(controller.getGame());
					if (traderOwnedProperties.size() == 0) {
						gooey.showMessage(jsonData.getString(JSONKey.YOU_OWN_NO_PROPERTIES.getKey()));
					} else {
						String[] propertyNames = new String[traderOwnedProperties.size()];
						for (int i = 0; i < traderOwnedProperties.size(); i++) {
							propertyNames[i] = traderOwnedProperties.get(i).getName();
							nameToOwnableSpace.put(propertyNames[i], traderOwnedProperties.get(i));
						}
						Object selection = JOptionPane.showInputDialog(null,
								jsonData.getString(JSONKey.CHOOSE_PROPERTY.getKey()),
								jsonData.getString(JSONKey.SELL_PROPERTY.getKey()), JOptionPane.QUESTION_MESSAGE, null,
								propertyNames, propertyNames[0]);
						if (selection != null) {
							String selectionString = selection.toString();
							StationSpace selectedSpace = nameToOwnableSpace.get(selectionString);
							if (selectedSpace instanceof PropertySpace
									&& ((PropertySpace) selectedSpace).getHousesBuilt() > 0) {
								gooey.showMessage(
										jsonData.getString(JSONKey.HOUSES_ON_PROPERTY_SELL_ALL_HOUSES.getKey()));
								sellHouses((PropertySpace) selectedSpace,
										((PropertySpace) selectedSpace).getHousesBuilt());
							}
							List<Player> otherPlayers = controller.getGame().getPlayers();
							otherPlayers.remove(trader);
							String[] otherPlayersNames = new String[otherPlayers.size()];
							for (int i = 0; i < otherPlayers.size(); i++) {
								otherPlayersNames[i] = otherPlayers.get(i).getName();
								nameToPlayer.put(otherPlayersNames[i], otherPlayers.get(i));
							}
							Object playerToTrade = JOptionPane.showInputDialog(null,
									jsonData.getString(JSONKey.CHOOSE_PLAYER.getKey()),
									jsonData.getString(JSONKey.SELL_PROPERTY.getKey()), JOptionPane.QUESTION_MESSAGE,
									null, otherPlayersNames, otherPlayersNames[0]);
							if (playerToTrade != null) {
								String playerToTradeString = playerToTrade.toString();
								Player tradee = nameToPlayer.get(playerToTradeString);
								boolean tradeeAccept = gooey.getUserLeftButtonPressed(
										tradee.getName() + jsonData.getString(JSONKey.ACCEPT.getKey()),
										jsonData.getString(JSONKey.YES.getKey()),
										jsonData.getString(JSONKey.NO.getKey()));
								if (tradeeAccept) {
									int amount = gooey.getUserInteger(tradee.getName()
											+ jsonData.getString(JSONKey.AMOUNT_TO_PAY.getKey()) + selection, 0,
											tradee.getAccountBalance());
									boolean traderAccept = gooey.getUserLeftButtonPressed(
											trader.getName() + jsonData.getString(JSONKey.ACCEPT.getKey()),
											jsonData.getString(JSONKey.YES.getKey()),
											jsonData.getString(JSONKey.NO.getKey()));
									if (traderAccept) {
										trader.removeFromOwnedProperties(selectedSpace, controller.getGame());
										selectedSpace.removeOwner(controller.getGame());
										tradee.addToOwnedProperties(selectedSpace, controller.getGame());
										selectedSpace.setOwner(tradee);
										controller.cashController.payment(tradee, amount, trader);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/*
	 * OfferToBuild: Metode der håndterer at tilbyde spilleren at bygge på sine
	 * ejendomme.
	 *
	 * @author Anders Brandt, s185016
	 */

	public void offerToBuild(Player player) {
		Set<Color> colors = new HashSet<Color>();
		for (StationSpace property : player.getOwnedProperties(controller.getGame())) {
			if (property instanceof PropertySpace) {
				colors.add(((PropertySpace) property).getColor());
			}
		}
		List<PropertySpace> propertiesYouCanBuildOn = new ArrayList<PropertySpace>();
		for (Color color : colors) {
			List<PropertySpace> matchedProperties = new ArrayList<PropertySpace>();
			for (PropertySpace property : controller.getGame().getSpacesForType(PropertySpace.class)) {
				if (property.getColor().equals(color)) {
					Player owner = property.getOwner(controller.getGame());
					if (owner != null && owner.equals(player) && !property.isMortgaged()) {
						matchedProperties.add(property);
					} else {
						matchedProperties.clear();
						break;
					}
				}
			}
			propertiesYouCanBuildOn.addAll(matchedProperties);
		}
		if (!propertiesYouCanBuildOn.isEmpty()) {
			while (gooey.getUserLeftButtonPressed(jsonData.getString(JSONKey.BUILD_ON_PROPERTY.getKey()),
					jsonData.getString(JSONKey.YES.getKey()), jsonData.getString(JSONKey.NO.getKey()))) {
				PropertySpace chosen = controller.view.whichPropertyDoWantToBuildOn(propertiesYouCanBuildOn);
				if (chosen != null) {
					int max = chosen.getHousesBuilt() + player.getAccountBalance() / (chosen.getPrice() / 2);
					if (max > 5)
						max = 5;
					int houseChosen = controller.view.getGUI()
							.getUserInteger(jsonData.getString(JSONKey.MAX_BUILT_HOUSES.getKey()), 0, max);
					if (houseChosen > chosen.getHousesBuilt()) {
						buildHouses(chosen, houseChosen - chosen.getHousesBuilt());
					} else if (houseChosen < chosen.getHousesBuilt()) {
						sellHouses(chosen, chosen.getHousesBuilt() - houseChosen);
					}
				}
			}
		}
	}

	/*
	 * PlayerBroke: Metode til konkurshåndtering
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void playerBroke(Player failure) {
		HashMap<String, Space> nameToSpace = new HashMap<>();
		ArrayList<StationSpace> ownedProperties = failure.getOwnedProperties(controller.getGame());
		do {
			String[] names = new String[ownedProperties.size()];
			for (int i = 0; i < ownedProperties.size(); i++) {
				names[i] = ownedProperties.get(i).getName();
				nameToSpace.put(names[i], ownedProperties.get(i));
			}
			// Spilleren bliver promptet for at vælge en ejendom at bearbejde og
			// mulighederne afhænger af ejendomstypen
			Object selection = null;
			do {
				selection = JOptionPane.showInputDialog(null, jsonData.getString(JSONKey.CURRENTLY_BROKE.getKey()),
						jsonData.getString(JSONKey.PLAYER_BROKE_TITLE.getKey()), JOptionPane.QUESTION_MESSAGE, null,
						names, names[0]);
			} while (selection == null);
			String selectionString = selection.toString();
			Space selectedSpace = nameToSpace.get(selectionString);
			String action;
			// Hvis den valgte ejendom er af typen PropertySpace og der ikke er bygget huse
			// på ejendommen
			if (selectedSpace instanceof PropertySpace && ((PropertySpace) selectedSpace).getHousesBuilt() == 0) {
				action = gooey.getUserButtonPressed(jsonData.getString(JSONKey.WHAT_DO.getKey()),
						jsonData.getString(JSONKey.SELL_PROPERTY.getKey()),
						jsonData.getString(JSONKey.MORTGAGE_PROPERTY.getKey()));
				if (Objects.equals(action, jsonData.getString(JSONKey.SELL_PROPERTY.getKey()))) {
					sellToBank((PropertySpace) selectedSpace);
				} else if (Objects.equals(action, jsonData.getString(JSONKey.MORTGAGE_PROPERTY.getKey()))) {
					mortgage((PropertySpace) selectedSpace);
				}
				// Hvis den valgte ejendom er af typen PropertySpace og der er bygget huse på
				// ejendommen
			} else if (selectedSpace instanceof PropertySpace && ((PropertySpace) selectedSpace).getHousesBuilt() > 0) {
				int currentBuildLevel = ((PropertySpace) selectedSpace).getHousesBuilt();
				int housesToSell = gooey.getUserInteger(
						jsonData.getString(JSONKey.HOUSES_ON_PROPERTY_CHOOSE_HOUSES.getKey()), 0, currentBuildLevel);
				sellHouses((PropertySpace) selectedSpace, housesToSell);
				// Hvis den valgte ejendom er af typen StationSpace
			} else if (selectedSpace instanceof StationSpace) {
				action = gooey.getUserButtonPressed(jsonData.getString(jsonData.getString(JSONKey.WHAT_DO.getKey())),
						jsonData.getString(JSONKey.SELL_PROPERTY.getKey()),
						jsonData.getString(JSONKey.MORTGAGE_PROPERTY.getKey()));
				if (Objects.equals(action, jsonData.getString(JSONKey.SELL_PROPERTY.getKey()))) {
					sellToBank((StationSpace) selectedSpace);
				} else if (Objects.equals(action, jsonData.getString(JSONKey.MORTGAGE_PROPERTY.getKey()))) {
					mortgage((StationSpace) selectedSpace);
				}
			}
		} while (failure.hasOverdrawnAccount() && !ownedProperties.isEmpty());
		if (failure.hasOverdrawnAccount()) {
			failure.setBrokeStatus(true);
		}
	}

	/*
	 * Unmorgage properties: Afpantsæt ens ejendomme
	 * 
	 * @author Cecilie Krog Drejer, s185032
	 */

	public void unmortgageProperties(Player player) {
		// Tjek at spilleren har noget at afpantsætte
		List<StationSpace> ownedProperty = player.getOwnedProperties(controller.getGame());
		List<StationSpace> mortgagedProperty = new ArrayList<StationSpace>();
		for (StationSpace space : ownedProperty) {
			// Check property is mortgaged, and you can afford to unmorgage them.
			if (!space.isMortgaged() && space.getPrice() / 2 <= player.getAccountBalance()) mortgagedProperty.add(space);
		}
			while (mortgagedProperty.size() > 0 && controller.view.getGUI().getUserLeftButtonPressed(jsonData.getString(JSONKey.UNMORTGAGE_PROPERTY.getKey()), jsonData.getString(JSONKey.YES.getKey()), jsonData.getString(JSONKey.NO.getKey()))) {
				String[] names = new String[mortgagedProperty.size()];
				HashMap<Object, StationSpace> options = new HashMap<Object, StationSpace>();
				for (int i = 0; i < mortgagedProperty.size(); i++) {
					names[i] = mortgagedProperty.get(i).getName();
					options.put(mortgagedProperty.get(i).getName(), mortgagedProperty.get(i));
				}
				Object selection = JOptionPane.showInputDialog(null, jsonData.getString(JSONKey.CURRENTLY_BROKE.getKey()),
				jsonData.getString(JSONKey.PLAYER_BROKE_TITLE.getKey()), JOptionPane.QUESTION_MESSAGE, null,
				names, names[0]);
				if (selection != null) {
					StationSpace selectedProperty = options.get(selection);
					selectedProperty.setMortgaged(false);
					player.changeAccountBalance(selectedProperty.getPrice() / 2);
					mortgagedProperty = new ArrayList<StationSpace>();
					for (StationSpace space : ownedProperty) {
						// Check property is mortgaged, and you can afford to unmorgage them.
						if (!space.isMortgaged() && space.getPrice() / 2 <= player.getAccountBalance()) mortgagedProperty.add(space);
					}
				}
			}
	}
}
