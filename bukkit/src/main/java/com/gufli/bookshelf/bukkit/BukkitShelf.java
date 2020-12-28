package com.gufli.bookshelf.bukkit;

import com.gufli.bookshelf.bukkit.events.DefaultEventListener;
import com.gufli.bookshelf.bukkit.events.MainEventListener;
import com.gufli.bookshelf.bukkit.gui.InventoryListener;
import com.gufli.bookshelf.bukkit.server.BukkitServer;
import com.gufli.bookshelf.bukkit.server.ConnectionListener;
import com.gufli.bookshelf.events.EventManager;
import com.gufli.bookshelf.server.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitShelf extends JavaPlugin {

    public final BukkitServer server;

    public BukkitShelf() {
        this.server = new BukkitServer();
        Server.register(this.server);
    }

    @Override
    public void onEnable() {

        // Hook custom event manager
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MainEventListener(), this);

        // Register default events with custom event manager
        EventManager.register(new ConnectionListener(this));
        EventManager.register(new DefaultEventListener());
        EventManager.register(new InventoryListener());


        getLogger().info("Enabled " + getDescription().getName() + " v" + getDescription().getVersion() + ".");
    }

}
