package net.thegrimsey.transportables;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Transportables.MODID)
public class TransportablesConfig implements ConfigData {
    @Comment("Maximum teleport distance for TeleSender rail in blocks. (-1-3000000000. Default: 30, Set to -1 to disable.).")
    public long TELESENDER_RANGE = 30;

    @Comment("Maximum distance for linking carriages. (-1-3000000000. Default: 6). Set to -1 to disable.")
    public long CARRIAGE_LINK_RANGE = 6;
}
