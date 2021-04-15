package net.thegrimsey.transportables;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.thegrimsey.transportables.items.Linker;

public class TransportablesItems {
    public static final Linker LINKER = new Linker();

    public static void RegisterItems()
    {
        RegisterItem("linker", LINKER);
    }

    private static void RegisterItem(String Id, Item item)
    {
        Registry.register(Registry.ITEM, new Identifier(Transportables.MODID, Id.toLowerCase()), item);
    }
}
