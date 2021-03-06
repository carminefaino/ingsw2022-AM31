package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This class contains the controller to manage the principal scene of the game
 */
public class MainSceneController implements Initializable {
    private final List<AnchorPane> professors = new ArrayList<>();
    private final List<GridPane> clouds = new ArrayList<>();

    //archipelagos cells
    FlowPane[] singleCellArchipelago;

    //students' images
    Image greenStudent = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentgreen.png")));
    Image redStudent = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentred.png")));
    Image yellowStudent = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentyellow.png")));
    Image pinkStudent = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentpink.png")));
    Image blueStudent = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentblue.png")));
    Image[] studentsImages = {greenStudent, redStudent, yellowStudent, pinkStudent, blueStudent};

    //towers' images
    Image blackTower = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/blacktower.png")));
    Image whiteTower = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/whitetower.png")));
    Image greyTower = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/greytower.png")));

    //assistants' images
    Image assistant1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/assistants/Assistente1.png")));
    Image assistant2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/assistants/Assistente2.png")));
    Image assistant3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/assistants/Assistente3.png")));
    Image assistant4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/assistants/Assistente4.png")));
    Image assistant5 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/assistants/Assistente5.png")));
    Image assistant6 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/assistants/Assistente6.png")));
    Image assistant7 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/assistants/Assistente7.png")));
    Image assistant8 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/assistants/Assistente8.png")));
    Image assistant9 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/assistants/Assistente9.png")));
    Image assistant10 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/assistants/Assistente10.png")));
    Image[] assistants = {assistant1, assistant2, assistant3, assistant4, assistant5, assistant6, assistant7, assistant8, assistant9, assistant10};

    //archipelagos
    @FXML
    GridPane gridArchipelagos;
    @FXML
    FlowPane arch0;
    @FXML
    FlowPane arch1;
    @FXML
    FlowPane arch2;
    @FXML
    FlowPane arch3;
    @FXML
    FlowPane arch4;
    @FXML
    FlowPane arch5;
    @FXML
    FlowPane arch6;
    @FXML
    FlowPane arch7;
    @FXML
    FlowPane arch8;
    @FXML
    FlowPane arch9;
    @FXML
    FlowPane arch10;
    @FXML
    FlowPane arch11;
    @FXML
    AnchorPane cloudPane;

    //BoardElements
    @FXML
    AnchorPane profGreen;
    @FXML
    AnchorPane profRed;
    @FXML
    AnchorPane profYellow;
    @FXML
    AnchorPane profPink;
    @FXML
    AnchorPane profBlue;
    @FXML
    GridPane studentsInDR;
    @FXML
    GridPane studentsInEntrance;

    //clouds
    @FXML
    GridPane cloud1;
    @FXML
    GridPane cloud2;
    @FXML
    GridPane cloud3;
    @FXML
    GridPane cloud4;

    //cards
    @FXML
    ImageView card1;
    @FXML
    ImageView card2;
    @FXML
    ImageView card3;
    @FXML
    ImageView card4;
    @FXML
    ImageView card5;
    @FXML
    ImageView card6;
    @FXML
    ImageView card7;
    @FXML
    ImageView card8;
    @FXML
    ImageView card9;
    @FXML
    ImageView card10;

    //coins
    @FXML
    AnchorPane coinPane;
    @FXML
    Label coinLabel;
    @FXML
    Button boardsButton;
    @FXML
    Button charactersButton;

    ImageView[] cards;

    @FXML
    TextArea messageForUser;

    @FXML
    GridPane tb;

    //This variable will store the number of students already moved somewhere from the entrance
    int numberOfMovedStudents = 0;

    int maxNumberOfMovedStudents = 0;

    int maxStepsMN = 0;
    String movedStudents = "MOVEST ";

    private int indexArchipelagoMNPosition;
    private boolean cloudClickable = false;
    private boolean cardsClickable = true;
    //Contains the first position available for each row of the dining room
    HashMap<Integer, Integer> firstPositionsAvailableDR = new HashMap<>();

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private final FXMLLoader boardSceneLoader = new FXMLLoader(getClass().getClassLoader().getResource("boardScene.fxml"));
    private final FXMLLoader characterSceneLoader = new FXMLLoader(getClass().getClassLoader().getResource("characterScene.fxml"));

    Parent rootBoard;
    Scene sceneBoard;

    Parent rootCharacter;
    Scene sceneCharacter;

