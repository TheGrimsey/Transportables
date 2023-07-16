package net.thegrimsey.transportables.mixin;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.thegrimsey.transportables.interfaces.HasCarriageInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractHorseEntity.class)
public class HorseBaseEntityMixin implements HasCarriageInterface {
    @Unique
    boolean hasCarriage = false;


    @Override
    public boolean hasCarriage() {
        return hasCarriage;
    }

    @Override
    public void setHasCarriage(boolean hasCarriage) {
        this.hasCarriage = hasCarriage;
    }
}
