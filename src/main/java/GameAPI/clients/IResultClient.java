package GameAPI.clients;


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

    @RequestLine("GET /{userId}")
    Result getResultsByUserId(@Param("userId") Integer userId);

    @RequestLine("POST /")
    @Headers("Content-Type: application/json")
    @Body("%7B" +
            "\"userId\": \"{userId}\"" +
            "%7D")
    Result createResults(@Param("userId") Integer userId);

    @RequestLine("PUT /{userId}")
    @Headers("Content-Type: application/json")
    @Body("%7B" +
            "\"userId\": \"{userId}\", " +
            "\"nbGamesPlayed\": \"{nbGamesPlayed}\", " +
            "\"nbGamesWon\": \"{nbGamesWon}\", " +
            "\"nbRoundPlayed\": \"{nbRoundPlayed}\", " +
            "\"nbRoundWon\": \"{nbRoundWon}\", " +
            "\"totalEarnedMoney\": \"{totalEarnedMoney}\", " +
            "\"biggestPotWon\": \"{biggestPotWon}\"" +
            "%7D")
    Result updateResultsByUserId(@Param("userId") Integer userId,
                                 @Param("nbGamesPlayed") Integer nbGamesPlayed,
                                 @Param("nbGamesWon") Integer nbGamesWon,
                                 @Param("nbRoundPlayed") Integer nbRoundPlayed,
                                 @Param("nbRoundWon") Integer nbRoundWon,
                                 @Param("totalEarnedMoney") Integer totalEarnedMoney,
                                 @Param("biggestPotWon") Integer biggestPotWon);


}
