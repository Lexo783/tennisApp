package com.dyma.tennisApp.web;

import com.dyma.tennisApp.Player;
import com.dyma.tennisApp.PlayerToSave;
import com.dyma.tennisApp.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tennis Players API")
@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Operation(summary = "Finds tennis players", description = "Returns the lists of players")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Players list",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array= @ArraySchema(schema = @Schema(implementation = Player.class))
                            )
                    }
            )
    })
    @GetMapping
    public List<Player> GetPlayers() {

        return playerService.getAllPlayers();
    }

    @Operation(summary = "Finds tennis player by these name", description = "Returns the player with a X name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Player",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "can't find player with specified lastName",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Error.class)
                            )
                    }
            )
    })
    @GetMapping("{playerName}")
    public Player GetPlayerByName(@PathVariable("playerName") String playerName) {
        return playerService.getByLastName(playerName);
    }

    @Operation(summary = "Create a tennis player", description = "Create a tennis players and return the object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Player",
                    content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class))}),
            @ApiResponse(responseCode = "400", description = "Player with specified last name already exists.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))})
    })
    @PostMapping
    public Player CreatePlayer(@RequestBody @Valid PlayerToSave player) {
        return playerService.create(player);
    }

    @Operation(summary = "Updates a player", description = "Updates a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated player",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerToSave.class))}),
            @ApiResponse(responseCode = "404", description = "Player with specified last name was not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))})

    })
    @PutMapping
    public Player updatePlayer(@RequestBody @Valid PlayerToSave playerToSave) {

        return playerService.update(playerToSave);
    }

    @Operation(summary = "Delete a tennis player", description = "Delete a tennis players by this lastName")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Player"
            ),
            @ApiResponse(responseCode = "404", description = "Player with specified last name was not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))})
    })
    @DeleteMapping("{lastName}")
    public void deletePlayerByLastName(@PathVariable("lastName") String lastName) {
        playerService.deletePlayer(lastName);
    }


}
