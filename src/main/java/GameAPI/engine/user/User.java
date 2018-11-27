package GameAPI.engine.user;

import GameAPI.engine.game.Result;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class User {

    private Integer id;
    private String email;
    private String username;
    private Integer money;
    @JsonIgnore
    Result result;

    public User(String email, String username, Integer money) {
        this.email = email;
        this.username = username;
        this.money = money;
    }

    public String getUsername() {
        return username;
    }

    public Integer getId() {
        return id;
    }

}


