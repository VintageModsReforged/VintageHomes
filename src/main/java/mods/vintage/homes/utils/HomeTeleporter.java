package mods.vintage.homes.utils;

import mods.vintage.homes.command.VintageHomesWorldStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class HomeTeleporter extends Teleporter {
    private final boolean coordCalc;

    public HomeTeleporter(WorldServer worldIn, boolean doCoordCalc) {
        super(worldIn);
        this.coordCalc = doCoordCalc;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float yaw) {
        if (coordCalc) {
            if (!(entity instanceof EntityPlayerMP)) return;

            EntityPlayerMP player = (EntityPlayerMP) entity;
            MinecraftServer server = MinecraftServer.getServer();

            VintageHomesWorldStorage storage = VintageHomesWorldStorage.get(server);
            String playerName = player.username;

            if (!storage.tag.hasKey(playerName)) {
                // Fallback: send to spawn or log
                return;
            }

            int[] data = storage.tag.getIntArray(playerName);
            if (data.length < 4) return;

            int posX = data[1];
            int posY = data[2];
            int posZ = data[3];

            entity.setLocationAndAngles(posX + 0.5, posY, posZ + 0.5, entity.rotationYaw, 0.0F);
            entity.motionX = 0.0D;
            entity.motionY = 0.0D;
            entity.motionZ = 0.0D;
        } else {
            // Optional fallback: teleport to world spawn
            WorldServer world = (WorldServer) entity.worldObj;
            int spawnX = world.getSpawnPoint().posX;
            int spawnY = world.getSpawnPoint().posY;
            int spawnZ = world.getSpawnPoint().posZ;
            entity.setLocationAndAngles(spawnX + 0.5, spawnY, spawnZ + 0.5, entity.rotationYaw, 0.0F);
        }
    }

    @Override
    public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float yaw) {
        return false;
    }
}
