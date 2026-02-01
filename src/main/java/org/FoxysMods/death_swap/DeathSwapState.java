package org.FoxysMods.death_swap;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;

public class DeathSwapState extends PersistentState {
    public int swapInterval = 300;
    public ArrayList<String> ignoredPlayers = new ArrayList<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("swapInterval", swapInterval);

        var list = new net.minecraft.nbt.NbtList();
        for (String name : ignoredPlayers) {
            list.add(net.minecraft.nbt.NbtString.of(name));
        }
        nbt.put("ignoredPlayers", list);

        return nbt;
    }

    public static DeathSwapState createFromNbt(NbtCompound nbt) {
        DeathSwapState state = new DeathSwapState();

        state.swapInterval = nbt.getInt("swapInterval");

        var list = nbt.getList("ignoredPlayers", 8);
        for (int i = 0; i < list.size(); i++) {
            state.ignoredPlayers.add(list.getString(i));
        }

        return state;
    }

    public static DeathSwapState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                DeathSwapState::createFromNbt,
                DeathSwapState::new,
                "death_swap"
        );
    }

    public static DeathSwapState getState(MinecraftServer server) {
        return DeathSwapState.get(server.getOverworld());
    }

}
