
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


class ModelTest {

    private ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private OneVsZeroModel model = new OneVsZeroModel();



    @Test
    public void testDraw(){
        System.setOut(new PrintStream(outputStreamCaptor));
        model.draw();
        assertEquals("99 99 99 \n99 99 99 \n99 99 99 \n",outputStreamCaptor.toString());
    }

    @Test
    void testDecidePlayerOrder(){
        model.decidePlayerOrder("player1","player2");
        assertNotNull(model.currentPlayer);
    }

    @Test
    void testPlaceNumber(){
        System.setOut(new PrintStream((outputStreamCaptor)));
        model.decidePlayerOrder("player1","player2");
        int current = model.players.get(model.currentPlayer);
        model.placeNumber(1,2);
        outputStreamCaptor.reset();
        model.draw();
        assertEquals("99 99 99 \n99 99 "+current + " \n99 99 99 \n",outputStreamCaptor.toString());
    }

    @Test
    void testRestart(){
        System.setOut(new PrintStream(outputStreamCaptor));
        model.restart();
        outputStreamCaptor.reset();
        model.draw();
        assertEquals("99 99 99 \n99 99 99 \n99 99 99 \n",outputStreamCaptor.toString());
    }

}
