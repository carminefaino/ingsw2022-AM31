package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.expertMode.Character1;
import it.polimi.ingsw.model.expertMode.Character2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests if the power of the character 2 works
 */
class Character2Test {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    GameHandler gameHandler = new GameHandler(player1, 2,false);
    Character2 character2 = new Character2(gameHandler.getGame());

    /**
     * check if correctly assigns the character to currentPlayer
     * and if correctly reports when there is a problem with coins or phase
     */
    @Test
    void usePower() {
        gameHandler.getGame().addPlayer(player2);
        character2.usePower();
        assertNotEquals(character2, gameHandler.getGame().getCurrentPlayer().getUsedCharacter());
        player1.addCoinsToWallet(20);
        character2.usePower();
        assertNotEquals(character2, gameHandler.getGame().getCurrentPlayer().getUsedCharacter());
        gameHandler.getGame().nextPhase();
        gameHandler.getGame().nextPhase();

        System.out.println(gameHandler.getGame().getPhase());
        character2.usePower();
        gameHandler.getGame().nextPhase();
        assertEquals(character2, gameHandler.getGame().getCurrentPlayer().getUsedCharacter());
        gameHandler.getGame().getCurrentPlayer().setLastUsedCard(gameHandler.getGame().getCurrentPlayer().getMyDeck().getLeftCards().get(2));
        gameHandler.getGame().moveMotherNature(4);
        assertEquals(true, gameHandler.getGame().getListOfArchipelagos().get(4).getIsMNPresent());

    }

}