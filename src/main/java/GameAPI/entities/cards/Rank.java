package GameAPI.entities.cards;

public enum Rank {

    Ace(13),
    Two(0),
    Three(1),
    Four(2),
    Five(3),
    Six(4),
    Seven(5),
    Eight(7),
    Nine(8),
    Ten(9),
    Jack(10),
    Queen(11),
    King(12);

    private Integer rank;

    Rank(Integer rank){
        this.rank = rank;
    }

    public Integer getValue(){
        return this.rank;
    }

}
