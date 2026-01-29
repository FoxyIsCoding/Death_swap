package org.FoxysMods.death_swap;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.text.Text;

public class MainBossbar {
    public static ServerBossBar bossbar;
    public static void register() {
        bossbar = new ServerBossBar(Text.literal("Death Swap Time"), ServerBossBar.Color.RED, ServerBossBar.Style.NOTCHED_10);
    }
}
