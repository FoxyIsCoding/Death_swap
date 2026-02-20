package org.FoxysMods.death_swap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class WinnerLogic {

    public static void checkForWinner(MinecraftServer server) {
        if (!Death_swap.isActive) return;

        var alivePlayers = server.getPlayerManager().getPlayerList().stream()
                .filter(player -> !player.isSpectator())
                .toList();

        if (alivePlayers.size() == 1) {
            ServerPlayerEntity winner = alivePlayers.get(0);

            WinsScoreboard.increase(winner);

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                player.sendMessage(Text.literal("✦ Winner! ✦").formatted(Formatting.GOLD, Formatting.BOLD));
                player.sendMessage(Text.literal(winner.getName().getString() + " has won Death Swap!").formatted(Formatting.GREEN, Formatting.BOLD));
                player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1.0f, 1.0f);

                if (player == winner) {
                    ServerWorld world = player.getServerWorld();
                    for (int i = 0; i < 100; i++) {
                        double offsetX = (Math.random() - 0.5) * 2;
                        double offsetY = Math.random() * 2 + 1;
                        double offsetZ = (Math.random() - 0.5) * 2;
                        world.spawnParticles(ParticleTypes.FIREWORK, player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ, 1, 0, 0, 0, 0.0);
                        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ, 1, 0, 0, 0, 0.0);
                    }
                }
            }
            StopLogic.stop(server);
        }
    }
}
