package org.FoxysMods.death_swap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;


public class WinnerLogic {

    public static void checkForWinner(MinecraftServer server) {
        if (!Death_swap.isActive) return;

        var alivePlayers = server.getPlayerManager().getPlayerList().stream()
                .filter(player -> !player.isSpectator())
                .toList();

        if (alivePlayers.size() == 1) {
            ServerPlayerEntity winner = alivePlayers.get(0);
            StopLogic.stop(server);
        }
    }
}
