package net.s0larj.prismium.attachment;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.entity.custom.AnchorProjectileEntity;

public class ModAttachments {

    public static final AttachmentType<Boolean> HOOKED = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "hooked") // The ID of your Attachment
    );
}
