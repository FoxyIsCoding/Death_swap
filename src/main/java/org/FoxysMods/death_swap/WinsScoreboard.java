package org.FoxysMods.death_swap;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class WinsScoreboard {
    public static void register(MinecraftServer server) {
        var scoreboard = server.getScoreboard();
        if (scoreboard.getObjective("Wins") == null) {
            scoreboard.addObjective("Wins", ScoreboardCriterion.DUMMY, Text.literal("Wins"), ScoreboardCriterion.RenderType.INTEGER);
        }
        scoreboard.setObjectiveSlot(0, scoreboard.getObjective("Wins"));
        scoreboard.setObjectiveSlot(2, scoreboard.getObjective("Wins"));
    }

    public static void increase(PlayerEntity player) {
        var scoreboard = player.getServer().getScoreboard();
        var objective = scoreboard.getObjective("Wins");
        var score = scoreboard.getPlayerScore(player.getName().getString(), objective);
        score.setScore(score.getScore() + 1);
    }
}
