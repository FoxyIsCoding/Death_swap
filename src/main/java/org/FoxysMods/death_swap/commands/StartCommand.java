package org.FoxysMods.death_swap.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.FoxysMods.death_swap.Death_swap;
import org.FoxysMods.death_swap.MainBossbar;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("deathswap").then(CommandManager.literal("start").executes(commandContext -> {
            ServerCommandSource source = commandContext.getSource();
            var server = source.getServer();
            List<ServerPlayerEntity> playerList = server.getPlayerManager().getPlayerList();
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            scheduler.schedule(() -> {
                server.execute(() -> {
                    for (ServerPlayerEntity player : playerList) {
                        player.sendMessage(Text.literal("3").formatted(Formatting.GOLD, Formatting.BOLD), true);
                        player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 0.8f);
                    }
                });
            }, 0, TimeUnit.SECONDS);

            scheduler.schedule(() -> {
                server.execute(() -> {
                    for (ServerPlayerEntity player : playerList) {
                        player.sendMessage(Text.literal("2").formatted(Formatting.GOLD, Formatting.BOLD), true);
                        player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 0.9f);
                    }
                });
            }, 1, TimeUnit.SECONDS);

            scheduler.schedule(() -> {
                server.execute(() -> {
                    for (ServerPlayerEntity player : playerList) {
                        player.sendMessage(Text.literal("1").formatted(Formatting.GOLD, Formatting.BOLD), true);
                        player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 1.0f);
                    }
                });
            }, 2, TimeUnit.SECONDS);

            scheduler.schedule(() -> {
                server.execute(() -> {
                    for (ServerPlayerEntity player : playerList) {
                        player.sendMessage(Text.literal("GO!").formatted(Formatting.GREEN, Formatting.BOLD), true);

                        player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 1.0f, 1.0f);

                        ServerWorld world = player.getServerWorld();
                        for (int j = 0; j < 50; j++) {
                            double offsetX = (Math.random() - 0.5) * 2;
                            double offsetY = Math.random() * 2;
                            double offsetZ = (Math.random() - 0.5) * 2;

                            world.spawnParticles(ParticleTypes.POOF, player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ, 1, 0, 0, 0, 0.0);
                        }

                        MainBossbar.bossbar.addPlayer(player);
                    }

                    Death_swap.isActive = true;
                    Death_swap.swapTime = Death_swap.swapInterval;


                    for (ServerPlayerEntity player : playerList) {
                        player.sendMessage(Text.literal(""), false);
                        player.sendMessage(Text.literal("✦ ").formatted(Formatting.GOLD).append(Text.literal("Death Swap is now ACTIVE!").formatted(Formatting.GREEN, Formatting.BOLD)).append(Text.literal(" ✦").formatted(Formatting.GOLD)), false);
                        player.sendMessage(Text.literal("First swap in: ").formatted(Formatting.GRAY).append(Text.literal("5 minutes").formatted(Formatting.AQUA, Formatting.BOLD)), false);
                    }

                    scheduler.shutdown();
                });
            }, 3, TimeUnit.SECONDS);

            return 1;
        })));
    }
}