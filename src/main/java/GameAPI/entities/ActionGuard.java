package GameAPI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Entité qui va stocker les informations relatives à la protection de la réalisation d'une action
 * C'est à dire :
 * - un booleen 'actionExpected' qui déterminera si oui ou non une action est attendu
 * - un userId qui déterminera de la part de quel utilisateur l'action attendu doit venir
 *
 * Ces informations seront modifiées dans 2 cas :
 *
 * - le cas où le jeu va finir un traitement et attendre l'action d'un joueur :
 *      - actionExpected = true
 *      - userId = Id du user dont c'est le tour
 *
 * - le cas où le jeu reçoit une requête qui satisfait les conditions pour le faire avancer :
 *      - actionExpected passe à false le temps du traitement
 *      - UserId = null
 */
@Data
@Slf4j
public class ActionGuard {

    private Boolean actionExpected;
    private Long userId;

    public ActionGuard(){
        this.actionExpected = false;
        this.userId = null;
    }

    public void expectActionFrom(Player player){
        Long userId = player.getUser().getId();
        this.actionExpected = true;
        this.userId = userId;
        log.info("[EXPECT ACTION FROM USER n°" + userId + "]");
    }

    public void forbidActions(){
        this.actionExpected = false;
        this.userId = null;
        log.info("[FORBID ACTIONS]");
    }
}
