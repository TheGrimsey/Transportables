package net.thegrimsey.transportables;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Transportables.MODID)
public class TransportablesConfig implements ConfigData {
    @Comment("Maximum teleport distance for TeleSender rail in blocks. (Integer: 0-2147483648. Default: 30)")
    public int TELESENDER_RANGE = 30;

    @Comment("Maximum distance for linking carriages. (Integer: 0-2147483648. Default: 6)")
    public int CARRIAGE_LINK_RANGE = 6;
}
