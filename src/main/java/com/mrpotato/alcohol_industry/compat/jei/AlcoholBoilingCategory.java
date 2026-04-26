package com.mrpotato.alcohol_industry.compat.jei;

import com.mrpotato.alcohol_industry.AlcoholIndustry;
import com.mrpotato.alcohol_industry.recipe.AlcoholBoilingRecipe;
import com.mrpotato.alcohol_industry.registry.ModItems;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class AlcoholBoilingCategory implements IRecipeCategory<AlcoholBoilingRecipe> {

    public static final RecipeType<AlcoholBoilingRecipe> RECIPE_TYPE =
        RecipeType.create(AlcoholIndustry.MOD_ID, "alcohol_boiling", AlcoholBoilingRecipe.class);

    private final IDrawable icon;
    private final Component title;

    public AlcoholBoilingCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, 
            new ItemStack(ModItems.ALCOHOL_BOILER_ITEM.get()));
        this.title = Component.translatable("alcohol_industry.jei.alcohol_boiling");
    }

    @Override
    public RecipeType<AlcoholBoilingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return 160;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlcoholBoilingRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients_();
        for (int i = 0; i < ingredients.size(); i++) {
            int row = i / 2;
            int col = i % 2;
            builder.addSlot(RecipeIngredientRole.INPUT, 4 + col * 20, 4 + row * 20)
                .addIngredients(ingredients.get(i));
        }

        FluidStack fluidInput = recipe.getFluidIngredient();
        if (!fluidInput.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 52, 10)
                .addFluidStack(fluidInput.getFluid(), fluidInput.getAmount())
                .setFluidRenderer(fluidInput.getAmount(), false, 16, 50);
        }

        FluidStack fluidOutput = recipe.getFluidResult();
        if (!fluidOutput.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 10)
                .addFluidStack(fluidOutput.getFluid(), fluidOutput.getAmount())
                .setFluidRenderer(fluidOutput.getAmount(), false, 16, 50);
        }
    }

    @Override
    public void draw(AlcoholBoilingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        
        guiGraphics.drawString(font, "→", 85, 30, 0xFF555555, false);
        
        HeatCondition heat = recipe.getRequiredHeat();
        String heatText = "Heat: " + heat.getSerializedName();
        int heatColor = switch (heat) {
            case SUPERHEATED -> 0xFFFF55FF;
            case HEATED -> 0xFFFFAA00;
            default -> 0xFF888888;
        };
        guiGraphics.drawString(font, heatText, 4, 68, heatColor, false);
        
        FluidStack fluidInput = recipe.getFluidIngredient();
        if (!fluidInput.isEmpty()) {
            guiGraphics.drawString(font, fluidInput.getAmount() + "mB", 48, 62, 0xFF666666, false);
        }
        
        FluidStack fluidOutput = recipe.getFluidResult();
        if (!fluidOutput.isEmpty()) {
            guiGraphics.drawString(font, fluidOutput.getAmount() + "mB", 116, 62, 0xFF666666, false);
        }
    }
}
