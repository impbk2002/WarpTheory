package shukaro.warptheory.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import static thaumcraft.common.config.Config.potionWarpWardID;
import static thaumcraft.common.config.Config.wuss;

public abstract class IWarpEvent
{
    public abstract String getName();

    public abstract int getSeverity();

    public final int getCost() { return (int)Math.ceil(getSeverity() / (double)10); }

    public boolean canDo(EntityPlayer player)
    {
        boolean flag = !player.isPotionActive(potionWarpWardID) || (WarpHandler.getUnavoidableCount(player) > 0) && !(wuss||player.capabilities.isCreativeMode);
        WarpHandler.addUnavoidableCount(player, -1);
        return flag;
    }

    public abstract boolean doEvent(World world, EntityPlayer player);
}
