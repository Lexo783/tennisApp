package com.dyma.tennisApp.service;

import com.dyma.tennisApp.Player;
import com.dyma.tennisApp.PlayerToSave;
import com.dyma.tennisApp.Rank;

import java.util.ArrayList;
import java.util.List;

public class RankingCalculator {

    private final List<Player> currentPlayersRanking;
    private final PlayerToSave playerToSave;

    public RankingCalculator(List<Player> currentPlayersRanking, PlayerToSave playerToSave) {
        this.currentPlayersRanking = currentPlayersRanking;
        this.playerToSave = playerToSave;
    }

    public List<Player> getNewPlayersRanking() {
        List<Player> newRankingList = new ArrayList<>(currentPlayersRanking);
        newRankingList.add(new Player(
                playerToSave.firstName(),
                playerToSave.lastName(),
                playerToSave.birthDate(),
                new Rank(999999999, playerToSave.points())
        ));

        newRankingList.sort((player1, player2) -> Integer.compare(player2.rank().points(), player1.rank().points()));

        List<Player> updatedPlayers = new ArrayList<>();

        for (int i = 0; i < newRankingList.size(); i++) {
            Player player = newRankingList.get(i);
            Player updatedPlayer = new Player(
                    player.firstName(),
                    player.lastName(),
                    player.birthDate(),
                    new Rank(i + 1, player.rank().points())
            );
            updatedPlayers.add(updatedPlayer);
        }
        return updatedPlayers;
    }
}