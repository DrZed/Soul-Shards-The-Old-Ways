package HxCKDMS.HxCShards.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SFRecipeHandler {
    private HashMap<Item, FRecipe> recipes = new HashMap<>();
	private Map<ItemStack, Integer> fuelList = new HashMap<>();

	public Map<ItemStack, ItemStack> getSmeltingList() {
        HashMap<ItemStack, ItemStack> list = new HashMap<>();
        recipes.values().forEach(recipe -> list.put(recipe.output, recipe.input));
		return list;
	}

	public Map<ItemStack, Integer> getFuelList() {
		return fuelList;
	}

	public void addRecipe(ItemStack output, ItemStack input, int fuelCost) {
        recipes.put(input.getItem(), new FRecipe(output, input, fuelCost));
	}

    public void addFuel(ItemStack itemStack, int burnTime) {
		fuelList.put(itemStack, burnTime);
	}

    public int getFuelBurnTime(ItemStack fuel) {
        final int[] t = {0};
        if (fuel != null && fuelList.containsKey(fuel))
            return fuelList.get(fuel);
        else if (fuel != null) {
            fuelList.keySet().forEach(f -> {
                if (f.getItem() == fuel.getItem() &&
                        f.getItemDamage() == fuel.getItemDamage() &&
                        f.getUnlocalizedName().equals(fuel.getUnlocalizedName()))
                    t[0] = fuelList.get(f);
            });
        }
        return t[0];
    }

	public ItemStack getSmeltingResult(ItemStack itemstack) {
	    ItemStack stack = null;
        if (itemstack != null && recipes.containsKey(itemstack.getItem()))
            stack = recipes.get(itemstack.getItem()).getOutput(itemstack);
        return stack;
	}

	public int getSmeltingTime(ItemStack itemstack) {
        if (itemstack != null && recipes.containsKey(itemstack.getItem()))
            return recipes.get(itemstack.getItem()).CookTime;
		return 0;
	}

    private class FRecipe {
        private ItemStack output = null;
        private ItemStack input = null;
        private int CookTime = 120;
        public FRecipe(ItemStack output, ItemStack input, int CookTime) {
            this.output = output;
            this.input = input;
            this.CookTime = CookTime;
        }

        public ItemStack getOutput(ItemStack stack) {
            if (stack.getItem() == input.getItem() &&
                    stack.getUnlocalizedName().equals(input.getUnlocalizedName()) &&
                    stack.getItemDamage() == input.getItemDamage())
            return output;
            return null;
        }
    }
}
