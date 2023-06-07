import java.time.LocalDateTime;

@lombok.Data
public class GameInfo {
    private LocalDateTime startOfGame;
    private int player1MovesCount;
    private int player2MovesCount;
    private String player1;
    private String player2;
    private String winner;
}
