package com.dyma.tennisApp.service;

import com.dyma.tennisApp.Player;
import com.dyma.tennisApp.PlayerList;
import com.dyma.tennisApp.PlayerToSave;
import com.dyma.tennisApp.Rank;
import com.dyma.tennisApp.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player>getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(player -> new Player(
                        player.getFirstName(),
                        player.getLastName(),
                        player.getBirthDate(),
                        new Rank(player.getRank(), player.getPoints())
                ))
                .sorted(Comparator.comparing(player -> player.rank().position()))
                .collect(Collectors.toList());
    }

    public Player getByLastName(String lastName) {
        return PlayerList.ALL.stream()
                .filter(player -> player.lastName().equals(lastName))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(lastName));
    }

    public Player create(PlayerToSave playerToSave) {
        return getPlayerNewRanking(PlayerList.ALL, playerToSave);
    }

    public Player update(PlayerToSave playerToSave) {
        getByLastName(playerToSave.lastName());

        List<Player> playersWithoutPlayerToUpdate = PlayerList.ALL.stream()
                .filter(player -> !player.lastName().equals(playerToSave.lastName()))
                .toList();

        RankingCalculator rankingCalculator = new RankingCalculator(playersWithoutPlayerToUpdate, playerToSave);
        List<Player> players = rankingCalculator.getNewPlayersRanking();

        return players.stream()
                .filter(player -> player.lastName().equals(playerToSave.lastName()))
                .findFirst().get();
    }

    public void deletePlayer(String lastName) {
        Player playerToDelete = getByLastName(lastName);
        PlayerList.ALL = PlayerList.ALL.stream()
                .filter(player -> !player.lastName().equals(lastName))
                .toList();
    }

    private Player getPlayerNewRanking(List<Player> existingPlayers, PlayerToSave playerToSave) {
        RankingCalculator rankingCalculator = new RankingCalculator(existingPlayers, playerToSave);
        List<Player> players = rankingCalculator.getNewPlayersRanking();

        return players.stream()
                .filter(player -> player.lastName().equals(playerToSave.lastName()))
                .findFirst().get();
    }


}
