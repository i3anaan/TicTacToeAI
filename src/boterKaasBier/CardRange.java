package boterKaasBier;

public class CardRange {
    public enum ComparedToCard { HIGHER_THEN, EQUAL, LOWER_THEN };
    
    private int higherThen;
    private int lowerThen;
    private int isExactly;
    
    public CardRange() {
        this.higherThen = 0;
        this.lowerThen = 14;
        this.isExactly = -1;
    }
    
    public CardRange(int minCard, int maxCard) {
        //TODO remove this constructor
        this();
        addConstraint(ComparedToCard.HIGHER_THEN, minCard);
        addConstraint(ComparedToCard.LOWER_THEN, maxCard);
    }
    
    public void addConstraint(ComparedToCard compared, Card card) {
        addConstraint(compared, card.value);
    }
    
    public void addConstraint(ComparedToCard compared, int cardValue) {
        switch (compared) {
            case HIGHER_THEN:
                higherThen = cardValue;
                break;     
                
            case LOWER_THEN:
                lowerThen = cardValue;
                break;
                         
            case EQUAL:
                isExactly = cardValue;
                break;
        }
    }
    
    public boolean isInRange(Card card) {
        return isInRange(card.value);
    }
    
    public boolean isInRange(int cardValue) {
        if (isExactly < 0) {
            return cardValue < lowerThen && cardValue > higherThen;
        } else {
            return cardValue == isExactly;
        }
    }
}
