package GameAPI.engine.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class Cards extends ArrayList<Card> {

  public Cards(Card... cards) {
    Collections.addAll(this, cards);
  }

  private LinkedHashMap<Rank, Integer> getEachRankNbr() {
    LinkedHashMap<Rank, Integer> nbrByRank = new LinkedHashMap<>();
    for (Card card : this) {
      if (!nbrByRank.containsKey(card.getRank())) {
        nbrByRank.put(card.getRank(), 0);
      }
      nbrByRank.put(card.getRank(), nbrByRank.get(card.getRank()) + 1);
    }
    return nbrByRank;
  }

  public List<Rank> getRanksByMinimumNbr(Integer minimumNbr) {
    LinkedHashMap<Rank, Integer> nbrByRank = this.getEachRankNbr(); // todo optimize
    return nbrByRank
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue() >= minimumNbr)
        // todo addAll ?
        .collect(ArrayList::new, (ranks, rankIntegerEntry) -> ranks.add(rankIntegerEntry.getKey()), ArrayList::addAll);
  }

}
