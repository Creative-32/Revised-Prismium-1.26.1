package net.s0larj.prismium.entity.model;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;
import net.s0larj.prismium.Prismium;

public class AnchorProjectileEntityModel <T extends Entity> extends EntityModel<EntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "anchor_projectile.png"), "main");
	public final ModelPart bb_main;

	public AnchorProjectileEntityModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-0.7F, -11.0F, -0.5F, 1.4F, 9.0F, 1.4F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.3F, -10.4F, -0.7F, 4.6F, 1.0F, 1.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.9F, -12.5F, -0.5F, 0.4F, 1.8F, 1.4F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.9F, -12.5F, -0.5F, 1.8F, 0.4F, 1.4F, new CubeDeformation(0.01F))
		.texOffs(0, 0).addBox(0.5F, -12.5F, -0.5F, 0.4F, 1.8F, 1.4F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.9F, -11.1F, -0.5F, 1.8F, 0.4F, 1.4F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4901F, -3.1557F, -0.8F, 0.0F, 0.0F, 0.7418F));

		PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.9647F, -4.5069F, -0.8F, 0.0F, 0.0F, -0.7418F));

		PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(0.01F, -1.01F, -0.29F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.6946F, -1.9869F, -0.51F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-0.7F, -2.0F, -0.8F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.1F, -2.1F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, -1.0F, -0.8F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

}