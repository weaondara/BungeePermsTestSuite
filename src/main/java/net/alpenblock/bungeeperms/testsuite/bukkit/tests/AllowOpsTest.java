package net.alpenblock.bungeeperms.testsuite.bukkit.tests;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.Statics;
import net.alpenblock.bungeeperms.platform.bukkit.BukkitConfig;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTestSuite;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllowOpsTest extends BukkitTest
{

    @Override
    public boolean test(CommandSender sender)
    {
        Player p = Bukkit.getPlayer(BukkitTestSuite.getTestplayer());
        if (p == null)
        {
            throw new RuntimeException("test player " + BukkitTestSuite.getTestplayer() + " not found");
        }

        boolean wasop = p.isOp();
        p.setOp(true);

        boolean wasallowops = ((BukkitConfig) BungeePerms.getInstance().getConfig()).isAllowops();

        Statics.setField(BukkitConfig.class, BungeePerms.getInstance().getConfig(), false, "allowops");
        assertFalse(sender, ((BukkitConfig) BungeePerms.getInstance().getConfig()).isAllowops());
        p.recalculatePermissions();
        assertFalse(sender, p.hasPermission("root.node3.perm5"));

        Statics.setField(BukkitConfig.class, BungeePerms.getInstance().getConfig(), true, "allowops");
        assertTrue(sender, ((BukkitConfig) BungeePerms.getInstance().getConfig()).isAllowops());
        p.recalculatePermissions();
        assertTrue(sender, p.hasPermission("root.node3.perm5"));

        //restore allowops
        Statics.setField(BukkitConfig.class, BungeePerms.getInstance().getConfig(), wasallowops, "allowops");

        //restore op
        p.setOp(wasop);

        return result();
    }

    @Override
    public String getName()
    {
        return "AllowOpsTest";
    }

}
