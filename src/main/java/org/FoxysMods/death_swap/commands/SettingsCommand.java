package org.FoxysMods.death_swap.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.FoxysMods.death_swap.DeathSwapState;
import org.FoxysMods.death_swap.Death_swap;

public class SettingsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("deathswap")
                .requires(source -> {
                    return source.hasPermissionLevel(2);
                })
                .then(CommandManager.literal("settings")
                        .then(CommandManager.literal("time")
                                .then(CommandManager.argument("seconds", IntegerArgumentType.integer(10))
                                        .executes(commandContext -> {
                                            DeathSwapState state = DeathSwapState.getState(commandContext.getSource().getServer());
                                            int seconds = IntegerArgumentType.getInteger(commandContext, "seconds");
                                            state.swapInterval = seconds;
                                            if (Death_swap.swapTime > seconds) Death_swap.swapTime = seconds;
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }
}