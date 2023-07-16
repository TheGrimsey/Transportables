package net.thegrimsey.transportables;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.thegrimsey.transportables.items.CarriageItem;
import net.thegrimsey.transportables.items.ChestCarriageItem;
import net.thegrimsey.transportables.items.LinkerItem;

public class TransportablesItems {
    public static final LinkerItem LINKER_ITEM = new LinkerItem();
    public static final CarriageItem CARRIAGE_ITEM = new CarriageItem();
    public static final ChestCarriageItem CHEST_CARRIAGE_ITEM = new ChestCarriageItem();

    public static void RegisterItems() {
        RegisterItem("linker", LINKER_ITEM);
        RegisterItem("carriage", CARRIAGE_ITEM);
        RegisterItem("chest_carriage", CHEST_CARRIAGE_ITEM);
    }

    private static void RegisterItem(String Id, Item item) {
        Registry.register(Registries.ITEM, new Identifier(Transportables.MODID, Id.toLowerCase()), item);
    }
}
