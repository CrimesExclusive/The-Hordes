package net.smileycorp.hordes.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.smileycorp.hordes.common.ModDefinitions;

public class InfectionCureCategory implements IRecipeCategory<InfectionCureWrapper> {

	public static final RecipeType<InfectionCureWrapper> TYPE = RecipeType.create(ModDefinitions.MODID, "infection_cures", InfectionCureWrapper.class);

	private final IDrawable background;
	private final IDrawable icon;

	public static final ResourceLocation TEXTURE = ModDefinitions.getResource("textures/gui/jei/cure_list.png");

	public InfectionCureCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(TEXTURE, 0, 0, 167, 113);
		icon = guiHelper.createDrawable(TEXTURE, 168, 0, 18, 18);
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public MutableComponent getTitle() {
		return MutableComponent.create(new TranslatableContents("jei.category.hordes.InfectionCures"));
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder recipeLayout, InfectionCureWrapper recipe, IFocusGroup focuses) {
		for (int i = 0; i <9; i++) {
			for (int j = 0; j <6; j++) {
				recipeLayout.addSlot(RecipeIngredientRole.INPUT, i*18+3, j*18+3).addItemStack(recipe.getItem(i, j));
			}
		}
	}

	@Override
	public void draw(InfectionCureWrapper recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		Minecraft mc = Minecraft.getInstance();
		Font font = mc.font;
		MutableComponent text = MutableComponent.create(new TranslatableContents("jei.category.hordes.InfectionCures"))
				.setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(0x440002)));
		font.draw(stack, text, 0, 0, 0);
		font.drawShadow(stack, text, 0, 0, 0);
	}

	@Override
	public RecipeType<InfectionCureWrapper> getRecipeType() {

		return null;
	}

}