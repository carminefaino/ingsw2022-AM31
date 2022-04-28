package it.polimi.ingsw.controller;

import it.polimi.ingsw.utilities.ErrorMessage;
import it.polimi.ingsw.utilities.constants.Constants;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.expertMode.*;

import java.beans.PropertyChangeSupport;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


// Add tests!!!!!!

/**
 * Contains the methods to check that the action received from the Client via ActionParser
 * are allowed for that client; if yes, perform the action
 */
public class ActionController {
    private Phase phase;
    private Game game;
    private int isFinished;
    private ActionParser actionParser;
    private TurnController turnController;

    private PropertyChangeSupport support;

    public ActionController(Game game, TurnController turnController){
        this.game = game;
        isFinished = 0;
        actionParser = new ActionParser(this);
        this.turnController = turnController;
        this.support = new PropertyChangeSupport(this);
    }

   public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }


    public Phase getPhase() {
        return phase;
    }

    public Game getGame() {
        return game;
    }

    /**
     * finisce il turno di ogni giocatore, aggiorna la fase 2 volte per saltare quella della selezione carte
     */
    public void endPlayerTurn(){
        game.nextPhase();
        game.nextPhase();
        phase = game.getPhase();

    }

    /**
     * Calculate the influence on the archipelago where MN is present
     * Menage the tower situation of an archipelago and of the players after that
     * Call checkUnification to check if the archipelago needs to be unified to another one
     */
    public void calculateInfluence(){
        if(game.isExpertModeOn()){
            if(getCurrentPlayer().getUsedCharacter() != null && getCurrentPlayer().getUsedCharacter().getId() == CharactersEnum.CHARACTER4.ordinal()){
                Character4 character4 = new Character4(game);
                character4.calculateInfluence();
            }else if(getCurrentPlayer().getUsedCharacter() != null && getCurrentPlayer().getUsedCharacter().getId() == CharactersEnum.CHARACTER5.ordinal()){
                Character5 character5 = new Character5(game);
                character5.calculateInfluence();
            }else if(getCurrentPlayer().getUsedCharacter() != null && getCurrentPlayer().getUsedCharacter().getId() == CharactersEnum.CHARACTER6.ordinal()){
                Character6 character6 = new Character6(game);
                character6.calculateInfluence();
            }
        }else{
            for(Archipelago a : game.getListOfArchipelagos()){
                //TODO: add message "influence not calculated because forbidden"
                if(a.getIsMNPresent() && a.getIsForbidden() == false){
                    Player newOwner;
                    Player oldOwner;
                    int maxInfluence = 0;
                    if(a.getOwner() == null){
                        oldOwner = null;
                        newOwner = getCurrentPlayer();

                    }else {
                        oldOwner = a.getOwner();
                        newOwner = a.getOwner();
                        maxInfluence = a.getBelongingIslands().size();
                    }

                    for(int c = 0; c < Constants.NUMBEROFKINGDOMS; c++){
                        for(Island i : a.getBelongingIslands()){
                            if(i.getAllStudents()[c] > 0 && newOwner.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                                maxInfluence += i.getAllStudents()[c];
                            }
                        }
                    }
                    for(Player p : game.getOrderOfPlayers()){
                        if(p != newOwner){
                            int newInfluence = 0;
                            for(int c=0; c< Constants.NUMBEROFKINGDOMS; c++){
                                for(Island i: a.getBelongingIslands()){
                                    if(i.getAllStudents()[c] > 0 && p.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                                        newInfluence += i.getAllStudents()[c];
                                    }
                                }

                            }
                            if(newInfluence > maxInfluence){
                                newOwner = p;
                                maxInfluence = newInfluence;
                            }
                        }
                    }

                    if(maxInfluence > 0){
                        a.changeOwner(newOwner);
                        for(int i = 0; i < a.getBelongingIslands().size(); i++) {
                            if(oldOwner != null){
                                oldOwner.getMyBoard().getTowersOnBoard().removeTower();
                            }
                            newOwner.getMyBoard().getTowersOnBoard().removeTower();
                        }
                        checkUnification(a);
                    }
                } else if (a.getIsMNPresent() && a.getIsForbidden() == true){
                    a.setIsForbidden(false);
                }
            }
        }
    }
    /**
     * Check if an archipelago has to be unified with previous or next,
     * if yes, it calls game.unifyArchipelagos()
     * @param a archipelago to be checked
     */
    public void checkUnification(Archipelago a){
        int index = game.getListOfArchipelagos().indexOf(a);
        int previous = index - 1;
        int next = index + 1;
        if(index == 0){
            previous = game.getListOfArchipelagos().size()-1;
        }
        if(index == game.getListOfArchipelagos().size()-1) {
            next = 0;
        }

        if(a.getOwner() == game.getListOfArchipelagos().get(previous).getOwner()) {
            game.unifyArchipelagos(a, game.getListOfArchipelagos().get(previous));
        }
        if(a.getOwner() == game.getListOfArchipelagos().get(next).getOwner()) {
            game.unifyArchipelagos(a, game.getListOfArchipelagos().get(next));
        }

    }

    //TODO: leave here only the controller part of the movement?
    /**
     * Check if a movement of a student from the entrance of the player's board is allowed
     * Call assignProfessor to check if a prof needs to be added
     * (TODO: add a listener that does it when the dining room change?)
     * @param player that do the movement
     * @param colors of the students that the player wants to move
     * @param destinations of the students (0 = dining room, [1..12] number of the archipelago
     */
