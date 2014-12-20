package shukaro.warptheory;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;
import shukaro.warptheory.block.WarpBlocks;
import shukaro.warptheory.entity.EntityPassiveCreeper;
import shukaro.warptheory.gui.WarpTab;
import shukaro.warptheory.handlers.WarpCommand;
import shukaro.warptheory.handlers.WarpEventHandler;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.items.WarpItems;
import shukaro.warptheory.net.CommonProxy;
import shukaro.warptheory.recipe.WarpRecipes;
import shukaro.warptheory.research.WarpResearch;
import shukaro.warptheory.util.NameGenerator;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

@Mod(modid = WarpTheory.modID, name = WarpTheory.modName, version = WarpTheory.modVersion,
        dependencies = "required-after:Thaumcraft")
public class WarpTheory
{
    @SidedProxy(clientSide = "shukaro.warptheory.net.ClientProxy", serverSide = "shukaro.warptheory.net.CommonProxy")
    public static CommonProxy proxy;

    public static final String modID = "WarpTheory";
    public static final String modName = "WarpTheory";
    public static final String modVersion = "1.7.10R1.0.Custom";

    public static boolean wussMode = false;
    public static int permWarpMult = 2;
    public static boolean allowPermWarpRemoval = true;
    public static boolean allowGlobalWarpEffects = false;
    public static boolean allowWarpEffects = false;
    public static boolean allowServerKickWarpEffects = false;
    public static boolean allowWarpEffect1 = false;
    public static boolean allowWarpEffect2 = false;
    public static boolean allowWarpEffect3 = false;
    public static boolean allowWarpEffect4 = false;
    public static boolean allowWarpEffect5 = false;
    public static boolean allowWarpEffect6 = false;
    public static boolean allowWarpEffect7 = false;
    public static boolean allowWarpEffect8 = false;
    public static boolean allowWarpEffect9 = false;
    public static boolean allowWarpEffect10 = false;
    public static boolean allowWarpEffect11 = false;
    public static boolean allowWarpEffect12 = false;
    public static boolean allowWarpEffect13 = false;
    public static boolean allowWarpEffect14 = false;
    public static boolean allowWarpEffect15 = false;
    public static boolean allowWarpEffect16 = false;
    public static boolean allowWarpEffect17 = false;
    public static boolean allowWarpEffect18 = false;
    public static boolean allowWarpEffect19 = false;
    public static boolean allowWarpEffect20 = false;
    public static boolean allowWarpEffect21 = false;
    public static boolean allowWarpEffect22 = false;
    
    public static Logger logger;

    public static CreativeTabs mainTab = new WarpTab(StatCollector.translateToLocal("warptheory.tab"));

    public static NameGenerator normalNames;

