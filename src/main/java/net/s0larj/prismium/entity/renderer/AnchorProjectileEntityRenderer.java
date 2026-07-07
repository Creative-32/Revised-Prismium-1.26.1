package net.s0larj.prismium.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.entity.custom.AnchorProjectileEntity;
import net.s0larj.prismium.entity.model.ModEntityModelLayers;
import net.s0larj.prismium.entity.state.AnchorProjectileEntityRenderState;
import net.s0larj.prismium.entity.model.AnchorProjectileEntityModel;

public class AnchorProjectileEntityRenderer extends EntityRenderer<AnchorProjectileEntity, AnchorProjectileEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "textures/entity/mini_golem.png");
    private final AnchorProjectileEntityModel model;

    public AnchorProjectileEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new AnchorProjectileEntityModel(context.bakeLayer(ModEntityModelLayers.ANCHOR_PROJECTILE));
    }

    @Override
    public AnchorProjectileEntityRenderState createRenderState() {
        return new AnchorProjectileEntityRenderState();
    }
}
