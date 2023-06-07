

import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.fxml.FXML;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.tinylog.Logger;
import java.util.List;

/**
 * Controller class that provides interface between UI and model.
 */
public class OneVsZeroController {
    //Music background
    Media media = new Media(getClass().getResource("minecraft.mp3").toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);

    OneVsZeroModel model;
    @FXML
    TextField player1Field;
    @FXML
    TextField player2Field;
    @FXML
    Button submitPlayerNames;
    @FXML
    BorderPane pane;
    @FXML
    GridPane statBar;

    @FXML
    /**
     * Sets up top score list in the UI and plays the music.
     */
    public void initialize(){
        List<Map.Entry<String,Integer>> list = JsonFileWriterReader.getInstance().getTopScoreList();
        for(int i = 0 ;i<list.size();i++){
            statBar.add(new Text("Name: "+list.get(i).getKey() + "\nScore: " + list.get(i).getValue()),i,0);
        }
        mediaPlayer.play();
        Logger.info("Creating the scene and calling initialize method to show the statistics");
    }

    /**
     * @author Altan Dzhumaev
     * Initialize model for further deployment. THe model has a board represented as an array that will be used for controlling the game.
     */
    public OneVsZeroController(){
        model = new OneVsZeroModel();
    }

    /**
     * Handles beginning of the game, displaying the game board and passing configuration information (player names) to the model.
     */
    public void handleIntro(){
        String player1 = player1Field.getText();
        String player2 = player2Field.getText();
        if(!player1.isBlank() && !player2.isBlank() && !player1.equals(player2)){
            model.decidePlayerOrder(player1,player2);
            displayGrid(pane);
            pane.setBottom(createButton(pane));
            Logger.info("Game started!");
        }else{
            Logger.error("Player name(s) is(are) blank or player names are identical");
        }
    }

    /**
     * Creates a game board and displays it when passed to the scene.
     * @param pane a pane object that will be used to display the grid.
     */
    private void displayGrid(BorderPane pane){
        GridPane gridpane = new GridPane();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                gridpane.add(createCell(), i, j);
        pane.setCenter(gridpane);
    }

    private Button createButton(BorderPane pane){
        Button button = new Button("Restart");
        button.setOnAction(e->{
            model.restart();
            displayGrid(pane);
        });
        return button;
    }

    /**
     * UI configuration for each cell. Sets design and size for the cell and places handle for click.
     * @return a pane which represents cell
     */
    private Pane createCell(){
        Pane pane = new Pane();
        pane.setPrefSize(150,150);
        pane.setStyle("-fx-border-color:black");
        pane.setOnMouseClicked(e->handleClick(e));
        return pane;
    }

    /**
     * Handles click on the cell, placing token based on the current player move.
     * @param event an event that activates when the user presses on the cell
     */
    private void handleClick(MouseEvent event){
        Pane pane = (Pane) event.getSource();
        int row = GridPane.getRowIndex(pane);
        int column = GridPane.getColumnIndex(pane);
        Text text = new Text();
        text.setX(pane.getHeight()/2);
        text.setY(pane.getWidth()/2);
        int token = model.placeNumber(row,column);
        if(token!=-1) {
            text.setText(String.valueOf(token));
            pane.getChildren().add(text);
        }

    }
}

