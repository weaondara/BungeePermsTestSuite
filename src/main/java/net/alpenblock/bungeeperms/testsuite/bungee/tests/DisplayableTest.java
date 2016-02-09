package net.alpenblock.bungeeperms.testsuite.bungee.tests;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.User;
import net.alpenblock.bungeeperms.testsuite.bungee.BungeeTest;
import net.alpenblock.bungeeperms.testsuite.bungee.BungeeTestSuite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class DisplayableTest extends BungeeTest
{

    @Override
    public boolean test(CommandSender sender)
    {
        if (ProxyServer.getInstance().getPlayer(BungeeTestSuite.getTestplayer()) == null)
        {
            throw new RuntimeException("test player " + BungeeTestSuite.getTestplayer() + " not found");
        }

        User u = BungeePerms.getInstance().getPermissionsManager().getUser(BungeeTestSuite.getTestplayer());
        if (u == null)
        {
            throw new RuntimeException("test player " + BungeeTestSuite.getTestplayer() + " not found");
        }

        sender.sendMessage("Prefix: " + u.buildPrefix().replaceAll("§", "&"));
        sender.sendMessage("Suffix: " + u.buildSuffix().replaceAll("§", "&"));

        return result();
    }

    @Override
    public String getName()
    {
        return "DisplayableTest";
    }

}