    @Mod.Instance(modID)
    public static WarpTheory instance;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent evt)
    {
        evt.registerServerCommand(new WarpCommand());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
        logger = evt.getModLog();
        try
        {
            File folder = new File(evt.getModConfigurationDirectory() + "/warptheory/");
            if (!folder.exists())
                folder.mkdirs();
            File normalFile = new File(evt.getModConfigurationDirectory() + "/warptheory/normal.txt");
            if (!normalFile.exists())
            {
                InputStream in = WarpTheory.class.getResourceAsStream("/assets/warptheory/names/normal.txt");
                Files.copy(in, normalFile.getAbsoluteFile().toPath());
            }
            normalNames = new NameGenerator(normalFile.getAbsolutePath());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        WarpBlocks.initBlocks();
        WarpItems.initItems();
        Configuration c = new Configuration(evt.getSuggestedConfigurationFile());
        c.load();
        wussMode = c.getBoolean("wussMode", "general", false, "enables less expensive recipes");
        permWarpMult = c.getInt("permWarpMult", "general", 2, 0, Integer.MAX_VALUE, "how much more 'expensive' permanent warp is compared to normal warp");
        allowPermWarpRemoval = c.getBoolean("allowPermWarpRemoval", "general", true, "whether items can remove permanent warp or not");
        allowGlobalWarpEffects = c.getBoolean("allowGlobalWarpEffects", "general", true, "whether warp effects that involve the environment are triggered");
        allowWarpEffects = c.getBoolean("addWarpEffects", "general", true, "whether to add general warp effects. If false extra effects will only be seen when using Pure Tear");
        allowServerKickWarpEffects = c.getBoolean("allowServerErrorWarpEffects", "general", false, "whether to allow warp effects known to cause errors on servers");
        allowWarpEffect1 = c.getBoolean("allowWarpEffect1", "general", true, "whether to allow spawn bats warp effect. If all disabled Pure Tear will not work");
        allowWarpEffect2 = c.getBoolean("allowWarpEffect2", "general", true, "whether to allow random teleport warp effect if all disabled Pure Tear will not work");
        allowWarpEffect3 = c.getBoolean("allowWarpEffect3", "general", true, "whether to allow poison warp effect if all disabled Pure Tear will not work");
        allowWarpEffect4 = c.getBoolean("allowWarpEffect4", "general", true, "whether to allow nausea warp effect if all disabled Pure Tear will not work");
        allowWarpEffect5 = c.getBoolean("allowWarpEffect5", "general", true, "whether to allow jump boost warp effect if all disabled Pure Tear will not work");
        allowWarpEffect6 = c.getBoolean("allowWarpEffect6", "general", true, "whether to allow blindness warp effect if all disabled Pure Tear will not work");
        allowWarpEffect7 = c.getBoolean("allowWarpEffect7", "general", true, "whether to allow decay warp effect if all disabled Pure Tear will not work");
        allowWarpEffect8 = c.getBoolean("allowWarpEffect8", "general", true, "whether to allow ears (unable to read messages) warp effect if all disabled Pure Tear will not work");
        allowWarpEffect9 = c.getBoolean("allowWarpEffect9", "general", true, "whether to allow swamp (random trees) warp effect if all disabled Pure Tear will not work");
        allowWarpEffect10 = c.getBoolean("allowWarpEffect10", "general", true, "whether to allow tongue (unable to send messages) warp effect if all disabled Pure Tear will not work");
        allowWarpEffect11 = c.getBoolean("allowWarpEffect11", "general", true, "whether to allow friendly creeper warp effect if all disabled Pure Tear will not work");
        allowWarpEffect12 = c.getBoolean("allowWarpEffect12", "general", true, "whether to allow livestock rain warp effect if all disabled Pure Tear will not work");
        allowWarpEffect13 = c.getBoolean("allowWarpEffect13", "general", true, "whether to allow wind warp effect if all disabled Pure Tear will not work");
        allowWarpEffect14 = c.getBoolean("allowWarpEffect14", "general", true, "whether to allow chest scramble warp effect if all disabled Pure Tear will not work");
        allowWarpEffect15 = c.getBoolean("allowWarpEffect15", "general", true, "whether to allow blood warp effect if all disabled Pure Tear will not work");
        allowWarpEffect16 = c.getBoolean("allowWarpEffect16", "general", true, "whether to allow acceleration warp effect if all disabled Pure Tear will not work");
        allowWarpEffect17 = c.getBoolean("allowWarpEffect17", "general", true, "whether to allow lightning warp effect if all disabled Pure Tear will not work");
        allowWarpEffect18 = c.getBoolean("allowWarpEffect18", "general", false, "whether to allow world hole warp effect. If all disabled Pure Tear will not work. Server errors.");
        allowWarpEffect19 = c.getBoolean("allowWarpEffect19", "general", true, "whether to allow rain warp effect if all disabled Pure Tear will not work");
        allowWarpEffect20 = c.getBoolean("allowWarpEffect20", "general", true, "whether to allow spawn wither warp effect if all disabled Pure Tear will not work");
        allowWarpEffect21 = c.getBoolean("allowWarpEffect21", "general", true, "whether to allow fake explosion warp effect if all disabled Pure Tear will not work");
        allowWarpEffect22 = c.getBoolean("allowWarpEffect22", "general", true, "whether to allow fake creeper warp effect if all disabled Pure Tear will not work");
        c.save();
        if(allowWarpEffects)
        	MinecraftForge.EVENT_BUS.register(new WarpEventHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt)
    {
        WarpRecipes.initRecipes();
        WarpHandler.initEvents();
        EntityRegistry.registerModEntity(EntityPassiveCreeper.class, "creeperPassive", 0, this, 160, 4, true);
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        WarpHandler.tcReflect();
        WarpResearch.initResearch();
    }
}
