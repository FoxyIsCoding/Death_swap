package org.FoxysMods.death_swap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import org.FoxysMods.death_swap.commands.WhitelistCommand;
import org.FoxysMods.death_swap.commands.SettingsCommand;
import org.FoxysMods.death_swap.commands.StartCommand;
import org.FoxysMods.death_swap.commands.StopCommand;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Death_swap implements ModInitializer {
    public static int swapTime = 0;
    public static boolean isActive = false;
    public static MainBossbar MainBossbar;

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, registrationEnvironment) -> {
            StartCommand.register(dispatcher);
            SettingsCommand.register(dispatcher);
            StopCommand.register(dispatcher);
            WhitelistCommand.register(dispatcher);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            MainBossbar.register();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            DeathSwapState.getState(server).markDirty();
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            DeathSwapState state = DeathSwapState.getState(server);
            if (server.getTicks() % 20 == 0) {
                if (isActive) {
                    swapTime--;
                    MainBossbar.bossbar.setPercent((float) swapTime / state.swapInterval);
                    if (swapTime <= 0) {
                        SwapLogic.swap(server);
                        swapTime = state.swapInterval;
                    }

                    if (swapTime <= 10) {
                        var playerList = server.getPlayerManager().getPlayerList().stream()
                                .filter(p->state.whitelist.contains(p.getName().getString()))
                                .collect(Collectors.toCollection(ArrayList::new));
                        for (var player : playerList) {
                            player.sendMessage(
                                    Text.literal("Next swap in " + swapTime + " seconds!")
                                            .formatted(Formatting.RED, Formatting.BOLD),
                                    true
                            );
                            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 1.0f + (-swapTime-1) * 0.1f);
                        }
                    }
                }
            }
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (isActive && entity instanceof ServerPlayerEntity player) {
                DeathSwapState state = DeathSwapState.getState(player.getServer());
                if (!state.whitelist.contains(player.getName().getString())) return;

                player.changeGameMode(GameMode.SPECTATOR);
                WinnerLogic.checkForWinner(player.getServer());
            }
        });

        ServerPlayConnectionEvents.JOIN.register((serverPlayNetworkHandler, sender, server)->{
            var player = serverPlayNetworkHandler.getPlayer();
            DeathSwapState state = DeathSwapState.getState(player.getServer());
            if (isActive  && state.whitelist.contains(player.getName().getString())) {
                player.changeGameMode(GameMode.SPECTATOR);
            }
        });
    }
}