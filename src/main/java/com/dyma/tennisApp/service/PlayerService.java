package com.dyma.tennisApp.service;

import com.dyma.tennisApp.Player;
import com.dyma.tennisApp.PlayerList;
import com.dyma.tennisApp.PlayerToSave;
import com.dyma.tennisApp.Rank;
import com.dyma.tennisApp.data.PlayerEntity;
import com.dyma.tennisApp.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player mapPlayerEntityToPlayer(PlayerEntity playerEntity) {
        return new Player(
                playerEntity.getFirstName(),
                playerEntity.getLastName(),
                playerEntity.getBirthDate(),
                new Rank(playerEntity.getRank(), playerEntity.getPoints())
        );
    }

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
        Optional<PlayerEntity> player = playerRepository.findOneByLastNameIgnoreCase(lastName);
        if (player.isEmpty()) {
            throw new PlayerNotFoundException(lastName);
        }

        PlayerEntity playerEntity = player.get();
        return mapPlayerEntityToPlayer(playerEntity);
    }

    public Player create(PlayerToSave playerToSave) {
        Optional<PlayerEntity> playerToCreate = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());
        if (playerToCreate.isPresent()) {
            throw new PlayerAlreadyExistsException(playerToSave.lastName());
        }

        PlayerEntity playerEntity = new PlayerEntity(
                playerToSave.lastName(),
                playerToSave.firstName(),
                playerToSave.birthDate(),
                playerToSave.points(),
                999999
        );

        playerRepository.save(playerEntity);

        RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
        List<PlayerEntity> updatedPlayers = rankingCalculator.getNewPlayersRanking();
        playerRepository.saveAll(updatedPlayers);

        return getByLastName(playerEntity.getLastName());
    }

    public Player update(PlayerToSave playerToSave) {
        Optional<PlayerEntity> playerToUpdate = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());

        if (playerToUpdate.isEmpty()) {
            throw new PlayerNotFoundException(playerToSave.lastName());
        }

        playerToUpdate.get().setFirstName(playerToSave.firstName());
        playerToUpdate.get().setBirthDate(playerToSave.birthDate());
        playerToUpdate.get().setPoints(playerToSave.points());
        playerRepository.save(playerToUpdate.get());

        RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
        List<PlayerEntity> updatedPlayers = rankingCalculator.getNewPlayersRanking();
        playerRepository.saveAll(updatedPlayers);

        return getByLastName(playerToSave.lastName());
    }

    public void deletePlayer(String lastName) {
        Optional<PlayerEntity> playerToDelete = playerRepository.findOneByLastNameIgnoreCase(lastName);

        if (playerToDelete.isEmpty()) {
            throw new PlayerNotFoundException(lastName);
        }

        playerRepository.delete(playerToDelete.get());

        RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
        List<PlayerEntity> updatedPlayers = rankingCalculator.getNewPlayersRanking();
        playerRepository.saveAll(updatedPlayers);
    }
}
