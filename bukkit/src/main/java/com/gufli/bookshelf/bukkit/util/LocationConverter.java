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

package com.gufli.bookshelf.bukkit.util;

import com.gufli.bookshelf.entity.PlatformLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationConverter {

    public static Location convert(PlatformLocation loc) {
        if ( loc == null ) {
            return null;
        }

        if ( Bukkit.getWorld(loc.getWorldName()) == null ) {
            return null;
        }

        return new Location(Bukkit.getWorld(loc.getWorldName()),
                loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public static PlatformLocation convert(Location loc) {
        if ( loc == null || loc.getWorld() == null ) {
            return null;
        }

        return new PlatformLocation(loc.getWorld().getName(),
                loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

}
