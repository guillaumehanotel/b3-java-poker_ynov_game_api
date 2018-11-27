package GameAPI.clients;

import GameAPI.engine.game.Result;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@FeignClient("ResultClient")
public interface IResultClient {

    @RequestLine("GET /results/users/{userId}")
    Result getResultsByUserId(@Param("userId") Integer userId);

    @RequestLine("GET /results/games/{gameId}")
    Result getResultsByGameId(@Param("gameId") Integer userId);

    @RequestLine("POST /results")
    @Headers("Content-Type: application/json")
    @Body("%7B" +
            "\"userId\": \"{userId}\", " +
            "\"gameId\": \"{gameId}\", " +
            "\"money_won\": \"{money_won}\", " +
            "\"date\": \"{date}\", " +
            "\"combinaison\": \"{combinaison}\" " +
            "%7D")
    Result createResult(@Param("userId") Integer userId,
                        @Param("gameId") Integer gameId,
                        @Param("money_won") Integer moneyWon,
                        @Param("date") String date,
                        @Param("combinaison") String combinaison);

}
