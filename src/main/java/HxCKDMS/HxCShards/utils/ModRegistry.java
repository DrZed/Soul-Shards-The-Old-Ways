package HxCKDMS.HxCShards.utils;

import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCShards.HxCShards;
import HxCKDMS.HxCShards.block.BlockCage;
import HxCKDMS.HxCShards.block.BlockForge;
import HxCKDMS.HxCShards.block.BlockMaterials;
import HxCKDMS.HxCShards.enchantment.EnchantmentSoulStealer;
import HxCKDMS.HxCShards.events.Achievements;
import HxCKDMS.HxCShards.guihandler.GuiHandler;
import HxCKDMS.HxCShards.item.ItemMaterials;
import HxCKDMS.HxCShards.item.ItemSoulShard;
import HxCKDMS.HxCShards.item.blocks.ItemBlockMaterials;
import HxCKDMS.HxCShards.item.tools.ItemAxeSoul;
import HxCKDMS.HxCShards.item.tools.ItemPickaxeSoul;
import HxCKDMS.HxCShards.item.tools.ItemSpadeSoul;
import HxCKDMS.HxCShards.item.tools.ItemSwordSoul;
import HxCKDMS.HxCShards.tileentity.TileEntityCage;
import HxCKDMS.HxCShards.tileentity.TileEntityForge;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static HxCKDMS.HxCShards.utils.Configs.tiers;

public class ModRegistry {
	// Tool material for the soul tools/sword
	public static ToolMaterial SOULIUM = EnumHelper.addToolMaterial("SOULIUM", 3, 3122, 12.0F, 6F, 30);

	// Setting up the enchantment details from the config
	public static Enchantment SOUL_STEALER = new EnchantmentSoulStealer(Configs.enchantID, Configs.enchantWeight);

	// Set the creative tab
	public static SoulShardTab CREATIVE_TAB = new SoulShardTab();

    public static SFRecipeHandler sFHandler;

	// Set up the mod items
	public static Item ItemMaterials = new ItemMaterials();
	public static Item ItemSoulShard = new ItemSoulShard();
	public static Item ItemSwordSoul = new ItemSwordSoul(SOULIUM);
	public static Item ItemPickaxeSoul = new ItemPickaxeSoul(SOULIUM);
	public static Item ItemAxeSoul = new ItemAxeSoul(SOULIUM);
	public static Item ItemSpadeSoul = new ItemSpadeSoul(SOULIUM);

	// Set up the mod blocks
	public static Block BlockCage = new BlockCage();
	public static Block BlockForge = new BlockForge().setCreativeTab(CREATIVE_TAB);
	public static Block BlockMaterials = new BlockMaterials();

	public static void registerObjs() {
		NetworkRegistry.INSTANCE.registerGuiHandler(HxCShards.modInstance, new GuiHandler());
        LogHelper.debug("Registered " + tiers.size() + " shard tiers.", Reference.modName);
		registerItems();
		registerBlocks();
		registerOreDictEntries();
		registerTileEntities();
		registerRecipes();
        sFHandler = new SFRecipeHandler();
        registerSoulForgeRecipes();
        Achievements.Get();
	}

	private static void registerItems() {
		GameRegistry.registerItem(ItemMaterials, "ItemMaterialsSoul");
		GameRegistry.registerItem(ItemSoulShard, "ItemSoulShard");
		GameRegistry.registerItem(ItemSwordSoul, "ItemSwordSoul");
		GameRegistry.registerItem(ItemPickaxeSoul, "ItemPickaxeSoul");
		GameRegistry.registerItem(ItemAxeSoul, "ItemAxeSoul");
		GameRegistry.registerItem(ItemSpadeSoul, "ItemSpadeSoul");
	}

	private static void registerBlocks() {
		GameRegistry.registerBlock(BlockForge, "BlockForge");
		GameRegistry.registerBlock(BlockMaterials, ItemBlockMaterials.class, "BlockMaterials");
		GameRegistry.registerBlock(BlockCage, "BlockCage");
	}

	private static void registerOreDictEntries() {
		// Materials
		OreDictionary.registerOre("nuggetIron", new ItemStack(ItemMaterials, 1, 0));
		OreDictionary.registerOre("nuggetSoulium", new ItemStack(ItemMaterials, 1, 1));
		OreDictionary.registerOre("ingotSoulium", new ItemStack(ItemMaterials, 1, 2));
		OreDictionary.registerOre("dustVile", new ItemStack(ItemMaterials, 1, 3));
		OreDictionary.registerOre("blockSoulium", new ItemStack(BlockMaterials, 1, 0));
	}

	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityForge.class, "TileEntityForge");
        GameRegistry.registerTileEntity(TileEntityCage.class, "TileEntityCage");
	}

	private static void registerRecipes() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemMaterials, 9, 2), "blockSoulium"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemMaterials, 9, 1), "ingotSoulium"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemMaterials, 9, 0), "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockForge), "SSS", "S S", "OOO", 'S', "cobblestone", 'O', Blocks.obsidian));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemMaterials, 1, 2), "AAA", "AAA", "AAA", 'A', "nuggetSoulium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.iron_ingot), "AAA", "AAA", "AAA", 'A', "nuggetIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockMaterials, 1, 0), "AAA", "AAA", "AAA", 'A', "ingotSoulium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCage), "SIS", "IXI", "SIS", 'I', Blocks.iron_bars, 'S', "ingotSoulium"));

        if (!Loader.isModLoaded("Natura"))
		    GameRegistry.addSmelting(Blocks.soul_sand, new ItemStack(ItemMaterials, 1, 3), 0.35F);

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSwordSoul), "A", "A", "B", 'A', "ingotSoulium", 'B', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPickaxeSoul), "AAA", "CBC", "CBC", 'A', "ingotSoulium", 'B', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemAxeSoul), "AA", "AB", "CB", 'A', "ingotSoulium", 'B', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSpadeSoul), "A", "B", "B", 'A', "ingotSoulium", 'B', "ingotIron"));
	}


    private static void registerSoulForgeRecipes() {
        sFHandler.addFuel(new ItemStack(ItemMaterials, 1, 3), 900);
        sFHandler.addFuel(new ItemStack(Items.coal, 1, 1), 250);
        sFHandler.addFuel(new ItemStack(Items.coal, 1, 0), 250);
        sFHandler.addRecipe(new ItemStack(ItemSoulShard), new ItemStack(Items.diamond), 24000);
        sFHandler.addRecipe(new ItemStack(ItemSoulShard, 9), new ItemStack(Blocks.diamond_block), 24000*9);
        sFHandler.addRecipe(new ItemStack(ItemMaterials, 1, 1), new ItemStack(Items.gold_nugget), 200);
        sFHandler.addRecipe(new ItemStack(ItemMaterials, 1, 2), new ItemStack(Items.gold_ingot), 1800);
        sFHandler.addRecipe(new ItemStack(BlockMaterials, 1, 0), new ItemStack(Blocks.gold_block), 1800*9);
        sFHandler.addRecipe(new ItemStack(ItemMaterials, 2, 3), new ItemStack(Blocks.soul_sand), 500);
    }
}
