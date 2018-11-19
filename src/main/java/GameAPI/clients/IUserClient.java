package GameAPI.clients;

import GameAPI.entities.User;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FeignClient(name = "UserClient", url = "http://localhost:8080/users")
public interface IUserClient {

    @RequestLine("GET /")
    List<User> getAll();

    @RequestLine("GET /{id}")
    User getOneById(@Param("id") Long id);

}
