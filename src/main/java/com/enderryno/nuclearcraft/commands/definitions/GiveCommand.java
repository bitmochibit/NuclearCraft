package com.enderryno.nuclearcraft.commands.definitions;

import com.enderryno.nuclearcraft.commands.GenericCommand;
import com.enderryno.nuclearcraft.commands.CommandInfo;
import com.enderryno.nuclearcraft.custom_items.interfaces.GenericItem;
import com.enderryno.nuclearcraft.custom_items.register.ItemRegister;
import com.enderryno.nuclearcraft.custom_items.register.exceptions.ItemNotRegisteredException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandInfo(name="ncgive", permission = "nuclearcraft.admin", requiresPlayer = true)
public class GiveCommand extends GenericCommand {
    @Override
    public void execute(Player player, String[] args) {
        int itemId;
        GenericItem item;

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "The second parameter must be a NuclearCraft ID");
            return;
        }
        try {
            itemId = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            player.sendMessage(ChatColor.RED + "The second parameter must be a NuclearCraft ID");
            return;
        }

        try {
            item = ItemRegister.getRegisteredItems().get(itemId);
        } catch (ItemNotRegisteredException e) {
            e.printStackTrace();
            return;
        }
        if (item == null) {
            player.sendMessage(ChatColor.RED + "This item is invalid");
            return;
        }
        try {
            player.getInventory().addItem(item.getItemStack());
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Something went wrong, look for the console");
            e.printStackTrace();
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Gave the player a " + item.getDisplayName());

    }
}
