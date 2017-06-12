package me.yamakaja.rpgpets.plugin;

import com.comphenix.protocol.ProtocolLibrary;
import me.yamakaja.rpgpets.api.NMSHandler;
import me.yamakaja.rpgpets.api.RPGPets;
import me.yamakaja.rpgpets.api.config.ConfigManager;
import me.yamakaja.rpgpets.api.entity.PetType;
import me.yamakaja.rpgpets.plugin.command.CommandRPGPets;
import me.yamakaja.rpgpets.plugin.protocol.EntitySpawnPacketTranslator;
import me.yamakaja.rpgpets.v1_11_R1.NMSHandler_v1_11_R1;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * Created by Yamakaja on 10.06.17.
 */
@SuppressWarnings("unused")
public class RPGPetsImpl extends JavaPlugin implements RPGPets {

    private NMSHandler nmsHandler;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.getCommand("rpgpets").setExecutor(new CommandRPGPets(this));

        if (!this.loadNMSHandler()) {
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        this.getLogger().info("Loaded for NMS version " + this.getNMSHandler().getNMSVersion() + "!");

        ProtocolLibrary.getProtocolManager().addPacketListener(new EntitySpawnPacketTranslator(this));

        this.registerPets();
        this.getLogger().info("Registered pet entities!");

        this.configManager = new ConfigManager(this);
        this.configManager.injectConfigs();
        this.getLogger().info("Configs loaded!");

        this.getLogger().info("Successfully enabled RPGPets!");
    }

    private void registerPets() {
        for (PetType petType : PetType.values())
            this.getNMSHandler().getPetRegistry().registerEntity(petType.getEntityId(), petType.getBaseType(), petType.getEntityClass(), petType.getEntityName());
    }

    /**
     * Loads the {@link NMSHandler} for the current version
     *
     * @return Whether loading a suitable handler was successful
     */
    private boolean loadNMSHandler() {
        String nmsVersion;
        try {
            nmsVersion = Bukkit.getServer().getClass().getPackage().getName().split(Pattern.quote("."))[3];
        } catch (Exception ex) {
            this.getLogger().log(Level.SEVERE, "An error occurred while determining server version! Disabling plugin ...", ex);
            return false;
        }

        switch (nmsVersion) {
            case "v1_11_R1":
                nmsHandler = new NMSHandler_v1_11_R1(this);
                break;
            default:
                this.getLogger().severe("*****************************************************");
                this.getLogger().severe("Unsupported version: \"" + nmsVersion + "\". Disabling plugin!");
                this.getLogger().severe("*****************************************************");
                return false;
        }
        return true;
    }

    @Override
    public NMSHandler getNMSHandler() {
        return nmsHandler;
    }

    @Override
    public ConfigManager getConfigManager() {
        return configManager;
    }

}