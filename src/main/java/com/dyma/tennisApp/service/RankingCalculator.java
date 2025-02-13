package com.dyma.tennisApp.service;
import com.dyma.tennisApp.Player;
import com.dyma.tennisApp.Rank;
import com.dyma.tennisApp.data.PlayerEntity;
import java.util.ArrayList;
import java.util.List;

public class RankingCalculator {

    private final List<PlayerEntity> currentPlayersRanking;

    public RankingCalculator(List<PlayerEntity> currentPlayersRanking) {
        this.currentPlayersRanking = currentPlayersRanking;
    }

    public List<PlayerEntity> getNewPlayersRanking() {
        currentPlayersRanking.sort((p1, p2) -> Integer.compare(p2.getPoints(), p1.getPoints()));
        List<PlayerEntity> updatedPlayers = new ArrayList<>();

        for (int i = 0; i < currentPlayersRanking.size(); ++i) {
            PlayerEntity updatedPlayer = currentPlayersRanking.get(i);
            updatedPlayer.setRank(i + 1);
            updatedPlayers.add(updatedPlayer);
        }

        return updatedPlayers;
    }
}