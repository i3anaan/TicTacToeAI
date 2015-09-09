package boterKaasBier;

import java.util.Collections;
import java.util.List;


public class Deck{
    private List<Card> cards;
    
    public void Deck() {
        for (int c = 1; c < 4; c++) {
            for (int n = 1; n < 13; n++) {
                putLast(new Card(c,n, false));
            }
        }
        shuffle();
    }
    
    public void putLast(Card card) {
        cards.add(card);
    }
    
    public Card getNext() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        } else {
            return null;
        }
    }
    
    public void shuffle() {
        Collections.shuffle(cards);
    }
}
