package org.FoxysMods.death_swap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;


public class WinnerLogic {

    public static void checkForWinner(MinecraftServer server) {
        if (!Death_swap.isActive) return;

        var alivePlayers = server.getPlayerManager().getPlayerList().stream()
                .filter(player -> !player.isSpectator())
                .toList();

        if (alivePlayers.size() == 1) {
            ServerPlayerEntity winner = alivePlayers.get(0);
            Death_swap.isActive = false;
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
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
}
