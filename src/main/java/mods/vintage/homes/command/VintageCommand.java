package mods.vintage.homes.command;

import mods.vintage.homes.VintageHomes;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;

public abstract class VintageCommand extends CommandBase {

    @Override
    public int getRequiredPermissionLevel() {
        return VintageHomes.commandPermissionLevel;
    }

    @Override
    public int compareTo(Object o) {
        return o instanceof ICommand ? this.getCommandName().compareTo(((ICommand) o).getCommandName()) : 0;
    }
}
