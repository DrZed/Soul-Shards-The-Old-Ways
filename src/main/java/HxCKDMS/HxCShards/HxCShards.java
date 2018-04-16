package HxCKDMS.HxCShards;

import HxCKDMS.HxCShards.commands.CommandSoulShards;
import HxCKDMS.HxCShards.events.*;
import HxCKDMS.HxCShards.proxies.IProxy;
import HxCKDMS.HxCShards.tileentity.TileEntityCage;
import HxCKDMS.HxCShards.utils.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxcconfig.handlers.SpecialHandlers;
import hxckdms.hxccore.libraries.GlobalVariables;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;

@Mod(
    modid = Reference.modID,
    name = Reference.modName,
    version = Reference.modVersion,
    dependencies = Reference.requiredDependencies
)

@SuppressWarnings({"WeakerAccess","unused"})
public class HxCShards {
//If someone is wondering I'm not giving Whammich credit for this mod since near all his code was shit,
//broken, and hideous. He should be ashamed for stepping foot in the modding community, I know I'm not good,
//but he was so fucking terrible nothing of his functioned well at all. If SSR's sauce was still open I'd have
//edited it instead of this garbage.
	@SidedProxy(clientSide = Reference.clientProxy, serverSide = Reference.serverProxy)
	public static IProxy proxy;
	
	@Instance(Reference.modID)
	public static HxCShards modInstance;

	public static EntityList entityList;
    public static HxCConfig config, entityListConfig;
    public static HashMap<String, TileEntityCage> spawners = new HashMap<>();
    public static HashMap<String, String> logDump = new HashMap<>();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		SpecialHandlers.registerSpecialClass(Configs.Tier.class);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.load();
        config = new HxCConfig(Configs.class, "HxCSoulShards", GlobalVariables.modConfigDir, "cfg", Reference.modID);
		Configs.init();
		config.initConfiguration();

		MinecraftForge.EVENT_BUS.register(new EventSoulRitual());

		MinecraftForge.EVENT_BUS.register(new EventMineBlock());

		MinecraftForge.EVENT_BUS.register(new PlayerHurtEvent());

		MinecraftForge.EVENT_BUS.register(new PlayerKillEntityEvent());

		MinecraftForge.EVENT_BUS.register(new CreateShardEvent());
		
		MinecraftForge.EVENT_BUS.register(new AchievementEvents());
		FMLCommonHandler.instance().bus().register(new AchievementEvents());

        ModRegistry.registerObjects();

        EntityMapper.init();

		FMLInterModComms.sendMessage("Waila", "register", Reference.wailaCallBack);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		entityList = new EntityList();
		entityList.init();
		entityListConfig = new HxCConfig(EntityList.class, "HxCSoulShards-EntityList", GlobalVariables.modConfigDir, "cfg", Reference.modID);
        entityListConfig.initConfiguration();
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSoulShards());
	}
}
