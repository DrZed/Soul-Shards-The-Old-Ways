package HxCKDMS.HxCShards.utils;

import HxCKDMS.HxCShards.HxCShards;
import HxCKDMS.HxCShards.block.BlockCage;
import HxCKDMS.HxCShards.block.BlockForge;
import HxCKDMS.HxCShards.block.BlockMaterials;
import HxCKDMS.HxCShards.enchantment.EnchantmentSoulStealer;
import HxCKDMS.HxCShards.events.Achievements;
import HxCKDMS.HxCShards.guihandler.GuiHandler;
import HxCKDMS.HxCShards.item.ItemMaterials;
import HxCKDMS.HxCShards.item.ItemSoulShard;
import HxCKDMS.HxCShards.item.armor.ItemSoulArmor;
import HxCKDMS.HxCShards.item.blocks.ItemBlockMaterials;
import HxCKDMS.HxCShards.item.tools.*;
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
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@SuppressWarnings("WeakerAccess")
public class ModRegistry {
	// Tool material for the soul tools/sword
	private static ToolMaterial SOULIUM = EnumHelper.addToolMaterial("SOULIUM", Configs.souliumMaterialHarvestLevel, Configs.souliumMaterialDurability, Configs.souliumMaterialMiningSpeed, Configs.souliumMaterialDamage, Configs.souliumMaterialEnchantability);
	private static ToolMaterial IMPROVED_SOULIUM = EnumHelper.addToolMaterial("IMPROVED_SOULIUM", Configs.souliumMaterialHarvestLevel * 2, Math.round(Configs.souliumMaterialDurability * 2.5f), Configs.souliumMaterialMiningSpeed * 1.5f, Configs.souliumMaterialDamage * 1.3f, Math.round(Configs.souliumMaterialEnchantability * 1.5f));
	private static ToolMaterial DRACONIC_SOULIUM = EnumHelper.addToolMaterial("DRACONIC_SOULIUM", Configs.souliumMaterialHarvestLevel * 5, Configs.souliumMaterialDurability * 15, Configs.souliumMaterialMiningSpeed * 8f, Configs.souliumMaterialDamage * 2.25f, Configs.souliumMaterialEnchantability * 2);

	private static ItemArmor.ArmorMaterial SOULIUM_A = EnumHelper.addArmorMaterial("SOULIUM", Configs.souliumMaterialDurability, new int[]{2, 5, 4, 2}, Configs.souliumMaterialEnchantability);
	private static ItemArmor.ArmorMaterial IMPROVED_SOULIUM_A = EnumHelper.addArmorMaterial("IMPROVED_SOULIUM", Configs.souliumMaterialDurability * 2, new int[]{3, 6, 5, 3}, Math.round(Configs.souliumMaterialEnchantability * 1.5f));
	private static ItemArmor.ArmorMaterial DRACONIC_SOULIUM_A = EnumHelper.addArmorMaterial("DRACONIC_SOULIUM", Configs.souliumMaterialDurability * 5, new int[]{4, 8, 6, 4}, Configs.souliumMaterialEnchantability * 2);

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

	public static Item ItemSwordSoul2 = new ItemSwordSoul(IMPROVED_SOULIUM);
	public static Item ItemPickaxeSoul2 = new ItemPickaxeSoul(IMPROVED_SOULIUM);
	public static Item ItemAxeSoul2 = new ItemAxeSoul(IMPROVED_SOULIUM);
	public static Item ItemSpadeSoul2 = new ItemSpadeSoul(IMPROVED_SOULIUM);

	public static Item ItemSwordSoul3 = new ItemSwordSoul(DRACONIC_SOULIUM);
	public static Item ItemPickaxeSoul3 = new ItemPickaxeSoul(DRACONIC_SOULIUM);
	public static Item ItemAxeSoul3 = new ItemAxeSoul(DRACONIC_SOULIUM);
	public static Item ItemSpadeSoul3 = new ItemSpadeSoul(DRACONIC_SOULIUM);

	public static Item ItemBowSoul = new ItemBowSoul(SOULIUM);
	public static Item ItemBowSoul2 = new ItemBowSoul(IMPROVED_SOULIUM);
	public static Item ItemBowSoul3 = new ItemBowSoul(DRACONIC_SOULIUM);

