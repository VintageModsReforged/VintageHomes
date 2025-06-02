package mods.vintage.homes.command;

import mods.vintage.homes.References;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class VintageHomesWorldStorage extends WorldSavedData {
    private static final String DATA = References.ID + "_storage";

    public NBTTagCompound homesTag = new NBTTagCompound();
    public NBTTagCompound warpsTag = new NBTTagCompound();

    public VintageHomesWorldStorage() {
        super(DATA);
    }

    public VintageHomesWorldStorage(String data) {
        super(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("homes")) {
            this.homesTag = tag.getCompoundTag("homes");
        }
        if (tag.hasKey("warps")) {
            this.warpsTag = tag.getCompoundTag("warps");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setTag("homes", this.homesTag);
        tag.setTag("warps", this.warpsTag);
    }

    // We want our homes to be global per player, so we should bind them to the overworld,
    // not whichever dimension the player is currently in.
    public static VintageHomesWorldStorage get(MinecraftServer server) {
        World world = server.worldServerForDimension(0);
        MapStorage storage = world.perWorldStorage;
        VintageHomesWorldStorage vintageStorage = (VintageHomesWorldStorage) storage.loadData(VintageHomesWorldStorage.class, DATA);
        if (vintageStorage == null) {
            vintageStorage = new VintageHomesWorldStorage();
            storage.setData(DATA, vintageStorage);
        }
        return vintageStorage;
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getWarpNames(NBTTagCompound warpsTag) {
        try {
            Field mapField = NBTTagCompound.class.getDeclaredField("tagMap");
            mapField.setAccessible(true);
            Map<String, NBTBase> tagMap = (Map<String, NBTBase>) mapField.get(warpsTag);
            return tagMap.keySet();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }
}
