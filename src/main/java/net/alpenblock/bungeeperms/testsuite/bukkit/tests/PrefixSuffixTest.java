package net.alpenblock.bungeeperms.testsuite.bukkit.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.ChatColor;
import net.alpenblock.bungeeperms.Group;
import net.alpenblock.bungeeperms.Server;
import net.alpenblock.bungeeperms.User;
import net.alpenblock.bungeeperms.World;
import net.alpenblock.bungeeperms.platform.Sender;
import net.alpenblock.bungeeperms.platform.bukkit.BukkitSender;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTestSuite;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class PrefixSuffixTest extends BukkitTest
{

    @Override
    public boolean test(CommandSender sender)
    {
        if (Bukkit.getPlayer(BukkitTestSuite.getTestplayer()) == null)
        {
            throw new RuntimeException("test player " + BukkitTestSuite.getTestplayer() + " not found");
        }
        
        Map<String, Server> uservers = new HashMap();
        Map<String, Server> gservers = new HashMap();
        Map<String, World> uworlds = new HashMap();
        Map<String, World> gworlds = new HashMap();

        uservers.put("main", new Server("main", new ArrayList<String>(), uworlds, null, null, null));
        gservers.put("main", new Server("main", new ArrayList<String>(), gworlds, null, null, null));

        uworlds.put("Alm", new World("Alm", new ArrayList<String>(), null, null, null));
        gworlds.put("Alm", new World("Alm", new ArrayList<String>(), null, null, null));

        Group g = new Group("test", new ArrayList<String>(), new ArrayList<String>(), gservers, 0, 0, "", false, null, null, null);
        User u = new User(BukkitTestSuite.getTestplayer(), null, new ArrayList<Group>(), new ArrayList<String>(), uservers, null, null, null);
        u.getGroups().add(g);
        Sender s = new BukkitSender(Bukkit.getPlayer(BukkitTestSuite.getTestplayer()));

        assertEquals(sender, "", g.buildPrefix("main", "Alm"));
        assertEquals(sender, "", g.buildSuffix("main", "Alm"));
        assertEquals(sender, (BungeePerms.getInstance().getConfig().isTerminatePrefixSpace() ? " " : "")
                     + (BungeePerms.getInstance().getConfig().isTerminatePrefixReset() ? ChatColor.RESET : ""),
                     u.buildPrefix(s));
        assertEquals(sender, (BungeePerms.getInstance().getConfig().isTerminateSuffixSpace() ? " " : "")
                     + (BungeePerms.getInstance().getConfig().isTerminateSuffixReset() ? ChatColor.RESET : ""),
                     u.buildSuffix(s));

        //2nd test
        g.setPrefix("[ggp]");
        g.setSuffix("[ggs]");
        u.setPrefix("[ugp]");
        u.setSuffix("[ugs]");

        gservers.get("main").setPrefix("[gsp]");
        gservers.get("main").setSuffix("[gss]");
        uservers.get("main").setPrefix("[usp]");
        uservers.get("main").setSuffix("[uss]");

        gservers.get("main").getWorld("Alm").setPrefix("[gwp]");
        gservers.get("main").getWorld("Alm").setSuffix("[gws]");
        uservers.get("main").getWorld("Alm").setPrefix("[uwp]");
        uservers.get("main").getWorld("Alm").setSuffix("[uws]");

        assertEquals(sender, "[ggp] [gsp] [gwp]" + ChatColor.RESET, g.buildPrefix("main", "Alm"));
        assertEquals(sender, "[ggs] [gss] [gws]" + ChatColor.RESET, g.buildSuffix("main", "Alm"));
        assertEquals(sender, "[ggp] [gsp] [gwp] [ugp] [usp] [uwp]"
                     + (BungeePerms.getInstance().getConfig().isTerminatePrefixSpace() ? " " : "")
                     + (BungeePerms.getInstance().getConfig().isTerminatePrefixReset() ? ChatColor.RESET : ""),
                     u.buildPrefix(s));
        assertEquals(sender, "[ggs] [gss] [gws] [ugs] [uss] [uws]"
                     + (BungeePerms.getInstance().getConfig().isTerminateSuffixSpace() ? " " : "")
                     + (BungeePerms.getInstance().getConfig().isTerminateSuffixReset() ? ChatColor.RESET : ""),
                     u.buildSuffix(s));

        return result();
    }

    @Override
    public String getName()
    {
        return "PrefixSuffixTest";
    }

}
