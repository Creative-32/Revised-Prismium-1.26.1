package net.s0larj.prismium.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.attachment.AnchorAttachment;

import java.util.List;


@Environment(EnvType.CLIENT)
public abstract class ProjectileEntityLayer<M extends LivingEntityRenderState, S> extends RenderLayer<M, EntityModel<EntityRenderState>> {
    //Model that will be rendered on the entity.
    private final Model<S> model;
    // Render state used by the anchor model.
    private final S modelState;
    // Texture used by the anchor model.
    private final Identifier texture;
    //Currently unused, but kept in case different placement behavior is added later.
    private final PlacementStyle placementStyle;

    public ProjectileEntityLayer(LivingEntityRenderer<?, M, EntityModel<EntityRenderState>> renderer, Model<S> model, S modelState,
            Identifier texture, PlacementStyle placementStyle) {
        super(renderer);
        this.model = model;
        this.modelState = modelState;
        this.texture = texture;
        this.placementStyle = placementStyle;
    }

    // The subclass provides the list of anchors currently attached to the rendered entity.
    protected abstract List<AnchorAttachment> numStuck(LivingEntityRenderState state);


    // This method points a model in the direction represented by  directionX, directionY and directionZ.
    // It is not currently called by submit(), but it can be used for arrow-style directional placement.
    private void submitStuckItem(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            float directionX,
            float directionY,
            float directionZ,
            int outlineColor
    ) {

        // Horizontal length of the direction vector.
        float horizontalLength = Mth.sqrt(directionX * directionX + directionZ * directionZ);

        // Calculate horizontal rotation from the direction vector.
        float calculatedYRot = (float) ( Math.atan2(directionX, directionZ) * (180.0F / Math.PI));

        // Calculate vertical rotation from the direction vector.
        float calculatedXRot = (float) ( Math.atan2(directionY, horizontalLength) * (180.0F / Math.PI));

        // Rotate the model horizontally.
        poseStack.mulPose(Axis.YP.rotationDegrees(calculatedYRot + 90.0F));

        // Rotate the model vertically.
        poseStack.mulPose(Axis.ZP.rotationDegrees(calculatedXRot));


        // Submit the model for rendering.
        submitNodeCollector.submitModel(
                this.model,
                this.modelState,
                poseStack,
                this.texture,
                lightCoords,
                OverlayTexture.NO_OVERLAY,
                outlineColor,
                (ModelFeatureRenderer.CrumblingOverlay) null
        );
    }


    public void submit(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            LivingEntityRenderState state,
            float yRot,
            float xRot
    ) {

        List<AnchorAttachment> projectiles = this.numStuck(state);

        if (projectiles.isEmpty()) {
            return;
        }

        for (AnchorAttachment projectile : projectiles) {

            poseStack.pushPose();

            Vec3 localPosition = projectile.pos();

            // Convert the Y position saved from the entity's feet into the living model's render coordinate system.
            double renderY = state.boundingBoxHeight - localPosition.y - .6D;

            // Move the model to the hit location.
            poseStack.translate(
                    localPosition.x,
                    renderY,
                    -localPosition.z
            );

            // Rotate horizontally.
            poseStack.mulPose(Axis.YP.rotationDegrees(-projectile.yRot()));

            // Rotate vertically.
            poseStack.mulPose(Axis.XP.rotationDegrees(projectile.xRot()));

            submitNodeCollector.submitModel(
                    this.model,
                    this.modelState,
                    poseStack,
                    this.texture,
                    lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    state.outlineColor,
                    null
            );

            poseStack.popPose();
        }
    }


    @Environment(EnvType.CLIENT)
    public enum PlacementStyle {
        IN_CUBE,
        ON_SURFACE
    }
}

