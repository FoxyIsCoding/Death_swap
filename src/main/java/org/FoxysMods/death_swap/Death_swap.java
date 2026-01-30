package org.FoxysMods.death_swap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import org.FoxysMods.death_swap.commands.SettingsCommand;
import org.FoxysMods.death_swap.commands.StartCommand;

import java.util.List;

public class Death_swap implements ModInitializer {
    public static int swapInterval = 300;
    public static int swapTime = 0;
    public static boolean isActive = false;
    public static MainBossbar MainBossbar;

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, registrationEnvironment) -> {
            StartCommand.register(dispatcher);
            SettingsCommand.register(dispatcher);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            MainBossbar.register();
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % 20 == 0) {
                if (isActive) {
                    swapTime--;
                    MainBossbar.bossbar.setPercent((float) swapTime / swapInterval);
                    if (swapTime <= 0) {
                        SwapLogic.swap(server);
                        swapTime = swapInterval;
                    }

                    if (swapTime <= 10) {
                        for (var player : server.getPlayerManager().getPlayerList()) {
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
                player.changeGameMode(GameMode.SPECTATOR);

                WinnerLogic.checkForWinner(player.getServer());
            }
        });
    }
}