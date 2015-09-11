package boterKaasBier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Deck{
    private List<Card> cards;
    
    public Deck() {
        cards = new ArrayList<Card>();
        
        for (int c = 1; c <= 4; c++) {
            for (int n = 1; n <= 13; n++) {
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
            throw new DeckEmptyException();
        }
    }
    
    public Card getOpenNext() {
        Card card = getNext();
        card.open = true;
        return card;
    }
    
    public void shuffle() {
        Collections.shuffle(cards);
    }
    
    public String toString() {
        return "Left: " + cards.size() + " cards.";
    }
}
