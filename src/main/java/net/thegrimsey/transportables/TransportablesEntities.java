package net.thegrimsey.transportables;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.thegrimsey.transportables.entity.CarriageEntity;
import net.thegrimsey.transportables.entity.ChestCarriageEntity;

public class TransportablesEntities {

    public static EntityType<CarriageEntity> CARRIAGE;
    public static EntityType<ChestCarriageEntity> CHEST_CARRIAGE;

    public static void RegisterEntities() {
        // If we add more entities this should be refactored to use a wildcard function.
        CARRIAGE = Registry.register(Registries.ENTITY_TYPE, new Identifier(Transportables.MODID, "carriage"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, CarriageEntity::new).dimensions(CarriageEntity.CARRIAGE_DIMENSIONS).build());
        FabricDefaultAttributeRegistry.register(CARRIAGE, CarriageEntity.createLivingAttributes());

        CHEST_CARRIAGE = Registry.register(Registries.ENTITY_TYPE, new Identifier(Transportables.MODID, "chest_carriage"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, ChestCarriageEntity::new).dimensions(CarriageEntity.CARRIAGE_DIMENSIONS).build());
        FabricDefaultAttributeRegistry.register(CHEST_CARRIAGE, ChestCarriageEntity.createLivingAttributes());
    }
}
