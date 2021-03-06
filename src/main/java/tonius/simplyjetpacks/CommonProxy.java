package tonius.simplyjetpacks;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import tonius.simplyjetpacks.config.MainConfig;
import tonius.simplyjetpacks.event.JetpackMobHandler;

public class CommonProxy {

    public void registerHandlers() {
        if (MainConfig.mobsUseJetpacks) {
            MinecraftForge.EVENT_BUS.register(new JetpackMobHandler());
        }
    }

    public void sendPacketToServer(int packetType, int int1) {
    }

    public void sendPacketToServer(int packetType, boolean... booleans) {
    }

    public void showJetpackParticles(World world, int entityID, int particle) {
    }

}
