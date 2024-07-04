/*
 *
 * DEFCON: Nuclear warfare plugin for minecraft servers.
 * Copyright (c) 2024 mochibit.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.mochibit.defcon.particles

import org.bukkit.Material
import org.bukkit.entity.Display
import org.bukkit.inventory.ItemStack
import org.joml.Vector3f

class ExplosionDustParticle() : CustomParticle(
    DisplayParticleProperties(
        itemStack = ItemStack(Material.LEATHER_BOOTS),
        teleportDuration = 59,
        viewRange = 500.0f,
        scale = Vector3f(10.0f, 10.0f, 10.0f),
        modelData = 2
    )
)
{

}