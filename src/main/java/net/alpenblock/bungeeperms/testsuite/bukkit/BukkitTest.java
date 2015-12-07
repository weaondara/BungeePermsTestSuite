package net.alpenblock.bungeeperms.testsuite.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class BukkitTest
{

    private boolean result;

    public abstract boolean test(CommandSender sender);

    public abstract String getName();

    public void assertTrue(CommandSender sender, boolean is)
    {
        result |= !is;
        if (!is)
        {
            sender.sendMessage(ChatColor.RED + "assert failed at " + Thread.currentThread().getStackTrace()[2]);
        }
    }

    public void assertFalse(CommandSender sender, boolean is)
    {
        result |= is;
        if (is)
        {
            sender.sendMessage(ChatColor.RED + "assert failed at " + Thread.currentThread().getStackTrace()[2]);
        }
    }

    public void assertEquals(CommandSender sender, Object expected, Object is)
    {
        result |= !expected.equals(is);
        if (!expected.equals(is))
        {
            sender.sendMessage(ChatColor.RED + "expected <" + expected + "> but is <" + is + ">");
            sender.sendMessage(ChatColor.RED + "assert failed at " + Thread.currentThread().getStackTrace()[2]);
        }
    }

    public boolean result()
    {
        return !result;
    }
}
