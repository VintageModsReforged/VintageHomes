package mods.vintage.homes.command;

import mods.vintage.core.platform.lang.Translator;
import mods.vintage.homes.utils.CommonTeleporter;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class CommandDimensionTP extends VintageCommand {

    @Override
    public String getCommandName() {
        return "tpd";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return Translator.RED.format("message.command.tpd.usage", Translator.AQUA.literal("/tpd"), Translator.AQUA.literal("/tpd"));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            sender.sendChatToPlayer(Translator.RED.format("message.command.tpd.allowed"));
        }

        EntityPlayerMP player = (EntityPlayerMP) sender;
        World curWorld = player.worldObj;
        int currentDim = curWorld.provider.dimensionId;

        if (args.length == 0) {
            sender.sendChatToPlayer(Translator.WHITE.format("message.command.tpd.info1", Translator.AQUA.literal("" + curWorld.provider.dimensionId)));
            sender.sendChatToPlayer(Translator.GOLD.format("message.command.tpd.info2", Translator.AQUA.literal("/tpd")));
            return;
        }

        if (args.length > 1) {
            sender.sendChatToPlayer(Translator.GOLD.format("message.command.tpd.warning", Translator.AQUA.literal("/tpd")));
        }

        String arg = args[0];
        int id;

        if (arg.equalsIgnoreCase("nether")) {
            id = -1;
        } else if (arg.equalsIgnoreCase("end") || arg.equalsIgnoreCase("the end")) {
            id = 1;
        } else if (arg.equalsIgnoreCase("overworld")) {
            id = 0;
        } else {
            try {
                id = Integer.parseInt(arg);
            } catch (NumberFormatException e) {
                throw new CommandException("message.command.tpd.warning1");
            }
        }

        if (currentDim == id) {
            throw new CommandException("message.command.tpd.warning2");
        }

        WorldServer targetWorld = DimensionManager.getWorld(id);
        if (targetWorld == null) {
            throw new CommandException("message.command.tpd.warning3");
        }

        int x = (int) Math.floor(player.posX);
        int y = (int) Math.floor(player.posY);
        int z = (int) Math.floor(player.posZ);

        boolean foundSafe = false;

        // Search upward
        for (int i = 1; i < 255; i++) {
            int testY = y + i;
            if (isSafeTeleportLocation(targetWorld, x, testY, z)) {
                y = testY;
                foundSafe = true;
                break;
            }
        }

        // If not found above, search downward
        if (!foundSafe) {
            for (int i = 254; i > 0; i--) {
                int testY = i;
                if (isSafeTeleportLocation(targetWorld, x, testY, z)) {
                    y = testY;
                    foundSafe = true;
                    break;
                }
            }
        }

        // Fallback for The End
        if (id == 1 && !foundSafe) {
            x = 4;
            y = 64;
            z = 0;
            foundSafe = true;
        }

        if (!foundSafe) {
            throw new CommandException("message.command.tpd.warning4");
        }

        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, id, new CommonTeleporter(targetWorld, CommonTeleporter.Type.HOME, false));
        player.setPositionAndUpdate(x + 0.5, y, z + 0.5);
    }

    private boolean isSafeTeleportLocation(World world, int x, int y, int z) {
        if (y <= 1 || y >= 254) return false;

        int blockId1 = world.getBlockId(x, y, z);       // feet
        int blockId2 = world.getBlockId(x, y + 1, z);   // head
        int blockBelow = world.getBlockId(x, y - 1, z); // ground

        boolean airAbove = blockId1 == 0 && blockId2 == 0;
        boolean solidBelow = blockBelow != 0 && !Block.blocksList[blockBelow].isAirBlock(world, x, y - 1, z);

        return airAbove && solidBelow;
    }
}
