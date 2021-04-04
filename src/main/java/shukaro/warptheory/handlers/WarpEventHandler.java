package shukaro.warptheory.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import static thaumcraft.common.config.Config.potionWarpWardID;
import static thaumcraft.common.config.Config.wuss;

public class WarpEventHandler
{
    @SubscribeEvent
    public void livingUpdate(LivingEvent.LivingUpdateEvent e)
    {
        if (e.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)e.entity;
            boolean appliable = (!player.isPotionActive(potionWarpWardID) || (WarpHandler.getUnavoidableCount(player) > 0)) && !wuss && !player.capabilities.isCreativeMode;
            boolean tickflag = !player.worldObj.isRemote && player.ticksExisted > 0 && player.ticksExisted % 2000 == 0;
            if (tickflag && appliable && WarpHandler.getTotalWarp(player) > 0 && player.worldObj.rand.nextInt(100) <= Math.sqrt(WarpHandler.getTotalWarp(player)))
            {
                IWarpEvent event = WarpHandler.queueOneEvent(player, WarpHandler.getTotalWarp(player));
                if (event != null)
                {
                    int warpTemp = WarpHandler.getIndividualWarps(player)[2];
                    if (warpTemp > 0 && event.getCost() <= warpTemp)
                        WarpHandler.removeWarp(player, event.getCost());
                    else if (warpTemp > 0)
                        WarpHandler.removeWarp(player, warpTemp);
                }
            }
            if (player.ticksExisted % 20 == 0 && player.worldObj.rand.nextBoolean())
            {
                IWarpEvent event = WarpHandler.dequeueEvent(player);
                WarpHandler.addUnavoidableCount(player, -1);
                if (event != null && event.canDo(player) && appliable)
                    event.doEvent(player.worldObj, player);
            }
        }
    }
}
