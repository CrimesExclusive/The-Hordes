package net.smileycorp.hordes.integration.jei;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class InfectionCureWrapper implements IRecipeWrapper {

	private final List<ItemStack> items;

	public InfectionCureWrapper(List<ItemStack> items) {
		this.items = items;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(VanillaTypes.ITEM, items);
	}

}
