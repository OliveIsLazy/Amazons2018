import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.InputStreamReader;

class PythonAlgorithm extends Thread implements Algorithm {

    String algorithm;
    ArrayList<Piece> pawns;

    PythonAlgorithm(String _algorithm, ArrayList<Piece> _pawns) {
        super();
        this.algorithm = _algorithm;
        this.pawns = _pawns;
    }

    @Override
    public Piece pickPawn() {
        return null;
    }

    @Override
    public String findBestMove() {
        String result = communicate();
        System.out.println(result);
        if (result.equals(null)) {
            System.out.println("Something went wrong within the communication...");
            System.exit(1);
        }
        return result;
    }

    @Override
    public void run() {
        ProcessBuilder brainBuilder = new ProcessBuilder("python", "brain.py", algorithm);
        brainBuilder.redirectErrorStream(true);
        try {
            Process brain = brainBuilder.start();
            brain.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String communicate() {
        this.getInputMoves();
        ProcessBuilder communicatorBuilder = new ProcessBuilder("python", "communicator.py", Game.chessBoard.encode());
        communicatorBuilder.redirectErrorStream(true);
        String result = null;
        try {
            Process communicator = communicatorBuilder.start();
            result = readCommunicationsOutput(communicator);
            System.out.println(result);
            communicator.waitFor();
            communicator.getInputStream().close();
            communicator.getOutputStream().close();
            communicator.getErrorStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String readCommunicationsOutput(Process communicator) {
        BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(communicator.getInputStream()));
        String r, result;
        r = result = null;
        try {
            while ((r = stdoutReader.readLine()) != null)
                result = r;
        } catch (Exception e) {
            System.out.println("Something went wrong within the communication...");
        }
        System.out.println(result);
        return result;
    }

    private String getInputMoves() {
        String input = ObjectToJSONConverter.convertPawns(this.pawns);
        System.out.println(input);
        return input;
    }
}