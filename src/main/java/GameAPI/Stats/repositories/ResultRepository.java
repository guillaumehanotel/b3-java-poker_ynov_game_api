package GameAPI.Stats.repositories;

import GameAPI.Stats.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {

    @Query(value = "SELECT SUM(money_won) FROM `result` WHERE user_id = ?1", nativeQuery = true)
    Integer getMoneyWonSumByUserId(Integer userId);

    @Query(value = "SELECT COUNT(DISTINCT game_id) FROM `result` WHERE user_id = ?1 ", nativeQuery = true)
    Integer getNbGamePlayed(Integer userId);

    @Query(value = "SELECT COUNT(id) FROM `result` WHERE user_id = ?1", nativeQuery = true)
    Integer getNbRoundPlayed(Integer userId);

    @Query(value = "SELECT SUM(money_won > 0) FROM  result where user_id = ?1", nativeQuery = true)
    Integer getNbRoundWon(Integer userId);

    @Query(value = "SELECT MAX(money_won) FROM result where user_id = ?1", nativeQuery = true)
    Integer getBiggestPotWon(Integer userId);
}
