package net.alpenblock.bungeeperms.testsuite.bukkit.tests;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.User;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTestSuite;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class SuperPermsIterate extends BukkitTest
{

    @Override
    public boolean test(CommandSender sender)
    {
        Player p = Bukkit.getPlayer(BukkitTestSuite.getTestplayer());
        if (p == null)
        {
            throw new RuntimeException("test player " + BukkitTestSuite.getTestplayer() + " not found");
        }

        User user = BungeePerms.getInstance().getPermissionsManager().getUser(BukkitTestSuite.getTestplayer());
        if (user == null)
        {
            throw new RuntimeException("test player " + BukkitTestSuite.getTestplayer() + " not found");
        }
        for (String perm : user.getEffectivePerms())
        {
            if (perm.startsWith("root.iterate."))
            {
                BungeePerms.getInstance().getPermissionsManager().removeUserPerm(user, perm);
            }
        }
        BungeePerms.getInstance().getPermissionsManager().addUserPerm(user, "root.iterate.3");
        BungeePerms.getInstance().getPermissionsManager().addUserPerm(user, "root.iterate.5");
        BungeePerms.getInstance().getPermissionsManager().addUserPerm(user, "root.iterate.7");

        boolean found3 = false;
        boolean found5 = false;
        boolean found7 = false;
        for (PermissionAttachmentInfo perms : p.getEffectivePermissions())
        {
            System.out.println(perms.getPermission());
            if (perms.getPermission().startsWith("root.iterate."))
            {
                switch (Integer.valueOf(perms.getPermission().split("root.iterate.")[1]))
                {
                    case 3:
                        found3 = true;
                        break;
                    case 5:
                        found5 = true;
                        break;
                    case 7:
                        found7 = true;
                        break;
                    default:
                        throw new RuntimeException("found weird perm " + perms.getPermission());
                }
            }
        }

        assertTrue(sender, found3);
        assertTrue(sender, found5);
        assertTrue(sender, found7);

        return result();
    }

    @Override
    public String getName()
    {
        return "SuperPermsIterate";
    }

}
