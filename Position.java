public class Position {
    int width;
    int height;

    public Position(int w, int h){
        width = w;
        height = h;
    }

    @Override
    public String toString(){
        return "Position: " + width + " " + height;
    }
}