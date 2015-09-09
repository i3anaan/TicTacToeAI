package boterKaasBier;

public class Card {
    public final int value;
    public final int color;
    public boolean open;
    
    public Card(int color, int number, boolean open) {
        this.value = number;
        this.color = color;
        this.open = open;
    }
}
