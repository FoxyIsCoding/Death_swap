package org.FoxysMods.death_swap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class SwapLogic {
    public static void swap(MinecraftServer server) {
        var playerList = server.getPlayerManager().getPlayerList();
        for (int i=0; i < playerList.size(); i+=2) {
            ServerPlayerEntity playerA = playerList.get(i);
            ServerPlayerEntity playerB = playerList.get((i + 1) % playerList.size());

            var posA = playerA.getPos();
            playerA.teleport(
                playerB.getX(),playerB.getY(),playerB.getZ()
            );
            playerB.teleport(
                posA.x, posA.y, posA.z
            );
        }
    }
}
