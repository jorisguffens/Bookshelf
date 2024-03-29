package com.gufli.bookshelf.bukkit;

import com.gufli.bookshelf.scheduler.AbstractScheduler;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Executor;

public class BukkitScheduler extends AbstractScheduler {

    private final Executor sync;

    public BukkitScheduler(Plugin plugin) {
        super(plugin.getName());
        this.sync = r -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, r);
    }

    public void shutdown() {
        scheduler.shutdown();
        worker.shutdown();
    }

    @Override
    public Executor sync() {
        return this.sync;
    }

}
