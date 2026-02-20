package org.FoxysMods.death_swap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class SwapLogic {
    record TP(ServerWorld w, double x, double y, double z, float yaw, float pitch) {}

    public static void swap(MinecraftServer server) {
        DeathSwapState state = DeathSwapState.getState(server);
        var playerList = server.getPlayerManager().getPlayerList().stream()
                .filter(player -> state.whitelist.contains(player.getName().getString()))
                .filter(player -> !player.isSpectator())
                .collect(Collectors.toCollection(ArrayList::new));
        var shuffledList = new ArrayList<>(playerList);

        if (playerList.size() < 2) return;

        boolean matches = true;
        while (matches) {
            Collections.shuffle(shuffledList);

            matches = false;
            for (int i = 0; i< shuffledList.size(); i++) {
                if (shuffledList.get(i).equals(playerList.get(i))) {
                    matches = true;
                    break;
                }
            }
        }

        var frozen = shuffledList.stream().map(p-> new TP(
                p.getServerWorld(),
                p.getX(),
                p.getY(),
                p.getZ(),
                p.getYaw(),
                p.getPitch()
        )).toList();
        for (int i = 0; i < playerList.size(); i++) {
            var player = playerList.get(i);
            player.teleport(
                    frozen.get(i).w,
                    frozen.get(i).x,
                    frozen.get(i).y,
                    frozen.get(i).z,
                    frozen.get(i).yaw,
                    frozen.get(i).pitch
            );
        }
    }
}
