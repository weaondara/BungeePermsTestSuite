package net.alpenblock.bungeeperms.testsuite.bungee.tests;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.User;
import net.alpenblock.bungeeperms.platform.bungee.BungeeEventListener;
import net.alpenblock.bungeeperms.platform.bungee.BungeeSender;
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
            throw new RuntimeException("test " + BungeeTestSuite.getTestplayer() + " player not found");
        }

        User u = BungeePerms.getInstance().getPermissionsManager().getUser(BungeeTestSuite.getTestplayer());
        if (u == null)
        {
            throw new RuntimeException("test " + BungeeTestSuite.getTestplayer() + " player not found in db");
        }

        sender.sendMessage("Prefix: " + u.buildPrefix());
        sender.sendMessage("Suffix: " + u.buildSuffix());

        return result();
    }

    @Override
    public String getName()
    {
        return "DisplayableTest";
    }

}