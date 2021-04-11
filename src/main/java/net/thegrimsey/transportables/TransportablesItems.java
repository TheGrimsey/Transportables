package net.thegrimsey.transportables;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.thegrimsey.transportables.items.RailLinker;

public class TransportablesItems {
    public static final RailLinker RAILLINKER = new RailLinker();

    public static void RegisterItems()
    {
        RegisterItem("raillinker", RAILLINKER);
    }

    private static void RegisterItem(String Id, Item item)
    {
        Registry.register(Registry.ITEM, new Identifier(Transportables.MODID, Id.toLowerCase()), item);
    }
}
