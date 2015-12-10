package net.alpenblock.bungeeperms.testsuite.bungee.tests;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.platform.bungee.BungeeEventListener;
import net.alpenblock.bungeeperms.platform.bungee.BungeeSender;
import net.alpenblock.bungeeperms.testsuite.bungee.BungeeTest;
import net.alpenblock.bungeeperms.testsuite.bungee.BungeeTestSuite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class ServerWorldTest extends BungeeTest
{
    
    @Override
    public boolean test(CommandSender sender)
    {
        if (ProxyServer.getInstance().getPlayer(BungeeTestSuite.getTestplayer()) == null)
        {
            throw new RuntimeException("test " + BungeeTestSuite.getTestplayer() + " player not found");
        }
        
        if (ProxyServer.getInstance().getPlayer(BungeeTestSuite.getTestplayer()) != null)
        {
            BungeeSender bs = new BungeeSender(ProxyServer.getInstance().getPlayer(BungeeTestSuite.getTestplayer()));
            sender.sendMessage(BungeeTestSuite.getTestplayer() + " is on " + bs.getServer() + " in " + bs.getWorld());
        }
        else
        {
            BungeeEventListener el = (BungeeEventListener) BungeePerms.getInstance().getEventListener();
            String world = el.getPlayerWorlds().get(BungeeTestSuite.getTestplayer());
            sender.sendMessage(BungeeTestSuite.getTestplayer() + " is not online and in " + world);
        }
        
        return result();
    }
    
    @Override
    public String getName()
    {
        return "ServerWorldTest";
    }
    
}
