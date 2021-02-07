/*
 * This file is part of KingdomCraft.
 *
 * KingdomCraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KingdomCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with KingdomCraft. If not, see <https://www.gnu.org/licenses/>.
 */

package com.gufli.bookshelf.bukkit.menu;

import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.menu.AbstractMenu;
import com.gufli.bookshelf.menu.MenuClickType;
import com.gufli.bookshelf.server.Bookshelf;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ShelfPlayer player = Bookshelf.getPlayer(e.getPlayer().getUniqueId());
        handleClose(player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if ( !(e.getPlayer() instanceof Player) ) {
            return;
        }

        ShelfPlayer player = Bookshelf.getPlayer(e.getPlayer().getUniqueId());
        if ( player == null ) {
            return;
        }

        handleClose(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if ( !(e.getWhoClicked() instanceof Player) ) {
            return;
        }

        ShelfPlayer player = Bookshelf.getPlayer(e.getWhoClicked().getUniqueId());
        if ( player == null ) {
            return;
        }

        if ( !(player.getInventory() instanceof AbstractMenu) ) {
            return;
        }

        AbstractMenu<?, ?> inv = (AbstractMenu<?, ?>) player.getInventory();

        if ( inv == null || inv.getHandle() == null ) {
            return;
        }

        if ( e.getRawSlot() >= e.getView().getTopInventory().getSize() ) {
            return;
        }
//        if ( !inv.getHandle().equals(e.getView().getTopInventory()) ) {
//            return;
//        }

        e.setCancelled(true);

        MenuClickType type = null;
        switch (e.getClick()) {
            case DOUBLE_CLICK:
            case SHIFT_LEFT:
            case LEFT:
                type = MenuClickType.LEFT;
                break;
            case SHIFT_RIGHT:
            case RIGHT:
                type = MenuClickType.RIGHT;
                break;
            case MIDDLE:
                type = MenuClickType.MIDDLE;
        }

        if ( type == null ) {
            return;
        }

        boolean playSound = false;

        try {
            playSound = inv.dispatchClick(player, type, e.getRawSlot());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if ( !playSound ) {
            return;
        }

        // play a sound
        Sound sound;
        try {
            sound = Sound.valueOf("CLICK"); // 1.8
        } catch (IllegalArgumentException ex) {
            sound = Sound.valueOf("UI_BUTTON_CLICK"); // 1.9 +
        }

        Player p = (Player) e.getWhoClicked();
        p.playSound(p.getLocation().add(0, 1.8, 0), sound, 1f, 1f);
    }

    private void handleClose(ShelfPlayer player) {
        if ( !(player.getInventory() instanceof AbstractMenu) ) {
            return;
        }

        AbstractMenu<?, ?> inv = (AbstractMenu<?, ?>) player.getInventory();
        if ( inv != null ) {
            inv.dispatchClose(player);
            player.remove(ShelfPlayer.CUSTOM_GUI_KEY);
        }
    }

}