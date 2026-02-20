package org.FoxysMods.death_swap.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.FoxysMods.death_swap.DeathSwapState;

public class WhitelistCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("deathswap")
                .requires(source -> {
                    return source.hasPermissionLevel(2);
                })
                .then(CommandManager.literal("whitelist")
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("players", EntityArgumentType.players())
                                        .executes(commandContext -> {
                                            DeathSwapState state = DeathSwapState.getState(commandContext.getSource().getServer());
                                            var players = EntityArgumentType.getPlayers(commandContext, "players");
                                            for (var player : players) {
                                                state.whitelist.add(player.getName().getString());
                                            }
                                            commandContext.getSource().sendFeedback(()->Text.literal("Added players to whitelist"), false);
                                            return 1;
                                        })
                                )
                        )

                        .then(CommandManager.literal("remove")
                                .then(CommandManager.argument("players", EntityArgumentType.players())
                                        .executes(commandContext -> {
                                            DeathSwapState state = DeathSwapState.getState(commandContext.getSource().getServer());
                                            var players = EntityArgumentType.getPlayers(commandContext, "players");
                                            for (var player : players) {
                                                state.whitelist.remove(player.getName().getString());
                                            }
                                            commandContext.getSource().sendFeedback(()->Text.literal("Removed players from whitelist"), false);
                                            return 1;
                                        })
                                )
                        )

                        .then(CommandManager.literal("list")
                                .executes(commandContext -> {
                                    DeathSwapState state = DeathSwapState.getState(commandContext.getSource().getServer());
                                    if (state.whitelist.isEmpty()) {
                                        commandContext.getSource().sendFeedback(()->
                                                Text.literal("No whitelisted players"), false
                                        );
                                    } else {
                                        commandContext.getSource().sendFeedback(()->
                                                Text.literal(String.join(", ", state.whitelist)), false
                                        );
                                    }
                                    return 1;
                                })
                        )
                )
        );
    }
}
