package net.alpenblock.bungeeperms.testsuite.bukkit.tests;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.ChatColor;
import net.alpenblock.bungeeperms.User;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTestSuite;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class DisplayableTest extends BukkitTest
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

        sender.sendMessage("Prefix: " + u.buildPrefix().replaceAll("" + ChatColor.COLOR_CHAR, "&"));
        sender.sendMessage("Suffix: " + u.buildSuffix().replaceAll("" + ChatColor.COLOR_CHAR, "&"));

        return result();
    }

    @Override
    public String getName()
    {
        return "DisplayableTest";
    }

}
