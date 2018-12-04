package GameAPI.Stats.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResultNotFoundException extends RuntimeException {
    public ResultNotFoundException(Integer userId){
        super("Could not find result for user " + userId);
    }
}
