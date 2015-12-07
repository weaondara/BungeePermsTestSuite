package net.alpenblock.bungeeperms.testsuite.bungee;

import net.alpenblock.bungeeperms.ChatColor;
import net.md_5.bungee.api.CommandSender;

public abstract class BungeeTest
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
