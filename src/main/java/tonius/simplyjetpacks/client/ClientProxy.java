package tonius.simplyjetpacks.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import tonius.simplyjetpacks.CommonProxy;
import tonius.simplyjetpacks.client.event.SoundLoader;
import tonius.simplyjetpacks.client.tickhandler.ClientTickHandler;
import tonius.simplyjetpacks.client.tickhandler.HUDTickHandler;
import tonius.simplyjetpacks.client.tickhandler.KeyHandlerSJ;
import tonius.simplyjetpacks.client.util.ParticleUtils;
import tonius.simplyjetpacks.config.MainConfig;
import tonius.simplyjetpacks.util.Vector3;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

    private static Minecraft mc = Minecraft.getMinecraft();
    private static Random rand = new Random();

    @Override
    public void registerHandlers() {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new SoundLoader());
        TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
        if (MainConfig.enableEnergyHUD) {
            TickRegistry.registerTickHandler(new HUDTickHandler(), Side.CLIENT);
        }
        KeyBindingRegistry.registerKeyBinding(new KeyHandlerSJ());
    }

    @Override
    public void sendPacketToServer(int packetType, int int1) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(8);
        DataOutputStream data = new DataOutputStream(bytes);
        try {
            data.writeInt(packetType);
            data.writeInt(int1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "SmpJet";
        packet.data = bytes.toByteArray();
        packet.length = bytes.size();
        PacketDispatcher.sendPacketToServer(packet);
    }

    @Override
    public void sendPacketToServer(int packetType, boolean... keys) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream(8);
        DataOutputStream data = new DataOutputStream(bytes);
        try {
            data.writeInt(packetType);
            for (boolean key : keys) {
                data.writeBoolean(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "SmpJet";
        packet.data = bytes.toByteArray();
        packet.length = bytes.size();
        PacketDispatcher.sendPacketToServer(packet);
    }

    @Override
    public void showJetpackParticles(World world, int entityID, int particle) {
        if (MainConfig.enableJetpackParticles) {
            Entity entity = world.getEntityByID(entityID);
            if (entity != null && entity instanceof EntityLivingBase) {
                EntityLivingBase wearer = (EntityLivingBase) entity;
                Vector3 playerPos = new Vector3(wearer);

                if (!(wearer.equals(mc.thePlayer))) {
                    playerPos.translate(new Vector3(0, 1.50, 0));
                }

                Vector3 vLeft = new Vector3();
                vLeft.z -= 0.38;
                vLeft.x -= 0.28;
                vLeft.rotate(wearer.renderYawOffset);
                vLeft.y -= 1.0;

                Vector3 vRight = new Vector3();
                vRight.z -= 0.38;
                vRight.x += 0.28;
                vRight.rotate(wearer.renderYawOffset);
                vRight.y -= 1.0;

                Vector3 vCenter = new Vector3();
                vCenter.z -= 0.30;
                vCenter.x = (rand.nextFloat() - 0.5F) * 0.25F;
                vCenter.rotate(wearer.renderYawOffset);
                vCenter.y -= 1.05;

                vLeft = Vector3.translate(vLeft.clone(), new Vector3(-wearer.motionX, -wearer.motionY, -wearer.motionZ));
                vRight = Vector3.translate(vRight.clone(), new Vector3(-wearer.motionX, -wearer.motionY, -wearer.motionZ));
                vCenter = Vector3.translate(vCenter.clone(), new Vector3(-wearer.motionX, -wearer.motionY, -wearer.motionZ));

                Vector3 v = new Vector3(playerPos).translate(vLeft);
                ParticleUtils.spawnParticle(particle, world, v.x, v.y, v.z, 0, -0.2, 0);

                v = new Vector3(playerPos).translate(vRight);
                ParticleUtils.spawnParticle(particle, world, v.x, v.y, v.z, 0, -0.2, 0);

                v = new Vector3(playerPos).translate(vCenter);
                ParticleUtils.spawnParticle(particle, world, v.x, v.y, v.z, 0, -0.2, 0);
            }
        }
    }

}
