package net.thegrimsey.transportables;

import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.MinecartItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.thegrimsey.transportables.entity.CarriageEntity;
import net.thegrimsey.transportables.items.CarriageItem;
import net.thegrimsey.transportables.items.LinkerItem;

public class TransportablesItems {
    public static final LinkerItem LINKER_ITEM = new LinkerItem();
    public static final CarriageItem CARRIAGE_ITEM = new CarriageItem();

    public static void RegisterItems()
    {
        RegisterItem("linker", LINKER_ITEM);
        RegisterItem("carriage", CARRIAGE_ITEM);
    }

    private static void RegisterItem(String Id, Item item)
    {
        Registry.register(Registry.ITEM, new Identifier(Transportables.MODID, Id.toLowerCase()), item);
    }
}
