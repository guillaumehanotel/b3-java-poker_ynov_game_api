package GameAPI.services;

import GameAPI.clients.IUserClient;
import GameAPI.entities.User;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private IUserClient iUserClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(String.class))
            .logLevel(Logger.Level.FULL)
            .target(IUserClient.class, "http://localhost:1337/user");

    public User getOneById(Long id){
        return iUserClient.getOneById(id);
    }

    public List<User> getAll(){
        return iUserClient.getAll();
    }

}
