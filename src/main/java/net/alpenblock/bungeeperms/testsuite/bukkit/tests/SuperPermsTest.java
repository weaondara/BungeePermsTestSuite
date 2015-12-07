package net.alpenblock.bungeeperms.testsuite.bukkit.tests;

import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTestSuite;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuperPermsTest extends BukkitTest
{

    @Override
    public boolean test(CommandSender sender)
    {
        Player p = Bukkit.getPlayer(BukkitTestSuite.getTestplayer());
        if (p == null)
        {
            throw new RuntimeException(BukkitTestSuite.getTestplayer() + " is not online!");
        }

        boolean wasop = p.isOp();
        p.setOp(false);

        //normal tests
        assertFalse(sender, p.hasPermission("root.*"));
        assertTrue(sender, p.hasPermission("root.node1.perm1"));
        assertFalse(sender, p.hasPermission("root.node2.perm3"));
        assertFalse(sender, p.hasPermission("root.node3.perm5"));

        //op tests
        p.setOp(true);
        assertTrue(sender, p.hasPermission("root.*"));
        assertTrue(sender, p.hasPermission("root.node1.perm1"));
        assertTrue(sender, p.hasPermission("root.node2.perm3"));
        assertTrue(sender, p.hasPermission("root.node3.perm5"));

        //restore op
        p.setOp(wasop);

        return result();
    }

    @Override
    public String getName()
    {
        return "SuperPermsTest";
    }

}
