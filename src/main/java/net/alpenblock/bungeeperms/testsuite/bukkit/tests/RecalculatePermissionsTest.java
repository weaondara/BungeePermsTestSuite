package net.alpenblock.bungeeperms.testsuite.bukkit.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.Group;
import net.alpenblock.bungeeperms.Server;
import net.alpenblock.bungeeperms.Statics;
import net.alpenblock.bungeeperms.User;
import net.alpenblock.bungeeperms.platform.bukkit.BukkitPlugin;
import net.alpenblock.bungeeperms.platform.bukkit.Injector;
import net.alpenblock.bungeeperms.platform.bukkit.Permissible;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.BukkitTestSuite;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;

public class RecalculatePermissionsTest extends BukkitTest
{

    @Override
    public boolean test(CommandSender sender)
    {
        if (Bukkit.getPlayer(BukkitTestSuite.getTestplayer()) == null)
        {
            throw new RuntimeException("test player " + BukkitTestSuite.getTestplayer() + " not found");
        }

        //create user perm list
        List<String> permlist = new ArrayList();
        permlist.add("testperm1.*");
        permlist.add("-testperm2.bla");
        permlist.add("-testperm3.*");

        //create tmp bungeeperms user
        User olduser = BungeePerms.getInstance().getPermissionsManager().getUser(BukkitTestSuite.getTestplayer());
        User newuser = new User(olduser.getName(), olduser.getUUID(), new ArrayList<Group>(), permlist, new HashMap<String, Server>(), null, null, null);

        //inject user in bp
        BungeePerms.getInstance().getPermissionsManager().removeUserFromCache(olduser);
        List<User> users = Statics.getField(BungeePerms.getInstance().getPermissionsManager(), List.class, "users");
        users.add(newuser);

        //inject old-fake-permissible into player's permissible
        Player p = Bukkit.getPlayer(BukkitTestSuite.getTestplayer());
        Permissible perm = (Permissible) Injector.getPermissible(p);
        perm.setOldPermissible(new FakeOldPermissible(p, perm.getOldPermissible()));
        p.recalculatePermissions();

        PermissionAttachment attach = new PermissionAttachment(BukkitPlugin.getInstance(), perm.getOldPermissible());
        Map<String, PermissionAttachmentInfo> perms = new LinkedHashMap();
        perms.put("testperm1", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm1", attach, false));
        perms.put("testperm1.true", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm1.true", attach, true));
        perms.put("testperm1.false", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm1.false", attach, true));
        perms.put("testperm2.*", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm2.*", attach, true));
        perms.put("testperm2.bla", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm2.bla", attach, false));
        perms.put("testperm2.foo", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm2.foo", attach, false));
        perms.put("testperm2.bar", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm2.bar", attach, false));
        perms.put("testperm3.*", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm3.*", attach, false));
        perms.put("testperm3.foo3", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm3.foo3", attach, false));
        perms.put("testperm3.bar3", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm3.bar3", attach, false));
        perms.put("testperm4.testperm1.foo", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm4.testperm1.foo", attach, false));
        perms.put("testperm1.*", new PermissionAttachmentInfo(perm.getOldPermissible(), "testperm1.*", attach, true));

        assertEquals(sender, perms.size(), perm.getEffectivePermissions().size());

        int i = 0;
        for (PermissionAttachmentInfo pia : perms.values())
        {
            System.out.println(pia.getPermission());
            PermissionAttachmentInfo pia2 = new ArrayList<>(perm.getEffectivePermissions()).get(i++);
            assertEquals(sender, pia.getPermission(), pia2.getPermission());
            assertEquals(sender, pia.getValue(), pia2.getValue());
        }

        //restore
        BungeePerms.getInstance().getPermissionsManager().removeUserFromCache(newuser);
        perm.setOldPermissible(((FakeOldPermissible)perm.getOldPermissible()).getOldperm());

        return result();
    }

    @Override
    public String getName()
    {
        return "RecalculatePermissionsTest";
    }

    private class FakeOldPermissible extends PermissibleBase
    {
        @Getter
        org.bukkit.permissions.Permissible oldperm;

        public FakeOldPermissible(ServerOperator opable, org.bukkit.permissions.Permissible oldperm)
        {
            super(opable);
            this.oldperm = oldperm;
        }

        @Override
        public void recalculatePermissions()
        {
            clearPermissions();
            Map<String, PermissionAttachmentInfo> perms = Statics.getField(PermissibleBase.class, this, Map.class, "permissions");

            //create some superperms
            PermissionAttachment attach = new PermissionAttachment(BukkitPlugin.getInstance(), this);

            //add some superperms to user
            perms.put("testperm1", new PermissionAttachmentInfo(this, "testperm1", attach, false));
            perms.put("testperm1.true", new PermissionAttachmentInfo(this, "testperm1.true", attach, true));
            perms.put("testperm1.false", new PermissionAttachmentInfo(this, "testperm1.false", attach, false));
            perms.put("testperm2.*", new PermissionAttachmentInfo(this, "testperm2.*", attach, true));
            perms.put("testperm2.bla", new PermissionAttachmentInfo(this, "testperm2.bla", attach, true));
            perms.put("testperm2.foo", new PermissionAttachmentInfo(this, "testperm2.foo", attach, false));
            perms.put("testperm2.bar", new PermissionAttachmentInfo(this, "testperm2.bar", attach, false));
            perms.put("testperm3.*", new PermissionAttachmentInfo(this, "testperm3.*", attach, true));
            perms.put("testperm3.foo3", new PermissionAttachmentInfo(this, "testperm3.foo3", attach, true));
            perms.put("testperm3.bar3", new PermissionAttachmentInfo(this, "testperm3.bar3", attach, false));
            perms.put("testperm4.testperm1.foo", new PermissionAttachmentInfo(this, "testperm4.testperm1.foo", attach, false));
        }
    }
}