	public static Item ItemSoulHelm = new ItemSoulArmor(SOULIUM_A, 0);
	public static Item ItemSoulChest = new ItemSoulArmor(SOULIUM_A, 1);
	public static Item ItemSoulLegs = new ItemSoulArmor(SOULIUM_A, 2);
	public static Item ItemSoulBoots = new ItemSoulArmor(SOULIUM_A, 3);

	public static Item ItemSoulHelm2 = new ItemSoulArmor(IMPROVED_SOULIUM_A, 0);
	public static Item ItemSoulChest2 = new ItemSoulArmor(IMPROVED_SOULIUM_A, 1);
	public static Item ItemSoulLegs2 = new ItemSoulArmor(IMPROVED_SOULIUM_A, 2);
	public static Item ItemSoulBoots2 = new ItemSoulArmor(IMPROVED_SOULIUM_A, 3);

	public static Item ItemSoulHelm3 = new ItemSoulArmor(DRACONIC_SOULIUM_A, 0);
	public static Item ItemSoulChest3 = new ItemSoulArmor(DRACONIC_SOULIUM_A, 1);
	public static Item ItemSoulLegs3 = new ItemSoulArmor(DRACONIC_SOULIUM_A, 2);
	public static Item ItemSoulBoots3 = new ItemSoulArmor(DRACONIC_SOULIUM_A, 3);

	// Set up the mod blocks
	public static Block BlockCage = new BlockCage();
	public static Block BlockForge = new BlockForge().setCreativeTab(CREATIVE_TAB);
	public static Block BlockMaterials = new BlockMaterials();

	public static void registerObjects() {
		NetworkRegistry.INSTANCE.registerGuiHandler(HxCShards.modInstance, new GuiHandler());

		registerItems();
		registerBlocks();
		registerOreDictionaryEntries();
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

		GameRegistry.registerItem(ItemSwordSoul2, "ItemSwordSoul2");
		GameRegistry.registerItem(ItemPickaxeSoul2, "ItemPickaxeSoul2");
		GameRegistry.registerItem(ItemAxeSoul2, "ItemAxeSoul2");
		GameRegistry.registerItem(ItemSpadeSoul2, "ItemSpadeSoul2");

		GameRegistry.registerItem(ItemSwordSoul3, "ItemSwordSoul3");
		GameRegistry.registerItem(ItemPickaxeSoul3, "ItemPickaxeSoul3");
		GameRegistry.registerItem(ItemAxeSoul3, "ItemAxeSoul3");
		GameRegistry.registerItem(ItemSpadeSoul3, "ItemSpadeSoul3");

		GameRegistry.registerItem(ItemBowSoul, "ItemBowSoul");
		GameRegistry.registerItem(ItemBowSoul2, "ItemBowSoul2");
		GameRegistry.registerItem(ItemBowSoul3, "ItemBowSoul3");

		GameRegistry.registerItem(ItemSoulHelm, "ItemSoulHelm");
		GameRegistry.registerItem(ItemSoulChest, "ItemSoulChest");
		GameRegistry.registerItem(ItemSoulLegs, "ItemSoulLegs");
		GameRegistry.registerItem(ItemSoulBoots, "ItemSoulBoots");

		GameRegistry.registerItem(ItemSoulHelm2, "ItemSoulHelm2");
		GameRegistry.registerItem(ItemSoulChest2, "ItemSoulChest2");
		GameRegistry.registerItem(ItemSoulLegs2, "ItemSoulLegs2");
		GameRegistry.registerItem(ItemSoulBoots2, "ItemSoulBoots2");

		GameRegistry.registerItem(ItemSoulHelm3, "ItemSoulHelm3");
		GameRegistry.registerItem(ItemSoulChest3, "ItemSoulChest3");
		GameRegistry.registerItem(ItemSoulLegs3, "ItemSoulLegs3");
		GameRegistry.registerItem(ItemSoulBoots3, "ItemSoulBoots3");
	}

	private static void registerBlocks() {
		GameRegistry.registerBlock(BlockForge, "BlockForge");
		GameRegistry.registerBlock(BlockMaterials, ItemBlockMaterials.class, "BlockMaterials");
		GameRegistry.registerBlock(BlockCage, "BlockCage");
	}

