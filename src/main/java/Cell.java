import org.tinylog.Logger;

/**
 * A class that represents a cell on the board that works like a structure for keeping the tokens.
 */
public class Cell {
    private int token = 99;

    public int getToken(){
        return token;
    }

    /**
     * Stores the token(number) inside the cell if it is empty.
     * @param token a number that is placed in the cell
     * @return a boolean if the operation was successful
     */
    public boolean placeToken(int token){
        if(this.token==99) {
            this.token=token;
            Logger.info("Token placed successfully");
            return true;
        }else{
            Logger.info("Cannot place token in the full square");
            return false;
        }


    }
}
