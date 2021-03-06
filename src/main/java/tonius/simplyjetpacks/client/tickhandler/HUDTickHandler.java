package tonius.simplyjetpacks.client.tickhandler;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import tonius.simplyjetpacks.item.jetpack.ItemJetpack;
import tonius.simplyjetpacks.util.StringUtils;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class HUDTickHandler implements ITickHandler {

    private static Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void tickStart(EnumSet type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet type, Object... tickData) {
        EntityPlayer player = mc.thePlayer;
        if (player != null) {
            if ((mc.currentScreen == null || mc.currentScreen instanceof GuiChat) && !mc.gameSettings.hideGUI) {
                ItemStack chestplate = player.getCurrentArmor(2);
                if (chestplate != null && chestplate.getItem() instanceof ItemJetpack) {
                    int tier = ((ItemJetpack) chestplate.getItem()).getTier();
                    if (tier < 1 || tier > 4) {
                        return;
                    }
                    int energy = ((ItemJetpack) chestplate.getItem()).getEnergyStored(chestplate);
                    int maxEnergy = ((ItemJetpack) chestplate.getItem()).getMaxEnergyStored(chestplate);
                    int percent = (int) Math.round(((double) energy / (double) maxEnergy) * 100D);
                    mc.entityRenderer.setupOverlayRendering();
                    mc.fontRenderer.drawString(StringUtils.getHUDEnergyText(percent, energy), 5, 5, 255 | 255 << 8 | 255 << 16, true);
                    if (percent > 0 && percent <= 10) {
                        mc.fontRenderer.drawString(StringUtils.getHUDEnergyLowText(), 5, 15, 255 | 255 << 8 | 255 << 16, true);
                    } else if (percent == 0) {
                        mc.fontRenderer.drawString(StringUtils.getHUDEnergyEmptyText(), 5, 15, 255 | 255 << 8 | 255 << 16, true);
                    }
                }
            }
        }
    }

    @Override
    public EnumSet ticks() {
        return EnumSet.of(TickType.RENDER);
    }

    @Override
    public String getLabel() {
        return "SJRenderHUD";
    }

}
