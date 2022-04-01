package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameHandlerTest {
    private GameHandler gameHandler;

    /**
     * Check if players are correctly added to the game
     */
    @Test
    void addNewPlayer() {
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3);
        gameHandler.addNewPlayer("chri", ColorOfTower.BLACK);
        gameHandler.addNewPlayer("fede", ColorOfTower.GREY);
        assertEquals(3, gameHandler.getGame().getOrderOfPlayers().size());
        assertEquals(1, gameHandler.getIsStarted());

        gameHandler = new GameHandler(pl1, 3);
        gameHandler.addNewPlayer("chri", ColorOfTower.BLACK);
        gameHandler.addNewPlayer("fede", ColorOfTower.BLACK);
        assertEquals(2, gameHandler.getGame().getOrderOfPlayers().size());
        assertEquals(0, gameHandler.getIsStarted());
    }

    /**
     * Check that if a color of a tower is already taken cannot be chosen again
     */
    @Test
    void checkColorTower() {
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3);
        gameHandler.addNewPlayer("chri", ColorOfTower.BLACK);
        assertFalse(gameHandler.checkColorTower(ColorOfTower.WHITE));
        assertTrue(gameHandler.checkColorTower(ColorOfTower.GREY));

    }

    /**
     * Check if the game is properly started when the number of players decided by the
     * first player is reached
     */
    @Test
    void startGame() {

        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3);
        assertEquals(0, gameHandler.getIsStarted());
        for(Player p: gameHandler.getGame().getOrderOfPlayers()){
           assertEquals(0, p.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        }
        gameHandler.addNewPlayer("chri", ColorOfTower.BLACK);
        assertEquals(0, gameHandler.getIsStarted());
        for(Player p: gameHandler.getGame().getOrderOfPlayers()){
            assertEquals(0, p.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        }
        gameHandler.addNewPlayer("fede", ColorOfTower.GREY);
        assertEquals(1, gameHandler.getIsStarted());
        for(Player p: gameHandler.getGame().getOrderOfPlayers()){
            assertEquals(6, p.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        }

    }

}