package GameAPI.engine.user;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class Players extends ArrayList<Player> {

    private Integer currentOrderIndex = 0;

    public Player getPlayingPlayer() {
        return get(currentOrderIndex);
    }

    public void passToNextPlayer() {
        if (currentOrderIndex + 1 == size()) {
            currentOrderIndex = 0;
        } else {
            currentOrderIndex++;
        }
    }

    public Player getNextPlayingPlayer() {
        passToNextPlayer();
        Player player = getPlayingPlayer();
        if (player.getIsEliminated() || player.getHasDropped()) {
            return getNextPlayingPlayer();
        }
        log.info("turn passes to " + player.getUser().getUsername());
        return player;
    }

    /**
     * Récupère le joueur suivant dans l'ordre des joueurs d'un jeu
     */
    public Player getNextPlayer() {
        if (currentOrderIndex == size()) {
            currentOrderIndex = 0;
        }
        Player player = get(currentOrderIndex);
        currentOrderIndex++;
        return player;
    }

    /**
     * Récupère le joueur suivant qui n'est pas éliminé et qui n'est pas couché
     */
    public Player getNextPlaying() {
        Player player = getNextPlayer();
        if (player.getIsEliminated() || player.getHasDropped()) {
            return getNextPlaying();
        }
        return player;
    }

    /**
     * Modifie l'index de l'ordre de jeu des joueurs
     */
    public void setCurrentOrderIndex(Integer currentOrderIndex) {
        if (currentOrderIndex >= this.size()) {
            log.info("CurrentIndex too big " + currentOrderIndex + " transform to " + currentOrderIndex % size());
        }
        this.currentOrderIndex = currentOrderIndex % size();
    }

    public HashMap<PlayerStatus, List<Player>> getPlayersByResult() {
        HashMap<Boolean, List<Player>> playersByDropState = getPlayersByDropState();
        HashMap<PlayerStatus, List<Player>> players = getPlayersByCardComparisonResult(playersByDropState.get(false));
        players.get(PlayerStatus.LOOSER).addAll(playersByDropState.get(false));
        // If only one player left
        if (playersByDropState.get(false).size() == 1) {
            Player lastPlayer = playersByDropState.get(false).get(0);
            players.get(PlayerStatus.WINNER).add(lastPlayer);
        }
        return players;
    }

    private HashMap<PlayerStatus, List<Player>> getPlayersByCardComparisonResult(List<Player> playingPlayers) {
        HashMap<PlayerStatus, List<Player>> players = new HashMap<>();
        players.put(PlayerStatus.WINNER, new ArrayList<>());
        players.put(PlayerStatus.LOOSER, new ArrayList<>());
        Player winner = stream().max(Player::comparesCards).orElse(null);
        if (playingPlayers != null)
            for (Player player : playingPlayers) {
                Integer compareOutput = player.comparesCards(winner);
                if (compareOutput == 0) players.get(PlayerStatus.WINNER).add(player);
                else if (compareOutput == -1) players.get(PlayerStatus.LOOSER).add(player);
            }
        return players;
    }

    private HashMap<Boolean, List<Player>> getPlayersByDropState() {
        return stream().collect(
            HashMap::new,
            (hashMap, player) -> {
                hashMap.putIfAbsent(player.getHasDropped(), new ArrayList<>());
                hashMap.get(player.getHasDropped()).add(player);
            },
            HashMap::putAll
        );
    }

    private Boolean allPlayersHaveDroppedExceptOne() {
        return stream().filter(player -> !player.getHasDropped()).count() == 1;
    }

    private Boolean haveAllPlayed() {
        return stream()
            .filter(player -> !player.isIgnoredForRound())
            .allMatch(Player::getHasPlayedTurn);
    }

    private Boolean haveAllEqualBet() {
        return stream()
            .filter(player -> !player.isIgnoredForRound())
            .map(Player::getCurrentBet)
            .allMatch(bet -> bet.equals(get(0).getCurrentBet()));
    }

    public Boolean haveAllFinishedTurn() {
        if (allPlayersHaveDroppedExceptOne()) {
            return false;
        } else {
            return (!haveAllPlayed() || !haveAllEqualBet());
        }
    }

}
