package org.FoxysMods.death_swap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class SwapLogic {
    public static void swap(MinecraftServer server) {
        var playerList = server.getPlayerManager().getPlayerList();
        for (int i=0; i < playerList.size(); i++) {
            ServerPlayerEntity playerA = playerList.get(i);
            ServerPlayerEntity playerB = playerList.get((i + 1) % playerList.size());

            var posA = playerA.getPos();

            playerA.requestTeleport(
                    playerB.getX(),
                    playerB.getY(),
                    playerB.getZ()
            );

            playerB.requestTeleport(
                    posA.x,
                    posA.y,
                    posA.z
            );
        }
    }
}
