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

package me.mochibit.defcon.customassets.sounds.definitions

import me.mochibit.defcon.customassets.sounds.AbstractCustomSound
import me.mochibit.defcon.customassets.sounds.SoundData
import me.mochibit.defcon.customassets.sounds.SoundInfo

@SoundInfo("nuke", "set_near_outer_wind")
class NukeSetNearOuterWind(): AbstractCustomSound(
    SoundData(
        sounds = hashSetOf("nuke/set_near_outer_wind")
    )
){}