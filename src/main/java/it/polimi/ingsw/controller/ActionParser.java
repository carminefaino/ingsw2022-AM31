package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.utilities.ErrorMessage;
import it.polimi.ingsw.view.RemoteView;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;

/**
 * Parse a string and call actionController of that phase or turnController if is card phase
 * Message structure:
 * CARD CARDPOWER
 * MOVEST [COLOR][DESTINATION],[COLOR][DESTINATION].. (R-0,B-2,R-3)
 * MOVEMN STEPSOFMN
 * CLOUD NOFCLOUDTOPICK
 * CHARACTER [IDCHARACTER] [IDARCHIPELAGO]/ FOR CHARACTER7 : ([COLOR TO ADD],[COLOR TO ADD],[COLOR TO REMOVE],[COLOR TO REMOVE]
 */

public class ActionParser {
    private ActionController actionController;

    private PropertyChangeSupport support;

    public ActionParser(ActionController actionController){
        this.actionController = actionController;
    }

    /**
     * Serialize the message arrived from the player recognising him and sending the action to the proper method
     * @param nickname nick of the player that sent the action
     * @param message received from the player
     * @return 1 if action valid, 0 if some error occurs
     */
    public synchronized boolean actionSerializer(String nickname, String message){
        System.out.println("message: " +message);
        //Every proper action message is composed of phase+action, so if
        //a message does not contain at least a space is not fine
        if(!message.contains(" ")){
            System.out.println(ErrorMessage.ActionNotValid);
            support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
            return false;
        }
        String[] input = message.split(" ");
        String phase = input[0];
        Player player = recognisePlayer(nickname);
        if( player != null){
            switch (phase.toUpperCase(Locale.ROOT)) {
                case "CARD" -> {
                        Integer cardPower = tryParse(input[1]);
                        if(cardPower == null){
                            System.out.println(ErrorMessage.ActionNotValid);
                            support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
                            return false;
                        }
                        System.out.println("Card " + cardPower + " received");
                        return actionController.getTurnController().checkActionCard(player, cardPower);
                }
                case "MOVEST" -> {
                    if(!input[1].contains(",")){
                        System.out.println(ErrorMessage.ActionNotValid);
                        support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
                        return false;
                    }
                    String[] colorDestination = input[1].split(",");
                    StudsAndProfsColor[] colors = new StudsAndProfsColor[colorDestination.length];
                    int[] destinations = new int[colorDestination.length];
                    for (int i = 0; i < colorDestination.length; i++) {
                        if(!colorDestination[i].contains("-")){
                            System.out.println(ErrorMessage.ActionNotValid);
                            support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
                            return false;
                        }
                        colors[i] = charToColorEnum(colorDestination[i].split("-")[0].charAt(0));
                        Integer tempDestination = tryParse(colorDestination[i].split("-")[1]);
                        if(tempDestination == null || colors[i] == null){
                            System.out.println(ErrorMessage.ActionNotValid);
                            support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
                            return false;
                        }
                        destinations[i] = tempDestination;
                    }
                    return actionController.checkActionMoveStudent(player, colors, destinations);
                }
                case "MOVEMN" -> {
                    Integer mnSteps = tryParse(input[1]);
                    if(mnSteps == null){
                        System.out.println(ErrorMessage.ActionNotValid);
                        support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
                        return false;
                    }
                    return actionController.checkActionMoveMN(player, mnSteps);
                }
                case "CLOUD" -> {
                    Integer nOfCloud = tryParse(input[1]);
                    if(nOfCloud == null){
                        System.out.println(ErrorMessage.ActionNotValid);
                        support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
                        return false;
                    }
                    return actionController.checkActionCloud(player, nOfCloud);
                }
                case "CHARACTER" -> {
                    Integer idOfCharacter = tryParse(input[1]);
                    if(idOfCharacter == null){
                        System.out.println(ErrorMessage.ActionNotValid);
                        support.firePropertyChange("ErrorMessage" , "", ErrorMessage.ActionNotValid );
                        return false;
                    }
                    String action = input[2];
                    //TODO: set in action controller to check
                    return actionController.checkActionCharacter(player, idOfCharacter, action);
                }
                default -> {
                    System.out.println("Action not recognised");
                    return false;
                }
            }
        }else{
            System.out.println("Player not recognised");
            return false;
        }


    }

    private Player recognisePlayer(String nickname){
        for(Player player :actionController.getGame().getOrderOfPlayers()){
            if(player.getNickname().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    private StudsAndProfsColor charToColorEnum(char color){
        return switch (Character.toUpperCase(color)) {
            case 'R' -> StudsAndProfsColor.RED;
            case 'G' -> StudsAndProfsColor.GREEN;
            case 'Y' -> StudsAndProfsColor.YELLOW;
            case 'P' -> StudsAndProfsColor.PINK;
            case 'B' -> StudsAndProfsColor.BLUE;
            default -> null;
        };
    }

    private static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    /**
     * riceve tutti i messaggi della CLI
     * converte la stringa in FASE : AZIONE
     * fa switch case FASE=
     * per ogni caso chiama actionController.checkAction<FASE>(player, azione)
     *
     * se la fase è SCELTACARTE chiamo checkAction nel turn controller
     *
     * se la fase è muovi Studenti chiamo il metodo studentMoveParsing che riceve la stringa
     * e la trasforma in due array: il primo contiene il colore (enum/int), il secondo contiene
     * l'indice di isola/board dove va messo
     *
     * se la fase è una delle altre l'azione inviata è un intero ricavato dalla stringa
     *

     *
     *
     */

}
