package tonius.simplyjetpacks.event;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import tonius.simplyjetpacks.item.jetpack.ItemJetpack;

public class JetpackMobHandler {

    private static Random rand = new Random();

    @ForgeSubscribe
    public void makeMobUseJetpackRandomly(LivingUpdateEvent evt) {
        if (!(evt.entityLiving instanceof EntityPlayer)) {
            ItemStack armor = evt.entityLiving.getCurrentItemOrArmor(3);
            if (armor != null && armor.getItem() instanceof ItemJetpack) {
                Item jetpack = armor.getItem();
                if (rand.nextInt(2) == 0) {
                    ((ItemJetpack) jetpack).useJetpack(evt.entityLiving, armor, false);
                }
                if (evt.entityLiving.posY > evt.entityLiving.worldObj.getActualHeight() + 10) {
                    evt.entityLiving.attackEntityFrom(DamageSource.generic, 80);
                }
            }
        }
    }

}
