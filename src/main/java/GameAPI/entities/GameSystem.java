package GameAPI.entities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Classe chargée de créer les games en fonction des demandes des utilisateurs
 */
@Component
@Slf4j
public class GameSystem {

    private List<Game> games;

    public GameSystem() {
        this.games = new ArrayList<>();
    }


    /**
     * Fonction appelé lorsqu'un utilisateur clique sur 'lance une partie',
     * Fait rejoindre un user à une partie
     */
    public Game userAskForGame(User user) {
        Game game = this.findGameForUser();
        try {
            user.joinGame(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return game;
    }

    /**
     * Fonction chargée de trouver une partie pour un joueur
     *
     * Si aucune partie n'est lancée, on en crée une et on l'ajoute à la liste global des parties
     * Si il existe des parties, mais joinQueue'aucune n'est en attente, alors idem, on crée
     * Sinon, on va chercher la première partie de la liste des parties en attente
     */
    private Game findGameForUser() {
        Game game;

        if (this.games.isEmpty()) {
            game = new Game();
            this.games.add(game);
        } else {
            List<Game> waitingGames = getWaitingGames();
            if (waitingGames.size() == 0) {
                game = new Game();
                this.games.add(game);
            } else {
                game = waitingGames.get(0);
            }
        }

        return game;
    }

    /**
     * Récupère toutes les parties en attente de la liste des parties
     */
    private List<Game> getWaitingGames() {
        return games.stream().filter(game -> game.getGameStatus() == GameStatus.STARTING_PENDING).collect(Collectors.toList());
    }

    /**
     * Va chercher parmi la liste de toutes les games, celle dont l'ID correspond à celui passé en paramètre
     * Si plusieurs games ou aucune sont trouvées suite à cette recherche, on lance une exception car ce n'est pas normal
     * On retourne la première (et seule) de la liste
     */
    public Game getGameById(Integer id) {
        List<Game> resultGameList = this.games.stream()
                .filter(game -> game.getId().equals(id))
                .collect(Collectors.toList());
        if (resultGameList.size() != 1)
            throw new IllegalStateException("No game or too much game found");
        return resultGameList.get(0);
    }

}