	private static void registerOreDictionaryEntries() {
		// Materials
		OreDictionary.registerOre("nuggetSoulium", new ItemStack(ItemMaterials, 1, 0));
		OreDictionary.registerOre("ingotSoulium", new ItemStack(ItemMaterials, 1, 1));
		OreDictionary.registerOre("dustVile", new ItemStack(ItemMaterials, 1, 2));
		OreDictionary.registerOre("blockSoulium", new ItemStack(BlockMaterials, 1, 0));
		OreDictionary.registerOre("ingotSouliumImproved", new ItemStack(ItemMaterials, 1, 3));
		OreDictionary.registerOre("ingotSouliumDraconic", new ItemStack(ItemMaterials, 1, 4));
	}

	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityForge.class, "TileEntityForge");
        GameRegistry.registerTileEntity(TileEntityCage.class, "TileEntityCage");
	}

	private static void registerRecipes() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemMaterials, 9, 1), "blockSoulium"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemMaterials, 9, 0), "ingotSoulium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockForge), "SSS", "CFC", "OOO", 'S', Blocks.stone_slab, 'C', "cobblestone", 'O', Blocks.obsidian, 'F', Blocks.furnace));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemMaterials, 1, 1), "AAA", "AAA", "AAA", 'A', "nuggetSoulium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockMaterials, 1, 0), "AAA", "AAA", "AAA", 'A', "ingotSoulium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemMaterials, 1, 3), "DAD", "BAB", "DAD", 'A', "ingotSoulium", 'D', Items.diamond, 'B', Items.blaze_rod));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCage), "SIS", "IXI", "SIS", 'I', Blocks.iron_bars, 'S', "ingotSoulium"));

        if (!Loader.isModLoaded("Natura"))
		    GameRegistry.addSmelting(Blocks.soul_sand, new ItemStack(ItemMaterials, 1, 2), 0.35F);

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSwordSoul), "A", "A", "B", 'A', "ingotSoulium", 'B', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPickaxeSoul), "AAA", "CBC", "CBC", 'A', "ingotSoulium", 'B', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemAxeSoul), "AA", "AB", "CB", 'A', "ingotSoulium", 'B', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSpadeSoul), "A", "B", "B", 'A', "ingotSoulium", 'B', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBowSoul), " AB", "A B", " AB", 'A', "ingotSoulium", 'B', "ingotIron"));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSoulHelm), "AAA", "A A", "   ", 'A', "ingotSoulium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSoulChest), "A A", "AAA", "AAA", 'A', "ingotSoulium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSoulLegs), "AAA", "A A", "A A", 'A', "ingotSoulium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSoulBoots), "A A", "A A", "   ", 'A', "ingotSoulium"));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSwordSoul2), "A", "C", "B", 'A', "ingotSouliumImproved", 'B', "ingotIron", 'C', ItemSwordSoul));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPickaxeSoul2), "AAA", " B ", " B ", 'A', "ingotSouliumImproved", 'B', "ingotIron", 'C', ItemPickaxeSoul));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemAxeSoul2), " AA", " BA", " B ", 'A', "ingotSouliumImproved", 'B', "ingotIron", 'C', ItemAxeSoul));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSpadeSoul2), "A", "C", "B", 'A', "ingotSouliumImproved", 'B', "ingotIron", 'C', ItemSpadeSoul));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBowSoul2), " AB", "A C", " AB", 'A', "ingotSouliumImproved", 'B', "ingotIron", 'C', ItemBowSoul));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSoulHelm), "ABA", "A A", "   ", 'A', "ingotSouliumImproved", 'B', ItemSoulHelm));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSoulChest), "A A", "ABA", "AAA", 'A', "ingotSouliumImproved", 'B', ItemSoulChest));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSoulLegs), "ABA", "A A", "A A", 'A', "ingotSouliumImproved", 'B', ItemSoulLegs));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSoulBoots), "A A", "ABA", "   ", 'A', "ingotSouliumImproved", 'B', ItemSoulBoots));
	}


    private static void registerSoulForgeRecipes() {
        sFHandler.addFuel(new ItemStack(ItemMaterials, 1, 2), 1000);
        sFHandler.addFuel(new ItemStack(Items.blaze_powder, 1, 0), 50);
        sFHandler.addRecipe(new ItemStack(ItemSoulShard, 1), new ItemStack(Configs.harderSoulgems ? Items.nether_star : Items.diamond), 24000);
        sFHandler.addRecipe(new ItemStack(ItemMaterials, 1, 1), new ItemStack(Items.gold_ingot), 1800);
        sFHandler.addRecipe(new ItemStack(ItemMaterials, 1, 2), new ItemStack(Blocks.soul_sand), 500);
    }
}
