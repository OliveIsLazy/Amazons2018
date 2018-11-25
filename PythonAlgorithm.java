import java.io.BufferedReader;
import java.io.InputStreamReader;

class PythonAlgorithm extends Thread implements Algorithm {

    String algorithm;
    private static Board board;

    PythonAlgorithm(Board _board, String _algorithm) {
        super();
        board = _board;
        this.algorithm = _algorithm;
    }

    @Override
    public Piece pickPawn() {
        return null;
    }

    @Override
    public String findBestMove() {
        String result = communicate(board);// .encode());
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

    private String communicate(Board board) {
        ProcessBuilder communicatorBuilder = new ProcessBuilder("python", "communicator.py", board.encode());
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

    public Board getBoard() {
        return board;
    }
}