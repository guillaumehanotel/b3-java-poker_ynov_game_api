package GameAPI.services;

import GameAPI.clients.IResultClient;
import GameAPI.engine.game.Result;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class StatsService {

    private IResultClient iResultClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(IResultClient.class))
            .logLevel(Logger.Level.FULL)
            .target(IResultClient.class, "http://localhost:8082");

    public Result getResultsByUserId(Integer id) {
        return iResultClient.getResultsByUserId(id);
    }

    public Result createResults(Integer userId, Integer gameId, Integer moneyWon, Date date, String combinaison) {
        return iResultClient.createResult(userId, gameId, moneyWon, date, combinaison);
    }

}
