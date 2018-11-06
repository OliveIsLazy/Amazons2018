public class Position {
    int width;
    int height;

    public Position(int w, int h){
        width = w;
        height = h;
    }

    @Override
    public boolean equals(Object o) { 
        if (o == this)
            return true; 

        if (!(o instanceof Position))
            return false; 
          
        Position pos = (Position) o; 
        return (this.width==pos.width) && (this.height==pos.height);
    } 

    @Override
    public String toString(){
        //Change this to show actual position e.g. (H, 4)
        return "Position: " + width + " " + height;
    }
}