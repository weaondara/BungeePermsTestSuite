package net.alpenblock.bungeeperms.testsuite.bukkit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.alpenblock.bungeeperms.ChatColor;
import net.alpenblock.bungeeperms.Color;
import net.alpenblock.bungeeperms.testsuite.bukkit.tests.AllowOpsTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.tests.DisplayableTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.tests.SuperPermsIterate;
import net.alpenblock.bungeeperms.testsuite.bukkit.tests.SuperPermsList;
import net.alpenblock.bungeeperms.testsuite.bukkit.tests.SuperPermsTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.tests.PrefixSuffixTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.tests.RecalculatePermissionsTest;
import net.alpenblock.bungeeperms.testsuite.bukkit.tests.VaultDisplayableTest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitTestSuite extends JavaPlugin
{

    @Getter
    private static BukkitTestSuite instance;

    @Getter
    private static String testplayer = "wea_ondara";

    private final List<BukkitTest> tests = new ArrayList();

    @Override
    public void onLoad()
    {
        instance = this;

        tests.add(new AllowOpsTest());
        tests.add(new DisplayableTest());
        tests.add(new PrefixSuffixTest());
        tests.add(new RecalculatePermissionsTest());
        tests.add(new SuperPermsIterate());
        tests.add(new SuperPermsTest());
        tests.add(new SuperPermsList());
        tests.add(new VaultDisplayableTest());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!cmd.getName().equalsIgnoreCase("bungeepermstestsuite"))
        {
            return false;
        }

        if (!(sender instanceof ConsoleCommandSender))
        {
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("help"))
        {
            sender.sendMessage("/bpts help - Shows this help");
            sender.sendMessage("/bpts list - Lists the test cases");
            sender.sendMessage("/bpts testall - Tests all test cases");
            sender.sendMessage("/bpts player <player> - Sets the player for the test cases");
            sender.sendMessage("/bpts test <testcase> - Executes a single testcase");
            return true;
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("testall"))
        {
            String[] tests = new String[this.tests.size()];
            for (int i = 0; i < this.tests.size(); i++)
            {
                tests[i] = this.tests.get(i).getName();
            }
            test(sender, tests);
            return true;
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("list"))
        {
            for (BukkitTest test : this.tests)
            {
                sender.sendMessage("- " + test.getName());
            }
            return true;
        }
        else if (args.length > 1 && args[0].equalsIgnoreCase("player"))
        {
            testplayer = args[1];
            return true;
        }
        else if (args.length > 1 && args[0].equalsIgnoreCase("test"))
        {
            String[] tests = new String[args.length - 1];
            System.arraycopy(args, 1, tests, 0, args.length - 1);
            test(sender, tests);
            return true;
        }

        sender.sendMessage(Color.Error + "Command not found.");
        return false;
    }

    private void test(CommandSender sender, String... tests)
    {
        int error = 0;
        int fail = 0;
        int all = 0;
        long time = 0;
        for (String testname : tests)
        {
            BukkitTest test = getTest(testname);
            if (test == null)
            {
                continue;
            }
            boolean s = false;
            try
            {
                sender.sendMessage(ChatColor.GRAY + "Running test " + test.getName() + " ...");
                long start = System.currentTimeMillis();
                s = test.test(sender);
                time += System.currentTimeMillis() - start;
            }
            catch (Throwable t)
            {
                StringWriter writer = new StringWriter();
                t.printStackTrace(new PrintWriter(writer));
                sender.sendMessage(ChatColor.RED + writer.toString());
                error++;
                fail--;
            }
            fail += !s ? 1 : 0;
            all++;
        }
        sender.sendMessage("Tests run: " + all + ", Failures: " + fail + ", Errors: " + error + ", Skipped: 0, Time elapsed: " + (double) time / 1000 + " sec");
    }

    private BukkitTest getTest(String name)
    {
        for (BukkitTest t : tests)
        {
            if (t.getName().equalsIgnoreCase(name))
            {
                return t;
            }
        }
        return null;
    }
}
