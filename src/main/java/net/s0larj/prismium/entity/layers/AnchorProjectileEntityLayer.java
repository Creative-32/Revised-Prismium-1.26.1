package net.s0larj.prismium.entity.layers;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.s0larj.prismium.entity.model.AnchorProjectileEntityModel;
import net.s0larj.prismium.entity.renderer.AnchorProjectileEntityRenderer;
import net.s0larj.prismium.entity.state.AnchorProjectileEntityRenderState;

public class AnchorProjectileEntityLayer<M extends PlayerModel> extends StuckInBodyLayer<M, AnchorProjectileEntityRenderState> {

    public AnchorProjectileEntityLayer(LivingEntityRenderer<?, AvatarRenderState, M> renderer,  final EntityRendererProvider.Context context) {
        super(renderer, new AnchorProjectileEntityModel<>(context.bakeLayer(ModelLayers.ARROW)), new AnchorProjectileEntityRenderState(), AnchorProjectileEntityRenderer.TEXTURE, PlacementStyle.IN_CUBE);
    }

    @Override
    protected int numStuck(AvatarRenderState state) {
        return 0;
    }

}
