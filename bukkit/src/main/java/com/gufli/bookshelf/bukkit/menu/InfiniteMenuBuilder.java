package com.gufli.bookshelf.bukkit.menu;

import com.gufli.bookshelf.menu.MenuItemCallback;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class InfiniteMenuBuilder {

    public static InfiniteMenuBuilder create() {
        return new InfiniteMenuBuilder();
    }

    public static InfiniteMenuBuilder create(String title) {
        return new InfiniteMenuBuilder().withTitle(title);
    }

    //

    private String title;

    private ItemStack previousItem;
    private ItemStack nextItem;

    private int itemAmount;
    private Function<Integer, BukkitMenuItem> supplier;

    protected final Map<Integer, BukkitMenuItem> hotbar = new HashMap<>();

    private InfiniteMenuBuilder() {
        previousItem = ItemStackBuilder.of(Material.ARROW)
                .withName(ChatColor.GREEN + "Previous page")
                .build();

        nextItem = ItemStackBuilder.of(Material.ARROW)
                .withName(ChatColor.GREEN + "Next page")
                .build();
    }

    //

    public final InfiniteMenuBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public InfiniteMenuBuilder withNextItem(ItemStack item) {
        this.nextItem = item;
        return this;
    }

    public InfiniteMenuBuilder withPreviousItem(ItemStack item) {
        this.previousItem = item;
        return this;
    }

    public InfiniteMenuBuilder withItemAmount(int amount) {
        this.itemAmount = amount;
        return this;
    }

    public InfiniteMenuBuilder withItemSupplier(Function<Integer, BukkitMenuItem> supplier) {
        this.supplier = supplier;
        return this;
    }

    public final InfiniteMenuBuilder withHotbarItem(int slot, ItemStack item) {
        hotbar.put(slot, new BukkitMenuItem(item));
        return this;
    }

    public final InfiniteMenuBuilder withHotbarItem(int slot, ItemStack item, MenuItemCallback cb) {
        hotbar.put(slot, new BukkitMenuItem(item, cb));
        return this;
    }

    public BukkitMenu build() {
        if ( supplier == null ) {
            throw new IllegalStateException("Supplier may not be null.");
        }

        int size = (itemAmount / 9) + (itemAmount % 9 > 0 ? 1 : 0);

        if ( !hotbar.isEmpty() ) {
            size += 2;
        }

        // no need for pagination
        if ( size <= 6 ) {
            BukkitMenu menu = new BukkitMenu(size * 9, title);
            for ( int i = 0; i < itemAmount; i++ ) {
                menu.setItem(i, supplier.apply(i));
            }

            // fill hotbar
            for ( int slot : hotbar.keySet() ) {
                if ( slot < 0 || slot >= 9 ) {
                    continue;
                }

                menu.setItem(((size-1) * 9) + slot, hotbar.get(slot));
            }

            return menu;
        }

        return page(0);
    }

    private BukkitMenu page(int page) {
        int rows = (itemAmount / 9) + (itemAmount % 9 > 0 ? 1 : 0);
        int pages = (rows / 4) + (rows % 4 > 0 ? 1 : 0);

        BukkitMenu menu = new BukkitMenu(54, title);

        // fill with items
        int offset = page * 36;
        for ( int i = 0; i < Math.min(itemAmount - offset, 36); i++ ) {
            menu.setItem(i, supplier.apply(offset + i));
        }

        // fill hotbar
        for ( int slot : hotbar.keySet() ) {
            if ( slot >= 9 ) {
                continue;
            }

            menu.setItem(45 + slot, hotbar.get(slot));
        }

        if ( page > 0 ) {
            menu.setItem(47, new BukkitMenuItem(previousItem, (player, clickType) -> {
                player.openInventory(page(page - 1));
                return true;
            }));
        }

        if ( page < pages - 1 ) {
            menu.setItem(51, new BukkitMenuItem(nextItem, (player, clickType) -> {
                player.openInventory(page(page + 1));
                return true;
            }));
        }

        return menu;
    }

}