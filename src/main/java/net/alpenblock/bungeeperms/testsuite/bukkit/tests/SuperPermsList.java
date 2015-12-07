package net.alpenblock.bungeeperms.testsuite.bukkit.tests;

import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTestSuite;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class SuperPermsList extends BukkitTest
{

    @Override
    public boolean test(CommandSender sender)
    {
        Player p = Bukkit.getPlayer(BukkitTestSuite.getTestplayer());
        if (p == null)
        {
            throw new RuntimeException(BukkitTestSuite.getTestplayer() + " is not online!");
        }

        for (PermissionAttachmentInfo perms : p.getEffectivePermissions())
        {
            System.out.println(perms.getPermission());
        }

        return result();
    }

    @Override
    public String getName()
    {
        return "SuperPermsList";
    }

}
