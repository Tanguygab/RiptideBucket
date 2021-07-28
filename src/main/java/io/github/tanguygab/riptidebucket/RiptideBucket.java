package io.github.tanguygab.riptidebucket;

import org.bukkit.plugin.java.JavaPlugin;

public final class RiptideBucket extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(this),this);
    }


}
