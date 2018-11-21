package GameAPI.entities;

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

    public User(String email, String username, Integer money) {
        this.email = email;
        this.username = username;
        this.money = money;
    }

}


