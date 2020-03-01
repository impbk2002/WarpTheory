package shukaro.warptheory.handlers;

import baubles.api.BaublesApi;
import gnu.trove.map.hash.THashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.warpevents.*;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.NameMetaPair;
import shukaro.warptheory.util.ChatHelper;
import thaumcraft.api.IWarpingGear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class WarpHandler
{
    private static final Random random = new Random();

    public static Map<String, Integer> warpNormal;
    public static Map<String, Integer> warpTemp;
    public static Map<String, Integer> warpPermanent;
    public static boolean wuss = false;
    public static int potionWarpWardID = -1;

    public static ArrayList<IWarpEvent> warpEvents = new ArrayList<IWarpEvent>();

    public static Map<NameMetaPair, NameMetaPair> decayMappings = new THashMap<NameMetaPair, NameMetaPair>();

    public static void addDecayMapping(Block start, Block end)
    {
        addDecayMapping(start, 0, end, 0);
    }

    public static void addDecayMapping(Block start, int startMeta, Block end, int endMeta)
    {
        decayMappings.put(new NameMetaPair(start, startMeta), new NameMetaPair(end, endMeta));
    }

    public static void addDecayMapping(Block start, int startMeta, Block end)
    {
        decayMappings.put(new NameMetaPair(start, startMeta), new NameMetaPair(end, 0));
    }

    public static void initEvents()
    {
    	if(ConfigHandler.allowWarpEffect1)
    		warpEvents.add(new WarpBats(ConfigHandler.minimumWarpForEffect1));
    	if(ConfigHandler.allowWarpEffect2)
    		warpEvents.add(new WarpBlink(ConfigHandler.minimumWarpForEffect2));
    	if(ConfigHandler.allowWarpEffect3)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect3, "poison", new PotionEffect(Potion.poison.id, 20 * 20)));
    	if(ConfigHandler.allowWarpEffect4)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect4, "nausea", new PotionEffect(Potion.confusion.id, 20 * 20)));
    	if(ConfigHandler.allowWarpEffect5)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect5, "jump", new PotionEffect(Potion.jump.id, 20 * 20, 20)));
    	if(ConfigHandler.allowWarpEffect6)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect6, "blind", new PotionEffect(Potion.blindness.id, 20 * 20)));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect7)
    		warpEvents.add(new WarpDecay(ConfigHandler.minimumWarpForEffect7));
    	if(ConfigHandler.allowWarpEffect8)
    		warpEvents.add(new WarpEars(ConfigHandler.minimumWarpForEffect8));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect9)
    		warpEvents.add(new WarpSwamp(ConfigHandler.minimumWarpForEffect9));
    	if(ConfigHandler.allowWarpEffect10)
    		warpEvents.add(new WarpTongue(ConfigHandler.minimumWarpForEffect10));
    	if(ConfigHandler.allowWarpEffect11)
    		warpEvents.add(new WarpFriend(ConfigHandler.minimumWarpForEffect11));
    	if(ConfigHandler.allowWarpEffect12)
    		warpEvents.add(new WarpLivestockRain(ConfigHandler.minimumWarpForEffect12));
    	if(ConfigHandler.allowWarpEffect13)
    		warpEvents.add(new WarpWind(ConfigHandler.minimumWarpForEffect13));
    	if(ConfigHandler.allowWarpEffect14)
    		warpEvents.add(new WarpChests(ConfigHandler.minimumWarpForEffect14));
    	if(ConfigHandler.allowWarpEffect15)
    		warpEvents.add(new WarpBlood(ConfigHandler.minimumWarpForEffect15));
    	if(ConfigHandler.allowWarpEffect16)
    		warpEvents.add(new WarpAcceleration(ConfigHandler.minimumWarpForEffect16));
    	if(ConfigHandler.allowWarpEffect17)
    		warpEvents.add(new WarpLightning(ConfigHandler.minimumWarpForEffect17));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect18 && ConfigHandler.allowServerKickWarpEffects)
    		warpEvents.add(new WarpFall(ConfigHandler.minimumWarpForEffect18));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect19)
    		warpEvents.add(new WarpRain(ConfigHandler.minimumWarpForEffect19));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect20)
    		warpEvents.add(new WarpWither(ConfigHandler.minimumWarpForEffect20));
    	if(ConfigHandler.allowWarpEffect21)
    		warpEvents.add(new WarpFakeSound(ConfigHandler.minimumWarpForEffect21, "fakeexplosion", "random.explode", 8));
    	if(ConfigHandler.allowWarpEffect22)
    		warpEvents.add(new WarpFakeSoundBehind(ConfigHandler.minimumWarpForEffect22, "fakecreeper", "creeper.primed", 2));

        addDecayMapping(Blocks.grass, Blocks.dirt);
        addDecayMapping(Blocks.dirt, 0, Blocks.sand);
        addDecayMapping(Blocks.dirt, 1, Blocks.sand);
        addDecayMapping(Blocks.dirt, 2, Blocks.dirt);
        addDecayMapping(Blocks.stone, Blocks.cobblestone);
        addDecayMapping(Blocks.cobblestone, Blocks.gravel);
        addDecayMapping(Blocks.sandstone, Blocks.sand);
        addDecayMapping(Blocks.gravel, Blocks.sand);
        addDecayMapping(Blocks.sand, Blocks.air);
        addDecayMapping(Blocks.lava, Blocks.cobblestone);
        addDecayMapping(Blocks.flowing_lava, Blocks.cobblestone);
        addDecayMapping(Blocks.water, Blocks.air);
        addDecayMapping(Blocks.snow, Blocks.water);
        addDecayMapping(Blocks.snow_layer, Blocks.air);
        addDecayMapping(Blocks.ice, Blocks.water);
        addDecayMapping(Blocks.clay, Blocks.sand);
        addDecayMapping(Blocks.mycelium, Blocks.grass);
        addDecayMapping(Blocks.stained_hardened_clay, Blocks.hardened_clay);
        addDecayMapping(Blocks.hardened_clay, Blocks.clay);
        addDecayMapping(Blocks.coal_ore, Blocks.stone);
        addDecayMapping(Blocks.diamond_ore, Blocks.stone);
        addDecayMapping(Blocks.emerald_ore, Blocks.stone);
        addDecayMapping(Blocks.gold_ore, Blocks.stone);
        addDecayMapping(Blocks.iron_ore, Blocks.stone);
        addDecayMapping(Blocks.lapis_ore, Blocks.stone);
        addDecayMapping(Blocks.lit_redstone_ore, Blocks.stone);
        addDecayMapping(Blocks.redstone_ore, Blocks.stone);
        addDecayMapping(Blocks.quartz_ore, Blocks.netherrack);
        addDecayMapping(Blocks.netherrack, Blocks.cobblestone);
        addDecayMapping(Blocks.soul_sand, Blocks.sand);
        addDecayMapping(Blocks.glowstone, Blocks.cobblestone);
        addDecayMapping(Blocks.log, Blocks.dirt);
        addDecayMapping(Blocks.log2, Blocks.dirt);
        addDecayMapping(Blocks.brown_mushroom_block, Blocks.dirt);
        addDecayMapping(Blocks.red_mushroom_block, Blocks.dirt);
        addDecayMapping(Blocks.end_stone, Blocks.cobblestone);
        addDecayMapping(Blocks.obsidian, Blocks.cobblestone);
    }

    @SuppressWarnings("unchecked")
    public static boolean tcReflect()
    {
        try
        {
            wuss = Class.forName("thaumcraft.common.config.Config").getField("wuss").getBoolean(null);
            potionWarpWardID = Class.forName("thaumcraft.common.config.Config").getField("potionWarpWardID").getInt(null);
        }
        catch (Exception e)
        {
            WarpTheory.logger.warn("Could not reflect into thaumcraft.common.Config to get config settings");
            e.printStackTrace();
        }
        try
        {
            Class tc = Class.forName("thaumcraft.common.Thaumcraft");
            Object proxy = tc.getField("proxy").get(null);
            Object pK = proxy.getClass().getField("playerKnowledge").get(proxy);
            warpNormal = (Map<String, Integer>)pK.getClass().getDeclaredField("warpSticky").get(pK);
            warpTemp = (Map<String, Integer>)pK.getClass().getField("warpTemp").get(pK);
            warpPermanent = (Map<String, Integer>)pK.getClass().getField("warp").get(pK);
        }
        catch (Exception e)
        {
            WarpTheory.logger.warn("Could not reflect into thaumcraft.common.Thaumcraft to get warpNormal mappings, attempting older reflection");
            e.printStackTrace();
            try
            {
                Class tc = Class.forName("thaumcraft.common.Thaumcraft");
                Object proxy = tc.getField("proxy").get(null);
                Object pK = proxy.getClass().getField("playerKnowledge").get(proxy);
                warpNormal = (Map<String, Integer>)pK.getClass().getDeclaredField("warp").get(pK);
                warpTemp = (Map<String, Integer>)pK.getClass().getField("warpTemp").get(pK);
            }
            catch (Exception x)
            {
                WarpTheory.logger.warn("Failed to reflect into thaumcraft.common.Thaumcraft to get warpNormal mapping");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void purgeWarp(EntityPlayer player)
    {
        queueMultipleEvents(player, getTotalWarp(player));
        removeWarp(player, getTotalWarp(player));
    }

	//add function to remove 1 warp, chance based
	//warp < 50 = 0%, warp >= 150 = 100%
    public static void purgeWarpMinor(EntityPlayer player)
    {
		int rn = random.nextInt(101);
		rn = rn-50+getTotalWarp(player);
		if (rn >=100)
		{
			removeWarp(player, 1);
			ChatHelper.sendToPlayer(player, StatCollector.translateToLocal("chat.warptheory.purgeminor"));
		}
		else
			ChatHelper.sendToPlayer(player, StatCollector.translateToLocal("chat.warptheory.purgefailed"));
    }

    public static void removeWarp(EntityPlayer player, int amount)
    {
        if (amount <= 0)
            return;
        if ((warpNormal != null && warpTemp != null) || tcReflect())
        {
            String name = player.getDisplayName();
            int wp = warpPermanent != null ? warpPermanent.get(name) : 0;
            int wn = warpNormal.get(name);
            int wt = warpTemp.get(name);
            if (amount <= wt)
            {
                warpTemp.put(name, wt - amount);
                return;
            }
            else
            {
                warpTemp.put(name, 0);
                amount -= wt;
            }
            if (amount <= wn)
            {
                warpNormal.put(name, wn - amount);
                return;
            }
            else
            {
                warpNormal.put(name, 0);
                amount -= wn;
            }
            if (ConfigHandler.allowPermWarpRemoval)
            {
                if ((int)Math.ceil(amount / ConfigHandler.permWarpMult) <= wp)
                    warpPermanent.put(name, wp - (int)Math.ceil(amount / ConfigHandler.permWarpMult));
                else
                    warpPermanent.put(name, 0);
            }
        }
    }

    public static int getTotalWarp(EntityPlayer player)
    {
        if (player == null)
            return 0;
        if ((warpNormal != null && warpTemp != null) || tcReflect())
        {
            return ((warpPermanent != null && warpPermanent.get(player.getDisplayName()) != null) ? warpPermanent.get(player.getDisplayName()) : 0) * ConfigHandler.permWarpMult +
                    (warpNormal.get(player.getDisplayName()) != null ? warpNormal.get(player.getDisplayName()) : 0) +
                    (warpTemp.get(player.getDisplayName()) != null ? warpTemp.get(player.getDisplayName()) : 0) +
                    getWarpFromGear(player);
        }
        return 0;
    }

    public static int[] getIndividualWarps(EntityPlayer player)
    {
        int[] totals = new int[3];
        if ((warpNormal != null && warpTemp != null) || tcReflect())
        {
            totals[0] = warpPermanent != null ? warpPermanent.get(player.getDisplayName()) : 0;
            totals[1] = warpNormal.get(player.getDisplayName());
            totals[2] = warpTemp.get(player.getDisplayName());
        }
        return totals;
    }

    public static int queueMultipleEvents(EntityPlayer player, int amount)
    {
        int w = amount;
        while (w > 0)
        {
            IWarpEvent event = queueOneEvent(player, w);
            if (event == null)
                return w;
            w -= event.getCost();
        }
        return w;
    }

    public static IWarpEvent queueOneEvent(EntityPlayer player, int maxSeverity)
    {
        IWarpEvent event = getAppropriateEvent(player, maxSeverity);
        if (event != null)
            queueEvent(player, event);
        return event;
    }

    public static IWarpEvent getAppropriateEvent(EntityPlayer player, int maxSeverity)
    {
        ArrayList<IWarpEvent> shuffled = (ArrayList<IWarpEvent>)warpEvents.clone();
        Collections.shuffle(shuffled);
        for (IWarpEvent e : shuffled)
        {
            if (e.getSeverity() <= maxSeverity)
                return e;
        }
        return null;
    }

    public static int getWarpFromGear(EntityPlayer player)
    {
        int w = 0;
        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IWarpingGear)
            w += ((IWarpingGear)player.getCurrentEquippedItem().getItem()).getWarp(player.getCurrentEquippedItem(), player);
        IInventory baubles = BaublesApi.getBaubles(player);
        for (int i = 0; i < 4; i++)
        {
            if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() instanceof IWarpingGear)
                w += ((IWarpingGear)player.inventory.getStackInSlot(i).getItem()).getWarp(player.inventory.getStackInSlot(i), player);
            if (baubles != null && baubles.getStackInSlot(i) != null && baubles.getStackInSlot(i).getItem() instanceof IWarpingGear)
                w += ((IWarpingGear)baubles.getStackInSlot(i).getItem()).getWarp(baubles.getStackInSlot(i), player);
        }
        return w;
    }

    public static IWarpEvent getEventFromName(String name)
    {
        for (IWarpEvent event : warpEvents)
        {
            if (event.getName().equals(name))
                return event;
        }
        return null;
    }

    public static void queueEvent(EntityPlayer player, IWarpEvent event)
    {
        String queue;
        if (!MiscHelper.getWarpTag(player).hasKey("queuedEvents"))
            queue = "";
        else
            queue = MiscHelper.getWarpTag(player).getString("queuedEvents");
        queue += event.getName() + " ";
        MiscHelper.getWarpTag(player).setString("queuedEvents", queue);
    }

    public static IWarpEvent dequeueEvent(EntityPlayer player)
    {
        String queue;
        if (!MiscHelper.getWarpTag(player).hasKey("queuedEvents"))
            queue = "";
        else
            queue = MiscHelper.getWarpTag(player).getString("queuedEvents");
        if (queue.length() > 0)
        {
            ArrayList<String> names = new ArrayList<String>();
            for (String n : queue.split(" "))
                names.add(n);
            Collections.shuffle(names);
            String todo = names.remove(player.worldObj.rand.nextInt(names.size()));
            queue = "";
            for (String n : names)
                queue += n + " ";
            MiscHelper.getWarpTag(player).setString("queuedEvents", queue);
            return getEventFromName(todo);
        }
        return null;
    }
}
