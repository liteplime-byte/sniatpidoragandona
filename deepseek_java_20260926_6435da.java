package com.example.sniatpidoragandona;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class SniatPidoragandona extends JavaPlugin {

    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        this.luckPerms = LuckPermsProvider.get();
        getLogger().info("SniatPidoragandona включён.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("§cЭта команда доступна только из консоли сервера.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cИспользование: /sniatpidoragandona <игрок>");
            return true;
        }

        String targetName = args[0];
        UserManager userManager = luckPerms.getUserManager();

        userManager.loadUser(targetName).thenAccept(user -> {
            if (user == null) {
                sender.sendMessage("§cИгрок §e" + targetName + " §cне найден в LuckPerms.");
                return;
            }
            UUID targetUuid = user.getUniqueId();

            userManager.deletePlayerData(targetUuid).thenRun(() -> {
                sender.sendMessage("§aДанные игрока §e" + targetName + " §aуспешно удалены из LuckPerms.");
            }).exceptionally(ex -> {
                sender.sendMessage("§cОшибка при удалении данных: " + ex.getMessage());
                return null;
            });
        }).exceptionally(ex -> {
            sender.sendMessage("§cИгрок §e" + targetName + " §cне найден в LuckPerms.");
            return null;
        });

        return true;
    }
}