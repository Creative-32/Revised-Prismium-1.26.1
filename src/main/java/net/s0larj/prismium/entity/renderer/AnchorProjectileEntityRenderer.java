package net.s0larj.prismium.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.entity.custom.AnchorProjectileEntity;
import net.s0larj.prismium.entity.model.ModEntityModelLayers;
import net.s0larj.prismium.entity.state.AnchorProjectileEntityRenderState;
import net.s0larj.prismium.entity.model.AnchorProjectileEntityModel;

public class AnchorProjectileEntityRenderer extends EntityRenderer<AnchorProjectileEntity, AnchorProjectileEntityRenderState> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "textures/entity/anchor_projectile.png");
    private final AnchorProjectileEntityModel model;

    public AnchorProjectileEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new AnchorProjectileEntityModel(context.bakeLayer(ModEntityModelLayers.ANCHOR_PROJECTILE));
    }

    @Override
    public void submit(AnchorProjectileEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.hooked){
            return;
        }
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot+180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));
        submitNodeCollector.submitModel(this.model, state, poseStack, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, (ModelFeatureRenderer.CrumblingOverlay)null);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public AnchorProjectileEntityRenderState createRenderState() {
        return new AnchorProjectileEntityRenderState();
    }

    public void extractRenderState(final AnchorProjectileEntity entity, final AnchorProjectileEntityRenderState state, final float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (entity.isHooked()){
            state.hooked = true;
        }
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.shake = (float)entity.shakeTime - partialTicks;
    }

}
