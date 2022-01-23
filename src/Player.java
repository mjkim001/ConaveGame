
public class Player {
    protected int posX;
    protected int posY;
    protected char image;

    Player(int posX, int posY, char image) {
        this.posX = posX;
        this.posY = posY;
        this.image = image;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public char getImage() {
        return image;
    }

}
