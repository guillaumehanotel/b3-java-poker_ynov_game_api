package GameAPI.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class User {

    private Long id;
    private String email;
    private String username;
    private String password;
    private Integer money;

    public User(String email, String username, String password, Integer money) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.money = money;
    }


    /**
     * Appelle la méthode d'ajout de joueur de la classe Game
     */
    public void joinGame(Game game) throws Exception {
        game.addPlayer(this);
    }



}


