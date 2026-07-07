package net.s0larj.prismium.entity.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.s0larj.prismium.entity.model.AnchorProjectileEntityModel;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.s0larj.prismium.Prismium;

// #region model_layer
public class ModEntityModelLayers {
    public static final ModelLayerLocation ANCHOR_PROJECTILE = createMain("anchor_projectile");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(Prismium.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.ANCHOR_PROJECTILE, AnchorProjectileEntityModel::createBodyLayer);
    }
}
