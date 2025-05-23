package net.pawlrip.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.pawlrip.StSMain;
import net.pawlrip.entity.custom.LumosEntity;
import net.pawlrip.entity.custom.PortkeyEntity;
import net.pawlrip.entity.custom.RayEntity;


public class StSEntities {

    public static final EntityType<RayEntity> STUPEFY_RAY = registerEntity("stupefy_ray",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RayEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.4F)).build());

    public static final EntityType<RayEntity> EXPELLIARMUS_RAY = registerEntity("expelliarmus_ray",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RayEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.4F)).build());

    public static final EntityType<RayEntity> PETRIFICUS_TOTALUS_RAY = registerEntity("petrificus_totalus_ray",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RayEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.4F)).build());

    public static final EntityType<RayEntity> ALARTE_ASCENDARE_RAY = registerEntity("alarte_ascendare_ray",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RayEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.4F)).build());

    public static final EntityType<RayEntity> MELOFORS_RAY = registerEntity("melofors_ray",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RayEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.4F)).build());

    public static final EntityType<RayEntity> FLIPENDO_RAY = registerEntity("flipendo_ray",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RayEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.4F)).build());

    public static final EntityType<RayEntity> PROTEGO_WALL = registerEntity("protego_wall",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RayEntity::new).dimensions(EntityDimensions.fixed(1.5F, 2.25F)).build());

    public static final EntityType<LumosEntity> LUMOS_ORB = registerEntity("lumos_orb",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LumosEntity::new).dimensions(EntityDimensions.fixed(0.1F, 0.1F)).build());

    public static final EntityType<PortkeyEntity> PORTKEY = registerEntity("portkey",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PortkeyEntity::new).dimensions(EntityDimensions.fixed(0.1F, 0.1F)).build());

    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType<T> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, StSMain.id(name), entityType);
    }

    public static void registerModEntities() {
        StSMain.LOGGER.info("Registering Mod Entities for " + StSMain.MOD_NAME);
    }
}
