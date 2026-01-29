package org.FoxysMods.death_swap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class Death_swap implements ModInitializer {
    public static int swapInterval = 10;
    public static int swapTime = 0;
    public static boolean isActive = false;

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, registrationEnvironment) -> {
            MainCommand.register(dispatcher);
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
                        isActive = false;
                    }
                }
            }
        });
    }
}
