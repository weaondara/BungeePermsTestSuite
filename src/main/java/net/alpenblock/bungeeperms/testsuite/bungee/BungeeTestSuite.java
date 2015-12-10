package net.alpenblock.bungeeperms.testsuite.bungee;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.alpenblock.bungeeperms.ChatColor;
import net.alpenblock.bungeeperms.Color;
import net.alpenblock.bungeeperms.testsuite.bungee.tests.PrefixSuffixTest;
import net.alpenblock.bungeeperms.testsuite.bungee.tests.ServerWorldTest;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeTestSuite extends Plugin
{
    @Getter
    private static BungeeTestSuite instance;
    
    @Getter
    private static String testplayer = "wea_ondara";
    
    private final List<BungeeTest> tests = new ArrayList();

    @Override
    public void onLoad()
    {
        instance = this;
        
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TestCommand("bungeepermstestsuite", "bungeepermstestsuite", "bpts"));
        tests.add(new PrefixSuffixTest());
        tests.add(new ServerWorldTest());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!cmd.getName().equalsIgnoreCase("bungeepermstestsuite"))
        {
            return false;
        }

        if (sender != ProxyServer.getInstance().getConsole())
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
            for (BungeeTest test : this.tests)
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
            BungeeTest test = getTest(testname);
            if (test == null)
            {
                continue;
            }
            boolean s = false;
            try
            {
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

    private BungeeTest getTest(String name)
    {
        for (BungeeTest t : tests)
        {
            if (t.getName().equalsIgnoreCase(name))
            {
                return t;
            }
        }
        return null;
    }

    private class TestCommand extends Command
    {

        public TestCommand(String name, String permission, String... aliases)
        {
            super(name, permission, aliases);
        }

        @Override
        public void execute(CommandSender sender, String[] args)
        {
            onCommand(sender, this, this.getName(), args);
        }

    }
}
