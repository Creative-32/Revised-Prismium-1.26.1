package net.s0larj.prismium.entity;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.s0larj.prismium.entity.model.ModEntityModelLayers;
import net.s0larj.prismium.entity.renderer.AnchorProjectileEntityRenderer;

public class ModCustomEntityClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntityModelLayers.registerModelLayers();
        // #endregion register_client
        // #region register_renderer
        EntityRenderers.register(ModEntityTypes.ANCHOR_PROJECTILE, AnchorProjectileEntityRenderer::new);
        // #endregion register_renderer
        // #region register_client
    }
}