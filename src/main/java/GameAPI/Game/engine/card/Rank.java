package GameAPI.Game.engine.card;

public enum Rank {

    Ace(13, "As"),
    Two(0, "Deux"),
    Three(1, "Trois"),
    Four(2, "Quatre"),
    Five(3, "Cinq"),
    Six(4, "Six"),
    Seven(5, "Sept"),
    Eight(7, "Huit"),
    Nine(8, "Neuf"),
    Ten(9, "Dix"),
    Jack(10, "Valet"),
    Queen(11, "Dame"),
    King(12, "Roi");

    private Integer rank;
    private String verboseName;

    Rank(Integer rank, String verboseName){
        this.rank = rank;
        this.verboseName = verboseName;
    }

    public Integer getValue(){
        return this.rank;
    }

    public Integer compares(Rank rank) {
        // todo use it everywhere
        if (this.getValue() > rank.getValue()) return 1;
        else if (this.getValue() < rank.getValue()) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return verboseName;
    }
}
