package org.FoxysMods.death_swap.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.FoxysMods.death_swap.DeathSwapState;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class IgnoreCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("deathswap")
                .requires(source -> {
                    return source.hasPermissionLevel(2);
                })
                .then(CommandManager.literal("ignore")
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("player", StringArgumentType.word())
                                        .suggests(((commandContext, suggestionsBuilder) -> {
                                            var server = commandContext.getSource().getServer();
                                            DeathSwapState state = DeathSwapState.getState(server);
                                            var playerList = server.getPlayerManager().getPlayerList().stream()
                                                    .filter(p->!state.ignoredPlayers.contains(p.getName().getString()))
                                                    .collect(Collectors.toCollection(ArrayList::new));

                                            for (var player : playerList) {
                                                suggestionsBuilder.suggest(player.getName().getString());
                                            }
                                            return suggestionsBuilder.buildFuture();
                                        }))
                                        .executes(commandContext -> {
                                            DeathSwapState state = DeathSwapState.getState(commandContext.getSource().getServer());
                                            var name = StringArgumentType.getString(commandContext,"player");
                                            state.ignoredPlayers.add(name);
                                            commandContext.getSource().sendFeedback(()->Text.literal(String.format("Added %s to death swap ignore list",name)),true);
                                            return 1;
                                        })
                                )
                        )

                        .then(CommandManager.literal("remove")
                                .then(CommandManager.argument("player", StringArgumentType.word())
                                        .suggests(((commandContext, suggestionsBuilder) -> {
                                            DeathSwapState state = DeathSwapState.getState(commandContext.getSource().getServer());
                                            for (String name : state.ignoredPlayers) {
                                                suggestionsBuilder.suggest(name);
                                            }
                                            return suggestionsBuilder.buildFuture();
                                        }))
                                        .executes(commandContext -> {
                                            DeathSwapState state = DeathSwapState.getState(commandContext.getSource().getServer());
                                            var name = StringArgumentType.getString(commandContext,"player");
                                            state.ignoredPlayers.remove(name);
                                            commandContext.getSource().sendFeedback(()->Text.literal(String.format("Removed %s from death swap ignore list",name)),true);
                                            return 1;
                                        })
                                )
                        )

                        .then(CommandManager.literal("list")
                                .executes(commandContext -> {
                                    DeathSwapState state = DeathSwapState.getState(commandContext.getSource().getServer());
                                    if (state.ignoredPlayers.isEmpty()) {
                                        commandContext.getSource().sendFeedback(()->
                                                Text.literal("No ignored players"), false
                                        );
                                    } else {
                                        commandContext.getSource().sendFeedback(()->
                                                Text.literal(String.join(", ", state.ignoredPlayers)), false
                                        );
                                    }
                                    return 1;
                                })
                        )
                )
        );
    }
}
