package GameAPI.engine.user;

import GameAPI.engine.game.Role;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Players extends ArrayList<Player> {

    private Integer dealerPosition;
    private Integer playingPlayerPosition;

    public Players(List<Player> players, Integer dealerPosition) {
        this.dealerPosition = dealerPosition;
        addAll(players);
        initPlayingPlayerPosition();
    }

    public void attributeDealer() {
        get(playingPlayerPosition).addRole(Role.DEALER);
    }

    /**
     * Doit être réinitialisé à la fin de chaque tour (on ne veut pas réinit au début du PREFLOP car la mise des blinds fait avancer les tours)
     */
    public void initPlayingPlayerPosition() {
        playingPlayerPosition = get(dealerPosition).isEliminated() ? getNextPlayingPlayerPosition() : dealerPosition;
    }

    /**
     * Récupère le joueur dont c'est le tour de jouer
     */
    public Player getPlayingPlayer() {
        return get(playingPlayerPosition);
    }

    /**
     * Fait passer le tour au prochain joueur (qu'il soit éliminé/couché ou non)
     */
    private void passToNextPlayer(){
        playingPlayerPosition = (playingPlayerPosition + 1) % size();
    }

    /**
     * Fait passer le tour au prochain joueur et retourne la position du prochain joueur dont c'est vraiment le tour de jouer
     */
    private Integer getNextPlayingPlayerPosition() {
        passToNextPlayer();
        if(getPlayingPlayer().isIgnoredForRound()){
            return getNextPlayingPlayerPosition();
        }
        return playingPlayerPosition;
    }

    /**
     * Fait passer le tour au prochain joueur et retourne le joueur dont c'est vraiment le tour de jouer
     */
    public Player getNextPlayingPlayer() {
        Player player = get(getNextPlayingPlayerPosition());
        log.info("turn passes to " + player.getUser().getUsername());
        return player;
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
                    hashMap.putIfAbsent(player.hasFold(), new ArrayList<>());
                    hashMap.get(player.hasFold()).add(player);
                },
                HashMap::putAll
        );
    }

    public Boolean haveAllFoldExceptOne() {
        return stream().filter(player -> !player.hasFold()).count() == 1;
    }

    public Boolean haveAllPlayed() {
        return stream()
                .filter(player -> !player.isIgnoredForRound())
                .allMatch(Player::hasPlayedTurn);
    }

    public Boolean haveAllEqualBet() {
        List<Integer> players = stream().filter(player -> !player.isIgnoredForRound()).map(Player::getCurrentBet).collect(Collectors.toList());
        return players.stream().allMatch(players.get(0)::equals);
    }


}
