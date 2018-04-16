package HxCKDMS.HxCShards.compat.nei;

import HxCKDMS.HxCShards.guihandler.GuiSoulForge;
import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.Reference;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.FurnaceRecipeHandler;
import codechicken.nei.recipe.IRecipeHandler;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ForgeRecipeHandler extends FurnaceRecipeHandler implements IRecipeHandler {
	public ArrayList<SmeltingPair> recipes = new ArrayList<>();

	@Override
	public ForgeRecipeHandler newInstance() {
		try {
			return getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public class SmeltingPair extends CachedRecipe {
		public SmeltingPair(ItemStack output, ItemStack input) {
            input.stackSize = 1;
			ingred = new PositionedStack(input, 51, 6);
			result = new PositionedStack(output, 111, 22);
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 48, Collections.singletonList(ingred));
		}

		@Override
		public PositionedStack getResult() {
			return result;
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			List<PositionedStack> newList = new ArrayList<>();
			newList.add(getFuelStack());
			return newList;
		}

		private PositionedStack getFuelStack() {
			Map<ItemStack, Integer> allSmeltingItems = ModRegistry.sFHandler.getFuelList();
			List<ItemStack> allSmeltingItemsArray = new ArrayList<>(allSmeltingItems.keySet());
			int stack = (cycleticks / 48) % allSmeltingItems.size();
			ItemStack itemStack = allSmeltingItemsArray.get(stack);
			return new PositionedStack(itemStack, 51, 42);
		}

		PositionedStack ingred;
		PositionedStack result;
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "soulforgefuel"));
		transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "soulforge"));
	}

	@Override
	public void drawExtras(int i) {
		drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
		drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("soulforge") && getClass() == ForgeRecipeHandler.class)
			loadAllRecipes();
		else if (outputId.equals("soulforgefuel"))
		    loadAllRecipes();
		else super.loadCraftingRecipes(outputId, results);
	}

	 @Override
	 public void loadUsageRecipes(String inputId, Object... ingredients) {
         if (inputId.equals("item") && getClass() == ForgeRecipeHandler.class)
             loadUsageRecipes((ItemStack) ingredients[0]);
         else super.loadUsageRecipes(inputId, ingredients);
     }

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		Map<ItemStack, ItemStack> recipes = ModRegistry.sFHandler.getSmeltingList();
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		Map<ItemStack, Integer> fuel = ModRegistry.sFHandler.getFuelList();
        fuel.keySet().stream().filter(item -> item.isItemEqual(ingredient)).forEach(item -> loadAllRecipes());
	}

	private void loadAllRecipes() {
		ModRegistry.sFHandler.getSmeltingList().forEach((out, in) -> recipes.add(new SmeltingPair(out, in)));
	}

	@Override
	public String getRecipeName() {
		return "Soul forge";
	}

	@Override
	public String getOverlayIdentifier() {
		return "soulforge";
	}

	@Override
	public String getGuiTexture() {
		return Reference.modID + ":textures/gui/container/forge.png";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getGuiClass() {
		return GuiSoulForge.class;
	}

	@Override
	public int numRecipes() {
		return recipes.size();
	}

	@Override
	public List<PositionedStack> getIngredientStacks(int recipe) {
		return recipes.get(recipe).getIngredients();
	}

	@Override
	public PositionedStack getResultStack(int recipe) {
		return recipes.get(recipe).getResult();
	}

	@Override
	public List<PositionedStack> getOtherStacks(int recipe) {
		return recipes.get(recipe).getOtherStacks();
	}

}
