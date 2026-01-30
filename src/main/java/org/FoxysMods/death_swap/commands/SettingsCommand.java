package org.FoxysMods.death_swap.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Supplier;

public class SettingsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(net.minecraft.server.command.CommandManager.literal("deathswap")
            .then(net.minecraft.server.command.CommandManager.literal("settings")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    source.sendFeedback(
                            (Supplier<Text>) Text.literal("Death Swap Mod Settings:\n" +
                                "- Swap Interval: 5 minutes\n" +
                                "- Safe Zone Radius: 10 blocks\n" +
                                "- Enable Boss Bar: true\n" +
                                "- Sound Effects: true").formatted(Formatting.AQUA), false);
                    return 1;
                })));
    }
}
