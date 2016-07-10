package HxCKDMS.HxCShards.compat.waila;

import HxCKDMS.HxCShards.block.BlockCage;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaRegister {
	public static void wailaCallback (IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new SoulCageWailaProvider(), BlockCage.class);
		registrar.registerNBTProvider(new SoulCageWailaProvider(), BlockCage.class);
	}
}
