package net.s0larj.prismium;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EntityType;
import net.s0larj.prismium.block.ModBlocks;
import net.s0larj.prismium.creativemodetab.ModCreativeModeTabs;
import net.s0larj.prismium.data.ModDataComponents;
import net.s0larj.prismium.entity.ModEntityTypes;
import net.s0larj.prismium.entity.layers.AnchorProjectileEntityLayer;
import net.s0larj.prismium.entity.model.ModEntityModelLayers;
import net.s0larj.prismium.entity.renderer.AnchorProjectileEntityRenderer;
import net.s0larj.prismium.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Prismium implements ModInitializer {
	public static final String MOD_ID = "prismium";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModCreativeModeTabs.registerModCreativeModeTabs();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEntityTypes.registerModEntityTypes();
		ModDataComponents.registerDataCompontents();
		ModEntityModelLayers.registerModelLayers();

		LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			registrationHelper.register(new );
		});
	}

}
