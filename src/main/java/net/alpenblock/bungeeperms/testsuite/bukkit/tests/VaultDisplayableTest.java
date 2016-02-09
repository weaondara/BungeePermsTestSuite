package net.alpenblock.bungeeperms.testsuite.bukkit.tests;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.ChatColor;
import net.alpenblock.bungeeperms.User;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTestSuite;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultDisplayableTest extends BukkitTest
{

    @Override
    public boolean test(CommandSender sender)
    {
        if (Bukkit.getPlayer(BukkitTestSuite.getTestplayer()) == null)
        {
            throw new RuntimeException("test player " + BukkitTestSuite.getTestplayer() + " not found");
        }

        User u = BungeePerms.getInstance().getPermissionsManager().getUser(BukkitTestSuite.getTestplayer());
        if (u == null)
        {
            throw new RuntimeException("test player " + BukkitTestSuite.getTestplayer() + " not found");
        }

        if (Bukkit.getPluginManager().getPlugin("Vault") == null)
        {
            throw new RuntimeException("vault not present");
        }
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        if (rsp == null)
        {
            throw new RuntimeException("vault: no chat service provider found");
        }
        Chat chat = rsp.getProvider();
        if (!chat.getName().equals("BungeePerms"))
        {
            throw new RuntimeException("vault: chat service provider is not bungeeperms");
        }

        sender.sendMessage("BP    Prefix: " + u.buildPrefix().replaceAll("" + ChatColor.COLOR_CHAR, "&"));
        sender.sendMessage("Vault Prefix: " + chat.getPlayerPrefix(Bukkit.getPlayer(BukkitTestSuite.getTestplayer())).replaceAll("" + ChatColor.COLOR_CHAR, "&"));
        sender.sendMessage("BP    Suffix: " + u.buildSuffix().replaceAll("" + ChatColor.COLOR_CHAR, "&"));
        sender.sendMessage("Vault Suffix: " + chat.getPlayerSuffix(Bukkit.getPlayer(BukkitTestSuite.getTestplayer())).replaceAll("" + ChatColor.COLOR_CHAR, "&"));

        return result();
    }

    @Override
    public String getName()
    {
        return "VaultDisplayableTest";
    }

}
