package org.FoxysMods.death_swap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class StopLogic {
    public static void stop(MinecraftServer server) {
        Death_swap.isActive = false;
        DeathSwapState state = DeathSwapState.getState(server);
        var playerList = server.getPlayerManager().getPlayerList().stream()
                .filter(p->state.whitelist.contains(p.getName().getString()))
                .collect(Collectors.toCollection(ArrayList::new));
        for (ServerPlayerEntity player : playerList) {
            player.changeGameMode(GameMode.DEFAULT);
            player.teleport(
                    server.getOverworld(),
                    server.getOverworld().getSpawnPos().getX() + 0.5,
                    server.getOverworld().getSpawnPos().getY(),
                    server.getOverworld().getSpawnPos().getZ() + 0.5,
                    0, 0
            );
        }
    }
}