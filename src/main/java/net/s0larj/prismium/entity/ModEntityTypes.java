package net.s0larj.prismium.entity;

import net.minecraft.world.entity.EntityType;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.entity.custom.AnchorProjectileEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;

public class ModEntityTypes {

    public static final EntityType<AnchorProjectileEntity> ANCHOR_PROJECTILE = registerEntity(
            "anchor_projectile",
            EntityType.Builder.<AnchorProjectileEntity>of(AnchorProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f,0.5f)
    );

    private static <T extends Entity> EntityType<T> registerEntity (String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Prismium.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerModEntityTypes() {
        Prismium.LOGGER.info("Registering EntityTypes for " + Prismium.MOD_ID);
    }

    public static void registerAttributes() {
        //FabricDefaultAttributeRegistry.register(ANCHOR, ANCHOR.);
    }

}
