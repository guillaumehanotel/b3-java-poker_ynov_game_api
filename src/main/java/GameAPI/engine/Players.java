package GameAPI.engine;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class Players extends ArrayList<Player> {

    private Integer currentOrderIndex = 0;

    public Player getPlayingPlayer(){
        return get(currentOrderIndex);
    }

    public void passToNextPlayer(){
        if (currentOrderIndex+1 == size()) {
            currentOrderIndex = 0;
        } else {
            currentOrderIndex++;
        }
    }

    public Player getNextPlayingPlayer(){
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
    public Player getNextPlayer(){
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
    public Player getNextPlaying(){
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
            throw new RuntimeException("CurrentIndex too big");
        }
        this.currentOrderIndex = currentOrderIndex;
    }

    HashMap<PlayerStatus, List<Player>> getPlayersByResult() {
        System.out.println("Players.getPlayersByResult");
        HashMap<PlayerStatus, List<Player>> players = new HashMap<>();
        players.put(PlayerStatus.WINNER, new ArrayList<>());
        players.put(PlayerStatus.LOOSER, new ArrayList<>());
        Player winner = stream().max(Player::comparesCards).orElse(null);
        System.out.println("winner" + winner);
        System.out.println(winner);
//        players.get(PlayerStatus.WINNER).add(winner);
        for (Player player : this) {
            System.out.println("Player " + player);
            Integer compareOutput = player.comparesCards(winner);
            System.out.println(compareOutput);
            if (compareOutput == 0) players.get(PlayerStatus.WINNER).add(player);
            else if (compareOutput == -1) players.get(PlayerStatus.LOOSER).add(player);
        }
        System.out.println(players.get(PlayerStatus.WINNER));
        return players;
    }

}