    private Stage stageBoard;
    private Stage stageCharacter;

    private boolean endGame = false;
    int sizeOldDeck = 0;

    /**
     * @return the loader of board scene
     */
    public FXMLLoader getBoardSceneLoader() {
        return boardSceneLoader;
    }

    /**
     * @return the loader of character scene
     */
    public FXMLLoader getCharacterSceneLoader() {
        return characterSceneLoader;
    }

    /**
     * Add a listener in this scene
     *
     * @param pcl listener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Receives the list of archipelagos still present and shows it
     *
     * @param listOfArchipelagos received
     */
    public void printArchipelagos(List<Archipelago> listOfArchipelagos) {
        for (FlowPane f : singleCellArchipelago) {
            f.getChildren().clear();
        }
        Image motherNature = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/mothernature.png")));
        Image studentRed = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentred.png")));
        Image studentGreen = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentgreen.png")));
        Image studentYellow = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentyellow.png")));
        Image studentPink = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentpink.png")));
        Image studentBlue = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentblue.png")));
        Image[] studentColor = {studentGreen, studentRed, studentYellow, studentPink, studentBlue};
        Image whiteTower = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/whitetower.png")));
        Image blackTower = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/blacktower.png")));
        Image greyTower = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/greytower.png")));
        Image[] towerColor = {whiteTower, blackTower, greyTower};
        int k = 0;

        for (int i = 0; i < listOfArchipelagos.size(); i++) {
            //mother nature
            if (listOfArchipelagos.get(i).getIsMNPresent()) {
                ImageView mn = new ImageView(motherNature);
                mn.setFitHeight(40);
                mn.setFitWidth(40);
                singleCellArchipelago[i].getChildren().add(mn);

                indexArchipelagoMNPosition = i;
                mn.setAccessibleText("mn");
                setOnDragMNDetected(mn);
                setOnDragImageDone(mn);
            }

            //students
            for (Island island : listOfArchipelagos.get(i).getBelongingIslands()) {
                for (int s = 0; s < Constants.NUMBEROFKINGDOMS; s++) {
                    for (int t = 0; t < island.getStudentsByColor(StudsAndProfsColor.values()[s]); t++) {
                        ImageView st = new ImageView(studentColor[s]);
                        st.setFitHeight(20);
                        st.setFitWidth(20);
                        singleCellArchipelago[i].getChildren().add(st);
                    }
                }
            }
            singleCellArchipelago[i].setAccessibleText("" + listOfArchipelagos.get(i).getIdArchipelago());
            setOnDragOverArchipelago(singleCellArchipelago[i]);
            setOnDragDroppedOnArchipelago(singleCellArchipelago[i]);
            k++;

            //towers
            if (listOfArchipelagos.get(i).getOwner() != null) {
                for (int j = 0; j < listOfArchipelagos.get(i).getBelongingIslands().size(); j++) {
                    ImageView tower = new ImageView(towerColor[listOfArchipelagos.get(i).getOwner().getColorOfTowers().ordinal()]);
                    tower.setFitHeight(27);
                    tower.setFitWidth(22);
                    singleCellArchipelago[i].getChildren().add(tower);
                }
            }

            //forbidden symbol
            if (listOfArchipelagos.get(i).getIsForbidden()) {
                singleCellArchipelago[i].setStyle("-fx-background-image: url(images/archiforbidden.png); -fx-background-size: stretch; -fx-background-repeat: no-repeat;");
            } else {
                singleCellArchipelago[i].setStyle("-fx-background-image: url(images/archi.png); -fx-background-size: stretch; -fx-background-repeat: no-repeat;");
            }
        }
        for (int i = k; i < Constants.NUMBEROFISLANDS; i++) {
            singleCellArchipelago[i].setVisible(false);
        }
    }

    /**
     * Called when the user do a click-left on a student
     *
     * @param student dragged
     */
    private void setOnDragStudentDetected(ImageView student) {
        student.setOnDragDetected((MouseEvent event) -> {
            /* drag was detected, start drag-and-drop gesture*/

            /* allow any transfer mode */
            Dragboard db = student.startDragAndDrop(TransferMode.MOVE);
            //Shows an image of the student that is moveing
            db.setDragView(student.getImage());

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(student.getAccessibleText());
            content.putImage(student.getImage());
            db.setContent(content);

            event.consume();
        });
    }

    /**
     * set true if the game is finished
     *
     * @param status of the game
     */
    public void setEndGame(boolean status) {
        this.endGame = status;
    }

