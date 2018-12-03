package GameAPI.clients;

import GameAPI.stat.ResultStat;
import GameAPI.engine.game.Result;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("ResultClient")
public interface IResultClient {

    @RequestLine("GET /stats/users/{userId}")
    ResultStat getStatsByUser(@Param("userId") Integer userId);

    @RequestLine("POST /results")
    @Headers("Content-Type: application/json")
    @Body("%7B" +
            "\"userId\": \"{userId}\", " +
            "\"gameId\": \"{gameId}\", " +
            "\"moneyWon\": \"{moneyWon}\", " +
            "\"combinaison\": \"{combinaison}\" " +
            "%7D")
    Result createResult(@Param("userId") Integer userId,
                        @Param("gameId") Integer gameId,
                        @Param("moneyWon") Integer moneyWon,
                        @Param("combinaison") String combinaison);


}
