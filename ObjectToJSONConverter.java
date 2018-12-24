import java.util.ArrayList;

public class ObjectToJSONConverter {

    public static String convertPawns(ArrayList<Piece> objects) {
        String json = "{";
        for (Piece pawn : objects) {
            json += "\"" + pawn.position + "\":";
            json += convertMoves(pawn.movesPool);
            json += ",";
        }
        json = json.substring(0, json.length() - 1);
        json += "}";
        return json;
    }

    private static String convertMoves(ArrayList<Move> objects) {
        String jsonObjectList = "[";
        for (Move move : objects) {
            jsonObjectList += "{\"" + move.position + "\":";
            jsonObjectList += convertPositions(move.shotsPool);
            jsonObjectList += "},";
        }
        jsonObjectList = jsonObjectList.substring(0, jsonObjectList.length() - 1);
        jsonObjectList += "]";
        return jsonObjectList;
    }

    private static String convertPositions(ArrayList<Position> objects) {
        String jsonList = "[";
        for (Position position : objects) {
            jsonList += "\"" + position.toString() + "\"";
            jsonList += ",";
        }
        jsonList = jsonList.substring(0, jsonList.length() - 1);
        jsonList += "]";
        return jsonList;
    }

}