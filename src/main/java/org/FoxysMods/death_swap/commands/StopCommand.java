package org.FoxysMods.death_swap.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.FoxysMods.death_swap.StopLogic;

public class StopCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("deathswap")
                .then(CommandManager.literal("stop")
                        .executes(commandContext -> {
                            StopLogic.stop(commandContext.getSource().getServer());
                            return 1;
                        })
                )
        );
    }
}
