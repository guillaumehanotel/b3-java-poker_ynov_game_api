package GameAPI.services;

import GameAPI.engine.game.Game;
import GameAPI.engine.game.GameSystem;
import GameAPI.engine.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameService {

    @Autowired
    GameSystem gameSystem;

    public Game makeUserJoinAGame(User user){
        return gameSystem.userAskForGame(user);
    }

    public GameSystem getGameSystem() {
        return gameSystem;
    }

}
