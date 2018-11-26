package GameAPI.engine.user;

import GameAPI.engine.card.Card;
import lombok.Data;

import java.util.List;

@Data
public class UserCards {

    private Integer userId;
    private List<Card> previousDownCards;

    public UserCards(Integer userId, List<Card> previousDownCards) {
        this.userId = userId;
        this.previousDownCards = previousDownCards;
    }
}
