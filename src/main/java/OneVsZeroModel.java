

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonGenerator;
import org.tinylog.Logger;


/**
 * Model class that has all the logic necessary for the game. Handles writing and reading through supporting classes.
 */
public class OneVsZeroModel {

    //Config for the game data.
    /**
     * Hashmap that stores players and their tokens.
     */
    public Map<String,Integer> players = new HashMap<String,Integer>();
    private String player1;
    private String player2;
    private String winner;
    /**
     * current player that will be decided by decidePlayerOrder.
     */
    public String currentPlayer;
    private int player1Count = 0;
    private int player2Count = 0;
    GameInfo gameStatistics = new GameInfo();
    private boolean gameIsNotOver = true;

    //board
    Cell[][] board = new Cell[3][3];

    /**
     * Initialize the board with empty cells(99).
     */
    public OneVsZeroModel(){
        for(int i = 0;i<3;i++){
            for(int j = 0 ; j<3;j++){
                board[i][j] = new Cell();
            }
        }
        Logger.info("Game started");
    }



    /**
     * Randomly decides which player goes first and stores it.
     * @author Altan Dzhumaev
     * @param player1 is the player's name
     * @param player2 is other player's name
     */
    public void decidePlayerOrder(String player1,String player2){
        this.player1 = player1;
        this.player2 = player2;
        gameStatistics.setStartOfGame(LocalDateTime.now());
        gameStatistics.setPlayer1(player1);
        gameStatistics.setPlayer2(player2);
        int randomDecision = (int)Math.round(Math.random());
        players.put(player1,Integer.valueOf(randomDecision));
        players.put(player2,Integer.valueOf(1-randomDecision));
        randomDecision = (int)Math.round(Math.random());
        if(randomDecision==0){
            currentPlayer = player1;
        }else{
            currentPlayer = player2;
        }
        Logger.info("The first player is {}", currentPlayer);
        Logger.info("The start time of the game is {}",LocalDateTime.now());
    }

    /**
     * Restarts the game, flushing the board but the player data stays the same.
     */
    public void restart(){
        decidePlayerOrder(player1,player2);
        gameIsNotOver = true;
        winner = null;
        for(int i = 0;i<3;i++){
            for(int j = 0 ; j<3;j++){
                board[i][j] = new Cell();
            }
        }
        Logger.info("Game restarted!");
    }


    /**
     * Draws the current state of the board where 99 is empty,0 is 0 and 1 is 1.
     * @author Altan Dzhumaev
     */
    public void draw(){
        for(int i =0 ; i<board.length;i++){
            for(int j = 0 ;j<board.length;j++){
                System.out.print(board[i][j].getToken() + " ");
            }
            System.out.print("\n");
        }
    }

    /**
     * Places number depending on the order of the players.
     * Also checks if the game is over or who is the winner if the game ended with someone's win.
     * Stores the data in a file when the winning condition is met.
     * @author Altan Dzhumaev
     * @param x is a x coordinate on the board
     * @param y is a y coordinate on the board
     * @return a number that was placed on the given coordinates(the number of the current player)
     */
    public int placeNumber(int x,int y) {
        if (!gameIsNotOver) {
            Logger.debug("Game is over - game is stopped");
            return -1;
        }
        if (board[x][y].placeToken(Integer.valueOf(players.get(currentPlayer)))) {

            if (checkIsWin(x,y)) {
                winner = currentPlayer;
                Logger.info("Winner is {}",winner);
                gameIsNotOver = false;
                gameStatistics.setWinner(winner);

            }
            if (checkIsFull()) {
                Logger.info("Draw - table is full and none of the players win");
                gameIsNotOver = false;
            }
            if (currentPlayer.equals(player1)) {
                currentPlayer = player2;
                player1Count++;
            } else {
                currentPlayer = player1;
                player2Count++;
            }
            draw();
            gameStatistics.setPlayer1MovesCount(player1Count);
            gameStatistics.setPlayer2MovesCount(player2Count);
            if(!gameIsNotOver) {
                JsonFileWriterReader.getInstance().appendToList(gameStatistics);
            }
            return board[x][y].getToken();
        }else{
            return -1;
        }

    }

    /**
     * Checks if the board is full and if it is full then the game is over.
     * @author Altan Dzhumaev
     * @return a boolean value that indicates if the board is full
     */
    private boolean checkIsFull () {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getToken() == 99) {
                    return false;
                }
            }
        }
        Logger.debug("Game board is full");
        return true;
    }



    /**
     * Checks if the game is won by any of the players. If yes, the last player who did the move wins.
     * @author Altan Dzhumaev
     * @param coordinateX an x coordinate on the board
     * @param coordinateY a y coordinate on the board
     * @return a boolean that indicates if the game is won or not
     */
    private boolean checkIsWin (int coordinateX,int coordinateY) {
        int row;
        int column;
        int sum = 0;
        for (row = 0; row < 3; row++) {
            if (board[row][coordinateY].getToken() == 99) {
                break;
            }
            sum += board[row][coordinateY].getToken();
        }
        if ((sum == 0 || sum == 1 || sum == 3) && row == 3) {
            return true;
        }

        sum = 0;
        for (column = 0; column < 3; column++) {
            if (board[coordinateX][column].getToken() == 99) {
                break;
            }
            sum += board[coordinateX][column].getToken();
        }
        if ((sum == 0 || sum == 1 || sum == 3) && column == 3) {
            return true;
        }
        return false;
    }
}