//TODO: check if the destination is valid
   public boolean checkActionMoveStudent(Player player,StudsAndProfsColor[] colors, int[] destinations){
       StudsAndProfsColor color;
       int destination;
       if(game.getPhase()== Phase.MOVE_STUDENTS && player == game.getCurrentPlayer()) {
           if (colors.length == turnController.getGameHandler().getNumberOfMovements()) {
               if(checkColors(player, colors)){
                   if(checkDestinations(player, colors, destinations)){
                       for (int i = 0; i < destinations.length; i++) {
                           color = colors[i];
                           destination = destinations[i];
                               if (destination == 0) {
                                       player.getMyBoard().getEntrance().removeStudent(color);
                                       player.getMyBoard().getDiningRoom().addStudent(color);
                                       if(game.isExpertModeOn()){
                                           if(player.getMyBoard().getDiningRoom().getStudentsByColor(color) % 3 == 0){
                                               if(game.getCoinFromBank(1)){
                                                   player.addCoinsToWallet(1);
                                               }else{
                                                   System.out.println("Sorry, there are not enough money in the box :(");
                                                   support.firePropertyChange("ErrorMessage" , "", ErrorMessage.EmptyBank);
                                               }
                                           }
                                       }
                                       game.assignProfessor(color);
                               } else {
                                   player.getMyBoard().getEntrance().removeStudent(color);
                                   for (Archipelago arc : game.getListOfArchipelagos()) {
                                       for (Island island : arc.getBelongingIslands()) {
                                           if (island.getIdIsland() == destination) {
                                               island.addStudent(color);
                                           }
                                       }
                                   }
                               }
                       }
                       game.nextPhase();
                       return true;
                   }else{
                       System.out.println("Destination not valid");
                       return false;
                   }
               }else{
                   System.out.println("You do not have a student of one of the color that you inserted ");
                   support.firePropertyChange("ErrorMessage" , "", ErrorMessage.StudentNotPresent );
                   return false;
               }
           }else {
               System.out.println("You can move only " + turnController.getGameHandler().getNumberOfMovements() + " students");
               support.firePropertyChange("ErrorMessage" , "", ErrorMessage.WrongNumberOfMovements + turnController.getGameHandler().getNumberOfMovements() + "students");
               return false;
           }
       }else if(game.getPhase()== Phase.MOVE_STUDENTS || player != game.getCurrentPlayer()){
               System.out.println("non è il tuo turno!!");
           support.firePropertyChange("ErrorMessage" , "", ErrorMessage.NotYourTurn );
               return false;
           }else if (game.getPhase()!= Phase.MOVE_STUDENTS){
            System.out.println("You are not in the phase " + Phase.MOVE_STUDENTS);
           support.firePropertyChange("ErrorMessage" , "", ErrorMessage.wrongPhase + game.getPhase() );

             return false;
       } else {
               System.out.println("hai inviato un'azione non valida, riprova");
               support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
               return false;
           }
   }

    //TODO: add an observer that when MN change the position calculate the influence
    /**
     * Check if a player can move MN of the steps that he sent
     * @param player that want to move MN
     * @param steps that ask to MN to do
     * @return true if the player can do the steps, false otherwise
     */
    public boolean checkActionMoveMN(Player player,int steps){
        if(game.getPhase()== Phase.MOVE_MN && player == game.getCurrentPlayer()){
                if(steps <= player.getLastUsedCard().getMaxSteps() + (player.getUsedCharacter() != null ? player.getUsedCharacter().getBonusSteps() : 0 )){
                    game.moveMotherNature(steps);
                    calculateInfluence();
                    game.nextPhase();
                    return true;
                }else {
                    System.out.println("The card that you played does not allow you to do these steps!" +
                            "(you can do max " + player.getLastUsedCard().getMaxSteps() + " steps)");
                    support.firePropertyChange("ErrorMessage", "", ErrorMessage.TooManySteps + player.getLastUsedCard().getMaxSteps() + "steps");
                    return false;
                }

        }else if(game.getPhase()== Phase.MOVE_MN || player != game.getCurrentPlayer()){
            System.out.println("non è il tuo turno!!");
            return false;
        }else{
            System.out.println("hai inviato un'azione non valida, riprova");
            support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
            return false;
        }
    }

    /**
     * Check if a cloud can be picked and do it
     * @param player that wants to pick a cloud
     * @param cloudId id of the cloud to pick
     */
    public boolean checkActionCloud(Player player,int cloudId){
        if(game.getPhase()== Phase.CLOUD_SELECTION && player == game.getCurrentPlayer()){
            if(cloudId >= 0 && cloudId < game.getListOfClouds().size()){

            for(Cloud cloud : game.getListOfClouds()){
                if (cloud.getIdCloud() == cloudId){
                    if(!cloud.getIsTaken()){
                        player.getMyBoard().getEntrance().addStudent(cloud.getStudents());
                        cloud.removeStudents();
                        game.getCurrentPlayer().setUsedCharacter(null);
                        game.nextPhase();
                        return true;
                    }else{
                        System.out.println("Cloud already taken");
                        support.firePropertyChange("ErrorMessage" , "", ErrorMessage.CloudTaken);
                        return false;
                    }
                }
                }
            }else {
                System.out.println("Number of the cloud not valid");
                support.firePropertyChange("ErrorMessage", "", ErrorMessage.DestinationNotValid);
                return false;
            }

        }else if(game.getPhase()== Phase.CLOUD_SELECTION || player != game.getCurrentPlayer()){
            System.out.println("non è il tuo turno!!");
            support.firePropertyChange("ErrorMessage" , "", ErrorMessage.NotYourTurn);
            return false;
        }else {
            System.out.println("hai inviato un'azione non valida, riprova");
            support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
            return false;
        }
        return false;
    }

    public Player getCurrentPlayer(){
        return game.getCurrentPlayer();
    }

    /**
     * Called if a player, adding the tower to an archipelago, run out of them
     */
    public void endGameImmediately(){
        isFinished = 1;
    }

    public TurnController getTurnController() {
        return turnController;
    }

    /**
     * Check that a player can use a character and use it
     * @param player of the player that is playing the character
     * @param idOfCharacter that the player want to use
     * @param action action associated with the character (e.g. number of the archipelago)
     * @return false if there is some error
     */
    public boolean checkActionCharacter(Player player, int idOfCharacter, String action) {
        if(game.isExpertModeOn()){
            if(player == game.getCurrentPlayer()){
                for(Characters c :game.getCharactersPlayable()){
                    if(c.getId() == idOfCharacter){
                        String playedCharacter = "";
                        switch (idOfCharacter) {
                            case 1 -> {
                                playedCharacter = CharactersEnum.CHARACTER1.toString();
                                for (Archipelago arc : game.getListOfArchipelagos()) {
                                    if (arc.getIdArchipelago() == Integer.parseInt(action)) {
                                        Character1 character1 = new Character1(game);
                                        if(character1.usePower(Integer.parseInt(action))){
                                            support.firePropertyChange("playedCharacter", "", playedCharacter);
                                            return true;
                                        }else{
                                            System.out.println("Error in playing character" + playedCharacter);
                                            return false;
                                        }
                                    }
                                }
                                return false;
                            }
                            case 2 -> {
                                playedCharacter = CharactersEnum.CHARACTER2.toString();
                                Character2 character2 = new Character2(game);
                                if(character2.usePower()){
                                    support.firePropertyChange("playedCharacter", "", playedCharacter);
                                    return true;
                                }else{
                                    System.out.println("Error in playing character" + playedCharacter);
                                    return false;
                                }
                            }
                            case 3 -> {
                                playedCharacter = CharactersEnum.CHARACTER3.toString();
                                for (Archipelago arc : game.getListOfArchipelagos()) {
                                    if (arc.getIdArchipelago() == Integer.parseInt(action)) {
                                        Character3 character3 = new Character3(game);
                                        if(character3.usePower(Integer.parseInt(action))){
                                            support.firePropertyChange("playedCharacter", "", playedCharacter);
                                            return true;
                                        }else{
                                            System.out.println("Error in playing character" + playedCharacter);
                                            return false;
                                        }
                                    }
                                }
                                return false;
                            }
                            case 4 -> {
                                playedCharacter = CharactersEnum.CHARACTER4.toString();
                                Character4 character4 = new Character4(game);
                                character4.usePower();
                                support.firePropertyChange("playedCharacter", "", playedCharacter);
                                return true;
                            }
                            case 5 -> {
                                playedCharacter = CharactersEnum.CHARACTER5.toString();
                                Character5 character5 = new Character5(game);
                                character5.usePower();
                                support.firePropertyChange("playedCharacter", "", playedCharacter);
                                return true;
                            }
                            case 6 -> {
                                playedCharacter = CharactersEnum.CHARACTER6.toString();
                                Character6 character6 = new Character6(game);
                                character6.usePower(StudsAndProfsColor.valueOf(action));
                                support.firePropertyChange("playedCharacter", "", playedCharacter);
                                return true;
                            }
                            case 7 -> {
                                playedCharacter = CharactersEnum.CHARACTER7.toString();
                                Character7 character7 = new Character7(game);
                                String[] colorDestination = action.split(",");
                                support.firePropertyChange("playedCharacter", "", playedCharacter);
                                return character7.usePower(StudsAndProfsColor.valueOf(colorDestination[0]), StudsAndProfsColor.valueOf(colorDestination[1]), StudsAndProfsColor.valueOf(colorDestination[2]), StudsAndProfsColor.valueOf(colorDestination[3]));
                            }
                            case 8 -> {
                                playedCharacter = CharactersEnum.CHARACTER8.toString();
                                Character8 character8 = new Character8(game);
                                character8.usePower();
                                support.firePropertyChange("playedCharacter", "", playedCharacter);
                                return true;
                            }
                            default -> {
                                System.out.println("Character not recognised");
                                return false;
                            }
                        }
                    }
                }
                System.out.println("The selected character is not available for this game");
                return false;
            }else{
                System.out.println("Is not your turn");
                return false;
            }
        }else{
            System.out.println("You are not playing in expert mode");
            return false;
        }

    }

    public ActionParser getActionParser() {
        return actionParser;
    }

    private boolean checkColors(Player p, StudsAndProfsColor[] colors){
        for(StudsAndProfsColor c : colors){
            if(p.getMyBoard().getEntrance().getStudentsByColor(c) == 0){
                return false;
            }
        }
        return true;
    }

    private boolean checkDestinations(Player p, StudsAndProfsColor[] colors, int[] destinations){
        for (int i = 0; i < destinations.length; i++) {
            StudsAndProfsColor color = colors[i];
            int destination = destinations[i];
            if (destination == 0) {
                //Check if the dining room of a color is already full
                if (p.getMyBoard().getDiningRoom().getStudentsByColor(color) == Constants.MAXSTUDENTSINDINING) {
                    support.firePropertyChange("ErrorMessage","", ErrorMessage.FullDiningRoom);
                    return false;
                }
                //check if the arc of that index exists
            }else if (!checkArchipelagoExistence(destination)) {
                support.firePropertyChange("ErrorMessage","", ErrorMessage.DestinationNotValid);
                return false;
            }
        }
        return true;
    }

    private boolean checkArchipelagoExistence(int d){
        for(Archipelago a : game.getListOfArchipelagos()){
            if(a.getIdArchipelago() == d){
                return true;
            }
        }
        return false;
    }
}
