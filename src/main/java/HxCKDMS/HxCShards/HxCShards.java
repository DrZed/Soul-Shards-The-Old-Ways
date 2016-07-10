package HxCKDMS.HxCShards;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Configuration.HxCConfig;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCShards.commands.CommandSoulShards;
import HxCKDMS.HxCShards.events.AchievementEvents;
import HxCKDMS.HxCShards.events.CreateShardEvent;
import HxCKDMS.HxCShards.events.PlayerKillEntityEvent;
import HxCKDMS.HxCShards.proxies.IProxy;
import HxCKDMS.HxCShards.tileentity.TileEntityCage;
import HxCKDMS.HxCShards.utils.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
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

	public static Entitylist elist;
    public static HxCConfig config, entlist;
    public static HashMap<String, TileEntityCage> spawners = new HashMap<>();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.load();
        config = new HxCConfig(Configs.class, "HxCSoulShards", HxCCore.HxCConfigDir, "cfg");
        config.initConfiguration();

        LogHelper.debug("Registering PlayerKill Event", Reference.modName);
		MinecraftForge.EVENT_BUS.register(new PlayerKillEntityEvent());

		LogHelper.debug("Registering CreateShard Event", Reference.modName);
		MinecraftForge.EVENT_BUS.register(new CreateShardEvent());
		
		MinecraftForge.EVENT_BUS.register(new AchievementEvents());
		FMLCommonHandler.instance().bus().register(new AchievementEvents());

        LogHelper.debug("Registering Objects", Reference.modName);
        ModRegistry.registerObjs();

        LogHelper.debug("Registering EntityMapper", Reference.modName);
        EntityMapper.init();

		FMLInterModComms.sendMessage("Waila", "register", Reference.wailaCallBack);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		elist = new Entitylist();
		elist.init();
		entlist = new HxCConfig(Entitylist.class, "HxCSoulShards-EntityList", HxCCore.HxCConfigDir, "cfg");
        entlist.initConfiguration();
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSoulShards());
	}
}
