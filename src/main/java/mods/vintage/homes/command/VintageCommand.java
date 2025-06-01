package mods.vintage.homes.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;

public abstract class VintageCommand extends CommandBase {

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public int compareTo(Object o) {
        return o instanceof ICommand ? this.getCommandName().compareTo(((ICommand) o).getCommandName()) : 0;
    }
}
