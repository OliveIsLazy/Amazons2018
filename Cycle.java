public class Cycle {
    public String player = "Black";

    public String nextPlayer() {
        if (player == "White")
            player = "Black";
        else
            player = "White";
        return player;
    }

    public static String getOpponent(String _player) {
        if ("Black".equals(_player))
            return "White";
        else
            return "Black";
    }
}