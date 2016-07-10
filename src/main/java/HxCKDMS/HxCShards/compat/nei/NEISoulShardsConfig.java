package HxCKDMS.HxCShards.compat.nei;

import HxCKDMS.HxCShards.utils.Reference;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEISoulShardsConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return Reference.modName;
	}

	@Override
	public String getVersion() {
		return Reference.modVersion;
	}

	@Override
	public void loadConfig() {
		ForgeRecipeHandler handler = new ForgeRecipeHandler();
		API.registerRecipeHandler(handler);
		API.registerUsageHandler(handler);
	}

}
