package mods.vintage.homes.utils;

import mods.vintage.homes.command.VintageHomesWorldStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class CommonTeleporter extends Teleporter {

    private final Type type;
    private final String destination;
    private final boolean coordCalc;

    public CommonTeleporter(WorldServer worldIn, Type type, boolean doCoordCalc) {
        this(worldIn, type, "", doCoordCalc);
    }

    public CommonTeleporter(WorldServer worldIn, Type type, String destination, boolean doCoordCalc) {
        super(worldIn);
        this.type = type;
        this.destination = destination;
        this.coordCalc = doCoordCalc;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float yaw) {
        if (!coordCalc) {
            WorldServer world = (WorldServer) entity.worldObj;
            ChunkCoordinates spawn = world.getSpawnPoint();
            entity.setLocationAndAngles(spawn.posX, spawn.posY, spawn.posZ, entity.rotationYaw, 0.0F);
            return;
        }

        if (!(entity instanceof EntityPlayerMP)) return;

        EntityPlayerMP player = (EntityPlayerMP) entity;
        MinecraftServer server = MinecraftServer.getServer();
        VintageHomesWorldStorage storage = VintageHomesWorldStorage.get(server);

        NBTTagCompound tpTag = null;

        if (type == Type.HOME) {
            if (!storage.homesTag.hasKey(player.username)) return;
            tpTag = storage.homesTag.getCompoundTag(player.username);
        } else if (type == Type.WARP && destination != null) {
            if (!storage.warpsTag.hasKey(destination)) return;
            tpTag = storage.warpsTag.getCompoundTag(destination);
        }

        if (tpTag == null) return;

        double posX = tpTag.getDouble("posX");
        double posY = tpTag.getDouble("posY");
        double posZ = tpTag.getDouble("posZ");

        entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, 0.0F);
        entity.motionX = 0.0D;
        entity.motionY = 0.0D;
        entity.motionZ = 0.0D;
    }

    @Override
    public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float yaw) {
        return false;
    }

    public enum Type {HOME, WARP}
}
