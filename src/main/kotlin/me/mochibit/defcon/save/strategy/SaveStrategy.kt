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

package me.mochibit.defcon.save.strategy

import me.mochibit.defcon.save.savedata.SaveDataInfo
import me.mochibit.defcon.save.schemas.SaveSchema
import javassist.bytecode.SignatureAttribute.ClassType

sealed interface SaveStrategy<T : SaveSchema> {
    fun save(schema: T, page: Int? = null)
    fun load(page: Int? = null): T?
    fun pageExists(page: Int): Boolean
}