package GameAPI.services;

import GameAPI.engine.Game;
import GameAPI.engine.GameSystem;
import GameAPI.engine.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameService {

    @Autowired
    GameSystem gameSystem;

    @Autowired
    UserService userService;

    public Game makeUserJoinAGame(User user){
        return gameSystem.userAskForGame(user);
    }

    public GameSystem getGameSystem() {
        return gameSystem;
    }

    public UserService getUserService() {
        return userService;
    }
}
