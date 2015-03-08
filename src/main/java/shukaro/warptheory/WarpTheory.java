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
import org.apache.logging.log4j.Logger;
import shukaro.warptheory.block.WarpBlocks;
import shukaro.warptheory.entity.EntityPassiveCreeper;
import shukaro.warptheory.gui.WarpTab;
import shukaro.warptheory.handlers.ConfigHandler;
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

@Mod(modid = WarpTheory.modID, name = WarpTheory.modName, version = WarpTheory.modVersion, guiFactory = "shukaro.warptheory.gui.GuiFactory",
        dependencies = "required-after:Thaumcraft")
public class WarpTheory
{
    @SidedProxy(clientSide = "shukaro.warptheory.net.ClientProxy", serverSide = "shukaro.warptheory.net.CommonProxy")
    public static CommonProxy proxy;

    public static final String modID = "WarpTheory";
    public static final String modName = "WarpTheory";
    public static final String modVersion = "1.7.10R1.0c.Custom";
    
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
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        try
        {
            File folder = new File(event.getModConfigurationDirectory() + "/warptheory/");
            if (!folder.exists())
                folder.mkdirs();
            File normalFile = new File(event.getModConfigurationDirectory() + "/warptheory/normal.txt");
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

        try{
            ConfigHandler.init(event.getSuggestedConfigurationFile());
        }
        catch (Exception e) {
            logger.error("WarpTheory has failed to load its configuration file");
        }
        finally {
            if(ConfigHandler.config != null){
                ConfigHandler.save();
            }
        }
        WarpBlocks.initBlocks();
        WarpItems.initItems();


        //FMLCommonHandler.instance().bus().register(new ConfigHandler());

        if(ConfigHandler.allowWarpEffects)
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
