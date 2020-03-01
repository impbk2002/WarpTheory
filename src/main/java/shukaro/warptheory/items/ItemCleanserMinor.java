package shukaro.warptheory.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.Constants;

public class ItemCleanserMinor extends ItemCleanser
{
    public ItemCleanserMinor() {
        super();
        this.setUnlocalizedName(Constants.ITEM_WARPCLEANSERMINOR);
    }

    @Override
    protected void purgeWarp(EntityPlayer player) {
        if (WarpHandler.getTotalWarp(player) == 0) {
            ChatHelper.sendToPlayer(player, StatCollector.translateToLocal("chat.warptheory.purgefail"));
        }

        WarpHandler.purgeWarpMinor(player);
    }

    @Override
    protected String getIcon() {
        return  "itemCleanserMinor";
    }
    
    @Override
    protected String getToolTip() {
        return "tooltip.warptheory.cleanserminor";
    }
}
