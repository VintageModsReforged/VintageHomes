package mods.vintage.homes.command;

import mods.vintage.homes.References;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class VintageHomesWorldStorage extends WorldSavedData {
    private static final String DATA = References.ID + "_storage";
    public NBTTagCompound tag = new NBTTagCompound();

    public VintageHomesWorldStorage() {
        super(DATA);
    }

    public VintageHomesWorldStorage(String data) {
        super(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        this.tag = tag.getCompoundTag("homes");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setCompoundTag("homes", this.tag);
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
}