    /**
     * Set a drag listener on the archipelago target
     * when a drag is detected, it communicates that is valid ONLY if the event has a string
     *
     * @param target archipelago that requires the listener
     */
    private void setOnDragOverArchipelago(FlowPane target) {
        target.setOnDragOver((DragEvent event) -> {
            //data is dragged over the target
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
            }
            //accept it only if it has a string data

            event.consume();
        });
    }

    /**
     * Set a drop listener on the archipelago target
     * if mother nature is detected on a valid position, call moveMN
     * if a valid movement of a student is detected, create/add the student to the string of the movements
     *
     * @param target archipelago
     */
    private void setOnDragDroppedOnArchipelago(FlowPane target) {
        target.setOnDragDropped((DragEvent event) -> {
            /* data dropped */
            try {
                /* if there is a string data on dragboard, read it and use it */
                if (event.getDragboard().getString().equals("mn")) {
                    if (maxStepsMN == 0) {
                        //If the maxSteps is 0 it means that we are not in moveMN phase
                        event.setDropCompleted(false);
                        event.consume();
                        return;
                    }
                    int steps = calculateSteps(target.getAccessibleText());
                    if (steps > 0 && steps <= maxStepsMN) {
                        ImageView mn = new ImageView(event.getDragboard().getImage());
                        mn.setFitHeight(40);
                        mn.setFitWidth(40);
                        target.getChildren().add(mn);
                        playMoveMn(steps);
                        maxStepsMN = 0;
                        cloudClickable = true;
                        event.setDropCompleted(true);
                    } else {
                        event.setDropCompleted(false);
                    }

                    event.consume();
                    return;
                }
                if (maxNumberOfMovedStudents == 0) {
                    //If the maxNumberOfMovedStudents is 0 it means that we are not in moveST phase
                    event.setDropCompleted(false);
                    event.consume();
                    return;
                }

                ImageView st = new ImageView(event.getDragboard().getImage());
                st.setFitHeight(20);
                st.setFitWidth(20);
                target.getChildren().add(st);

                String colorMoved = convertTextNumberToColor(event.getDragboard().getString());
                String destination = target.getAccessibleText();
                moveStudents(event, colorMoved, destination);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * Set a drag listener on the Imageview image
     * when image is dragged, it become invisible
     *
     * @param image where to set drag listener
     */
    private void setOnDragImageDone(ImageView image) {
        image.setOnDragDone((DragEvent event) -> {
            /* the drag-and-drop gesture ended */
            /* if the data was successfully moved, clear it */
            if (event.getTransferMode() == TransferMode.MOVE) {
                image.setVisible(false);
            }
            event.consume();
        });
    }

    /**
     * notifies the view that a student has been moved
     *
     * @param movedStudents string to be sent
     */
    private void playMoveStudents(String movedStudents) {
        support.firePropertyChange("movedStudents", "", movedStudents);
    }

    /**
     * calculates the maximum number of steps of mother nature based
     * on the card played in the current turn
     *
     * @param mnDestination destination of mn
     * @return number of steps
     */
    private int calculateSteps(String mnDestination) {
        int indexMnDestination;
        //Find the index of the destination
        for (indexMnDestination = 0; indexMnDestination < singleCellArchipelago.length; indexMnDestination++) {
            if (singleCellArchipelago[indexMnDestination].getAccessibleText().equals(mnDestination)) {
                break;
            }
        }
        int mnSteps;
        if (indexMnDestination < indexArchipelagoMNPosition) {
            int invisibleArc = 0;
            //Count the archipelagos that are not visible between the destination and the current position
            //(They are only at the end of the singlCellArchipelago array
            for (int i = indexArchipelagoMNPosition; i < singleCellArchipelago.length; i++) {
                if (!singleCellArchipelago[i].isVisible()) invisibleArc++;
            }
            //calculate the distance from the current island to the the one that should be the last island if every island would be displayed
            //remove from this the number of arc that are not visible
            //add the index of the destination ( is like if calculating the steps from 0 to the destination)
            mnSteps = (Constants.NUMBEROFISLANDS - indexArchipelagoMNPosition) - invisibleArc + indexMnDestination;

        } else {
            mnSteps = indexMnDestination - indexArchipelagoMNPosition;
        }

        return mnSteps;
    }

    /**
     * notifies the view that mother nature has been moved
     *
     * @param mnSteps steps that mn will perform
     */
    private void playMoveMn(int mnSteps) {
        String moveMn = "MOVEMN " + mnSteps;
        support.firePropertyChange("moveMn", "", moveMn);
    }

    private void setOnClickCardListener(ImageView card) {
        card.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("clicked card");
            if (cardsClickable) {
                playCard(card.getAccessibleText());
                card.setOpacity(0.5);
            }
            event.consume();
        });
    }

    /**
     * notifies the view that a card has been played
     *
     * @param cardPower of the played card
     */
    private void playCard(String cardPower) {
        String playSelectedCard = "CARD " + cardPower;
        support.firePropertyChange("cardPlayed", "", playSelectedCard);
        cardsClickable = false;
    }

    public void setOnDragMNDetected(ImageView motherNature) {
        motherNature.setOnDragDetected((MouseEvent event) -> {
            /* drag was detected, start drag-and-drop gesture*/

            /* allow any transfer mode */
            Dragboard db = motherNature.startDragAndDrop(TransferMode.ANY);
            //Shows an image of the motherNature that is moveing
            db.setDragView(motherNature.getImage());

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(motherNature.getAccessibleText());
            content.putImage(motherNature.getImage());
            db.setContent(content);

            event.consume();
        });
    }

    /**
     * Receive this player's board and shows it in main scene
     *
     * @param receivedBoard board received
     */
    public void printMyBoard(Board receivedBoard) {
        studentsInEntrance.getChildren().clear();
        studentsInDR.getChildren().clear();
        tb.getChildren().clear();

        //towers
        int row = 0;
        int column = 0;
        for (int i = 0; i < receivedBoard.getTowersOnBoard().getNumberOfTowers(); i++) {
            ImageView t;
            switch (receivedBoard.getColorOfTower()) {
                case WHITE -> t = new ImageView(whiteTower);
                case GREY -> t = new ImageView(greyTower);
                default -> t = new ImageView(blackTower);
            }
            t.setFitHeight(40);
            t.setFitWidth(40);
            tb.add(t, column, row);
            GridPane.setHalignment(t, HPos.CENTER);
            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }

        //professor
        int prof = 0;
        for (AnchorPane i : professors) {
            i.setVisible(receivedBoard.getProfessorsTable().getHasProf(StudsAndProfsColor.values()[prof]));
            prof++;
        }

        //students in dining room
        for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
            for (int j = 0; j < receivedBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]); j++) {
                ImageView st = new ImageView(studentsImages[i]);
                st.setFitHeight(30);
                st.setFitWidth(30);
                studentsInDR.add(st, j, i);
            }
            firstPositionsAvailableDR.put(i, receivedBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]));
        }
        //Adding StackPane to every Cell in the GridPane and Adding the Target Events to each StackPane.
        for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
            for (int j = 0; j < Constants.MAXSTUDENTSINDINING; j++) {
                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(100, 100);
                setOnDragOverDiningRoom(stackPane);
                setOnDragDroppedOnDiningRoom(stackPane);
                studentsInDR.add(stackPane, j, i);
            }
        }

        //students in entrance
        column = 0;
        row = 0;
        for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
            for (int j = 0; j < receivedBoard.getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]); j++) {
                if (row == 0 && column == 0) {
                    column++;
                }
                ImageView st = new ImageView(studentsImages[i]);
                st.setFitWidth(30);
                st.setFitHeight(30);
                st.setAccessibleText("" + i);
                setOnDragStudentDetected(st);
                setOnDragImageDone(st);
                studentsInEntrance.add(st, column, row);
                column++;
                if (column == 2) {
                    column = 0;
                    row++;
                }
            }
        }
    }

    private void setOnDragOverDiningRoom(StackPane diningRoom) {
        diningRoom.setOnDragOver((DragEvent event) -> {
            /* data is dragged over the target */
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
            }
            /* accept it only if it has a string data */

            event.consume();
        });
    }

    private void setOnDragDroppedOnDiningRoom(StackPane diningRoomTarget) {
        diningRoomTarget.setOnDragDropped((DragEvent event) -> {
            /* data dropped */

            try {
                /* if there is a string data on dragboard, read it and use it */
                if (event.getDragboard().hasString()) {
                    if (maxNumberOfMovedStudents == 0) {
                        //If the maxSteps is 0 it means that we are not in moveST phase
                        event.setDropCompleted(false);
                        event.consume();
                        return;
                    }

                    //Getting the coordinate of the target on the diningRoom
                    Integer rIndex = GridPane.getRowIndex(diningRoomTarget);
                    int kingdomTarget = rIndex == null ? 0 : rIndex;

                    ImageView st = new ImageView(event.getDragboard().getImage());
                    st.setFitHeight(30);
                    st.setFitWidth(30);
                    int kingdomOfTheStudent = Integer.parseInt(event.getDragboard().getString());
                    //allow the movement only if the row is the one of the student
                    if (kingdomTarget == kingdomOfTheStudent && firstPositionsAvailableDR.get(kingdomTarget) < Constants.MAXSTUDENTSINDINING) {
                        //add(whatToAdd, column, row
                        studentsInDR.add(st, firstPositionsAvailableDR.get(kingdomTarget), kingdomOfTheStudent);
                        firstPositionsAvailableDR.put(kingdomTarget, firstPositionsAvailableDR.get(kingdomTarget) + 1);

                        String colorMoved = convertTextNumberToColor(event.getDragboard().getString());
                        String destination = "" + 0;
                        moveStudents(event, colorMoved, destination);

                    } else {
                        event.setDropCompleted(false);
                        event.consume();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void moveStudents(DragEvent event, String colorMoved, String destination) {
        movedStudents += colorMoved + "-" + destination;
        numberOfMovedStudents++;
        if (numberOfMovedStudents == maxNumberOfMovedStudents) {
            playMoveStudents(movedStudents);
            movedStudents = "MOVEST ";
            numberOfMovedStudents = 0;
            maxNumberOfMovedStudents = 0;
        } else {
            movedStudents += ",";
        }
        event.setDropCompleted(true);
        event.consume();
    }

    /**
     * Receives the list of current turn's clouds and shows it
     *
     * @param cloudList received
     */
    public void printClouds(List<Cloud> cloudList) {
        for (GridPane c : clouds) {
            c.getChildren().clear();
        }
        switch (cloudList.size()) {
            case 2: {
                cloud3.setVisible(false);
                cloud4.setVisible(false);
            }
            case 3: {
                cloud4.setVisible(false);
            }
            case 4:
        }

        int row = 0;
        int column = 0;
        for (Cloud c : cloudList) {
            if (!c.getIsTaken()) {
                for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                    for (int k = 0; k < c.getStudents()[j]; k++) {
                        ImageView st = new ImageView();
                        st.setImage(studentsImages[j]);
                        st.setFitHeight(30);
                        st.setFitWidth(30);
                        clouds.get(cloudList.indexOf(c)).add(st, column, row);
                        GridPane.setHalignment(st, HPos.CENTER);
                        column++;
                        if (column == 2) {
                            column = 0;
                            row++;
                        }
                    }
                }
                column = 0;
                row = 0;

                clouds.get(cloudList.indexOf(c)).setAccessibleText("" + c.getIdCloud());
                setOnClickCloud(clouds.get(cloudList.indexOf(c)));
            }
        }
    }

    /**
     * Add a click event listener on the GridPane cloud
     * If the player is in the cloud phase, call playCloud
     *
     * @param cloud received
     */
    private void setOnClickCloud(GridPane cloud) {
        cloud.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (cloudClickable) {
                playCloud(cloud.getAccessibleText());
                cloudClickable = false;
            }
            event.consume();
        });
    }

    /**
     * Notifies the view that a cloud has been chosen by the player
     *
     * @param cloudSelected
     */
    private void playCloud(String cloudSelected) {
        String playSelectedCloud = "CLOUD " + cloudSelected;
        support.firePropertyChange("cloudPlayed", "", playSelectedCloud);
    }

    /**
     * Set clouds clickable or not
     *
     * @param areCloudClickable
     */
    public void setCloudsClickable(boolean areCloudClickable) {
        this.cloudClickable = areCloudClickable;
    }

    /**
     * Receives this player's deck and shows it in the main scene
     *
     * @param deck
     */
    public void printDeck(Deck deck) {
        for (ImageView card : cards) {
            card.setVisible(false);
        }
        for (Card c : deck.getLeftCards()) {
            cards[deck.getLeftCards().indexOf(c)].setImage(assistants[c.getPower() - 1]);
            cards[deck.getLeftCards().indexOf(c)].setAccessibleText("" + c.getPower());
            cards[deck.getLeftCards().indexOf(c)].setVisible(true);
            setOnClickCardListener(cards[deck.getLeftCards().indexOf(c)]);
        }
        if (deck.getLeftCards().size() < sizeOldDeck) {
            for (ImageView card : cards) {
                card.setOpacity(1.0);
            }
        }
        sizeOldDeck = deck.getLeftCards().size();
    }

    /**
     * Open the board scene when the board button is pressed.
     * If the scene is already open puts it in the foreground
     */
    public void openBoardScene() {
        if (stageBoard == null) {
            stageBoard = new Stage();
            sceneBoard = new Scene(rootBoard);
            stageBoard.setScene(sceneBoard);
            Image icon = new Image(getClass().getResourceAsStream("/images/board.jpeg"));
            stageBoard.getIcons().add(icon);
            stageBoard.setTitle("Boards");
            stageBoard.setResizable(false);
        }
        if (!stageBoard.isShowing()) {
            stageBoard.show();
        } else {
            stageBoard.toFront();
        }
    }

    /**
     * Open the character scene when the board button is pressed.
     * If the scene is already open puts it in the foreground
     */
    public void openCharactersScene() {
        if (stageCharacter == null) {
            stageCharacter = new Stage();
            sceneCharacter = new Scene(rootCharacter);
            stageCharacter.setScene(sceneCharacter);
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/expertMode/CarteTOT_front2.jpg")));
            stageCharacter.getIcons().add(icon);
            stageCharacter.setTitle("Characters");
            stageCharacter.setResizable(false);
        }
        if (!stageCharacter.isShowing()) {
            stageCharacter.show();
        } else {
            stageCharacter.toFront();
        }
    }

    /**
     * Receive the number of coins of this player and update the coin label
     *
     * @param coins
     */
    public void printCoin(int coins) {
        coinPane.setVisible(true);
        coinLabel.setVisible(true);
        String coin = Integer.toString(coins);
        coinLabel.setText("x" + coin);
        charactersButton.setVisible(true);
    }

    /**
     * Override of the method initialize to set all the components of the scene
     * according to the game in progress
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image tower = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tower.png")));
        profGreen.setVisible(false);
        profRed.setVisible(false);
        profYellow.setVisible(false);
        profPink.setVisible(false);
        profBlue.setVisible(false);

        cards = new ImageView[]{card1, card2, card3, card4, card5, card6, card7, card8, card9, card10};
        singleCellArchipelago = new FlowPane[]{arch0, arch1, arch2, arch3, arch4, arch5, arch6, arch7, arch8, arch9, arch10, arch11};

        professors.add(profGreen);
        professors.add(profRed);
        professors.add(profYellow);
        professors.add(profPink);
        professors.add(profBlue);

        clouds.add(cloud1);
        clouds.add(cloud2);
        clouds.add(cloud3);
        clouds.add(cloud4);

        try {
            rootBoard = boardSceneLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            rootCharacter = characterSceneLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        coinPane.setVisible(false);
        coinLabel.setVisible(false);
        charactersButton.setVisible(false);
    }

    /**
     * Convert the number of the color of the student provided to the initial letter of the color
     */
    private String convertTextNumberToColor(String textNumberOfTheColor) {
        int numberOfTheColor = Integer.parseInt(textNumberOfTheColor);
        switch (numberOfTheColor) {
            case 0 -> {
                return "G";
            }
            case 1 -> {
                return "R";
            }
            case 2 -> {
                return "Y";
            }
            case 3 -> {
                return "P";
            }
            case 4 -> {
                return "B";
            }
        }
        return null;
    }

    /**
     * Set max number of moves mother nature can do in the current turn
     *
     * @param maxStepsMN
     */
    public void setMaxStepsMN(int maxStepsMN) {
        this.maxStepsMN = maxStepsMN;
    }

    /**
     * Set max number of students can be moved in this match
     *
     * @param maxNumberOfMovedStudents
     */
    public void setMaxNumberOfMovedStudents(int maxNumberOfMovedStudents) {
        this.maxNumberOfMovedStudents = maxNumberOfMovedStudents;
    }

    /**
     * @param isCardClickable
     */
    public void setCardsClickable(boolean isCardClickable) {
        this.cardsClickable = isCardClickable;
    }

    /**
     * Modify the message label to show the user instructions
     *
     * @param messageForUser
     */
    public void setMessageForUserText(String messageForUser) {
        if (!endGame) {
            this.messageForUser.setText(messageForUser);
        }
    }
}
