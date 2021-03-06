package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Game;

import java.io.Serializable;

/**
 * Every character has the function usePower which is called by the actionController;
 * ActionController check if a player played a character when a player use a card
 */
public abstract class Characters implements Serializable {
    Game game;
    int price;
    int id;
    boolean alreadyUsed;
    String descriptionOfPower;
    int bonusSteps = 0;
    int bonusInfluence = 0;

    public Characters(int price, Game game) {
        alreadyUsed = false;
        this.price = price;
        this.game = game;
    }

    /**
     * checks if player has enough coins in the wallet to pay for Characters
     * @return true if it has correctly taken coins, else false
     */
    public boolean payForUse() {
        if (!alreadyUsed) {
            if (game.getCurrentPlayer().getWallet() >= price) {
                game.getCurrentPlayer().removeCoinsFromWallet(price);
                game.addCoinInBank(price);
                alreadyUsed = true;
            } else {
                System.out.println("you don't have enough money to use this power");
                return false;
            }
        } else {
            if (game.getCurrentPlayer().getWallet() >= price + 1) {
                game.getCurrentPlayer().removeCoinsFromWallet(price + 1);

                game.addCoinInBank(price + 1);
            } else {
                System.out.println("you don't have enough money to use this power");
                return false;
            }
        }
        return true;
    }

    public String getDescriptionOfPower() {
        return descriptionOfPower;
    }

    public int getPrice() {
        return this.price;
    }

    public int getBonusSteps() {
        return bonusSteps;
    }

    public int getBonusInfluence() {
        return bonusInfluence;
    }

    /**
     * check if the character has already been used
     * (in case it has, the price to use it is incremented)
     * @return true if the character has already been used at least once
     */
    public boolean getAlreadyUsed() {
        return this.alreadyUsed;
    }

    public int getId() {
        return id;
    }

    /**
     * Check if an archipelago has the requirements to be merged with the previous or the next
     *
     * @param a archipelago to check
     */
    public void checkUnification(Archipelago a) {
        int index = game.getListOfArchipelagos().indexOf(a);
        int previous = index - 1;
        int next = index + 1;
        if (index == 0) {
            previous = game.getListOfArchipelagos().size() - 1;
        }
        if (index == game.getListOfArchipelagos().size() - 1) {
            next = 0;
        }

        if (a.getOwner() == game.getListOfArchipelagos().get(previous).getOwner()) {
            game.unifyArchipelagos(a, game.getListOfArchipelagos().get(previous));
        }
        if (a.getOwner() == game.getListOfArchipelagos().get(next).getOwner()) {
            game.unifyArchipelagos(a, game.getListOfArchipelagos().get(next));
        }
    }
}