package com.dongle.FMCBridge;

import java.util.logging.Level;

import com.dongle.proxy.CommonProxy;

import net.minecraftforge.common.config.Configuration;

public class Config {
    private static final String CATEGORY_AUTH = "authentication";

    // This values below you can access elsewhere in your mod:
    public static String username = "username";
    public static String password = "password";

    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
    // exist yet and read the values if it does exist.
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initAuthConfig(cfg);

        } catch (Exception e1) {
            
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initAuthConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_AUTH, "Authentication");
        // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.
        username = cfg.getString("username", CATEGORY_AUTH, username, "The username used to authorize the bridge to have access to this mod");
        password = cfg.getString("realName", CATEGORY_AUTH, password, "The password used to authorize the bridge to have access to this mod");
    }

}
