package GameAPI.Stats.services;

import GameAPI.Stats.entities.Result;
import GameAPI.Stats.entities.ResultStat;
import GameAPI.Stats.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StatsService {

    @Autowired
    private ResultRepository resultRepository;

    public ResultStat getStatsByUser(Integer userId){
        ResultStat resultStat = new ResultStat();

        resultStat.setTotalMoneyWon(resultRepository.getMoneyWonSumByUserId(userId));
        resultStat.setNbGamePlayed(resultRepository.getNbGamePlayed(userId));
        resultStat.setNbRoundPlayed(resultRepository.getNbRoundPlayed(userId));
        resultStat.setNbRoundWon(resultRepository.getNbRoundWon(userId));
        resultStat.setBiggestPotWon(resultRepository.getBiggestPotWon(userId));

        return resultStat;
    }

    public Result createResults(Result result) {
        return resultRepository.save(result);
    }

}
