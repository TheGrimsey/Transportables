package net.thegrimsey.transportables;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.thegrimsey.transportables.entity.CarriageEntity;

public class TransportablesEntities {

    public static EntityType<CarriageEntity> CARRIAGE;

    public static void RegisterEntities()
    {
        CARRIAGE = Registry.register(Registry.ENTITY_TYPE, new Identifier(Transportables.MODID, "carriage"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CarriageEntity::new).dimensions(EntityDimensions.fixed(2.5f,1.5f)).build());
        FabricDefaultAttributeRegistry.register(CARRIAGE, CarriageEntity.createLivingAttributes());
    }
}
