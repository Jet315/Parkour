package me.jet315.timedpmparkour.commands;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.commands.admincommands.*;
import me.jet315.timedpmparkour.utils.Maths;
import me.jet315.timedpmparkour.utils.Messages;
import me.jet315.parkouradditional.API;
import me.jet315.parkouradditional.storage.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Jet on 16/12/2017.
 */
public class CommandHandler implements org.bukkit.command.CommandExecutor {

    private ArrayList<String> commandDelay = new ArrayList<>();

    private Map<String, CommandExecutor> commands = new HashMap<String, CommandExecutor>();

    public Map<String, CommandExecutor> getCommands() {
        return commands;
    }

    public CommandHandler() {
        commands.put("createarena", new CreateArenaCommand());
        commands.put("delete", new DeleteArenaCommand());
        commands.put("forceload", new ForceLoadArenaCommand());
        commands.put("setdifficulty", new SetDifficultyCommand());
        commands.put("setend", new SetEndCommand());
        commands.put("setpermission", new SetPermissionCommand());
        commands.put("setspawn", new SetSpawnCommand());
        commands.put("setstart", new SetStartCommand());
        commands.put("setholo", new SetHoloCommand());
        commands.put("deletemysql", new DeleteSQLDataCommand());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("pk")) {
            if (args.length == 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("&7&m--------------------\n");
                for (CommandExecutor command : commands.values()) {

                    if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
                        stringBuilder.append("");
                    } else {
                        stringBuilder.append("&e- &7").append(command.getCommand()).append("\n");
                    }

                }
                stringBuilder.append("&7&m--------------------");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', stringBuilder.toString()));

                return true;
            }


            if (args[0] != null) {
                String name = args[0].toLowerCase();
                if (commands.containsKey(name)) {
                    final CommandExecutor command = commands.get(name);

                    if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                        return true;
                    }

                    if (!command.isBoth()) {
                        if (command.isConsole() && sender instanceof Player) {
                            sender.sendMessage(ChatColor.RED + "Only console can use that command!");
                            return true;
                        }
                        if (command.isPlayer() && sender instanceof ConsoleCommandSender) {
                            sender.sendMessage(ChatColor.RED + "Only players can use that command!");
                            return true;
                        }
                    }

                    if (command.getLength() > args.length) {
                        sender.sendMessage(ChatColor.RED + "Usage: " + command.getUsage());
                        return true;
                    }

                    command.execute(sender, args);
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("stats")) {
            if (!(sender instanceof Player)) return false;
            Player p = (Player) sender;
            if (commandDelay.contains(p.getName())){
                p.sendMessage(Messages.prefix + ChatColor.RED + " Please wait before checking stats again!");
                return false;
            }

            refreshPlayer(p);

            if (args.length == 0) {
                ArrayList<String> chat = getChatStats(p, null);
                for (String line : chat) {
                    p.sendMessage(line);
                }
                return false;
            } else if (args.length > 0) {
                if (sender.hasPermission("stats.other")) {
                    if (args.length == 1) {


                        //Task - get stats for the player ( called once the other future returns)
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        Callable<ArrayList<String>> task = new Callable<ArrayList<String>>() {
                            public ArrayList<String> call() throws Exception {
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                                ArrayList<String> chatStats = getChatStats(p,offlinePlayer);

                                return chatStats;
                            }
                        };

                        Future<ArrayList<String>> messages = executorService.submit(task);

                        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                int counter = 1;
                                while (!messages.isDone()){
                                    counter++;
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //10 seconds no response
                                    if(counter==100){
                                        //When accessing Bukkit, needs to be within Synchronized
                                        Bukkit.getScheduler().runTask(Core.getInstance(),new Runnable(){
                                            @Override
                                            public void run() {
                                                p.sendMessage(ChatColor.RED + "Error occurred while processing player, please try again later" );
                                            }
                                        });
                                        return;
                                    }

                                }
                                //while has completed, future is done

                                //When accessing Bukkit, needs to be within Synchronized
                                Bukkit.getScheduler().runTask(Core.getInstance(),new Runnable(){
                                    @Override
                                    public void run() {
                                        try {
                                            ArrayList<String> message = messages.get();
                                            for (String line : message) {
                                                p.sendMessage(line);
                                            }
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                            }
                        });


                        //ArrayList<String> chat = getChatStats(p, offlinePlayer);
                        //System.out.println(String.valueOf(System.currentTimeMillis() - start) + "    - chat stats");
/*                        for (String line : chat) {
                            p.sendMessage(line);
                        }*/
                        return false;
                    } else {
                        //Send usage stats
                        p.sendMessage(Messages.prefix + ChatColor.RED + " Correct Usage: " + ChatColor.GREEN + "/Stats " + ChatColor.AQUA + "or " + ChatColor.GREEN + "/Stats <Player>");
                        return false;
                    }
                } else {
                    p.sendMessage(Messages.prefix + ChatColor.RED + " A rank is required to check another users stats. " + ChatColor.GREEN + "Shop.TimeMC.org " + ChatColor.GRAY + "*Click*");
                }
            }

        }
        return false;
    }

    private ArrayList<String> getChatStats(final Player p, final OfflinePlayer offlinePlayer) {

        ArrayList<String> text = new ArrayList<>();
        PlayerData playersData;
        //ArrayList<PlayerMapData> mapData;

        //Stores the player who is being checked
        OfflinePlayer playerToBeChecked;
        text.add(" ");
        text.add(" ");
        //-Title of the message
        if (offlinePlayer == null) {
            //Load stats for self
            playersData = API.getPlayersStats(p);
            playerToBeChecked = p;
            //mapData = Core.getInstance().getPlayerData().getPlayerMaps().get(p);
            text.add(ChatColor.translateAlternateColorCodes('&', "&7&m-----&9&m[&7&m-------------&r   &ATime&bMC &bStats&r   &7&m-------------&9&m]&7&m-----"));
        } else {
            //Load stats of otherplayer
            playersData = API.getPlayersStats(offlinePlayer);
            playerToBeChecked = offlinePlayer;
            //mapData = Core.getInstance().getMySQLManager().getPlayersData(offlinePlayer.getUniqueId());
            text.add(ChatColor.translateAlternateColorCodes('&', "&7&m----&9&m[&7&m-------------&r   &ATime&bMC &cStats for &a" + offlinePlayer.getName() + "&r   &7&m------------&9&m]&7&m----"));
        }
        if (playersData.getJumps() <= 20 && !playerToBeChecked.getName().equalsIgnoreCase(p.getName())) {
            text.add(ChatColor.RED + "This player has never played before / not played long enough to generate stats from");
            return text;
        }

        //-Jumps, Checkpoints and playtime
        String[] timePlayed = Maths.splitToComponentTimes(playersData.getTime());
        if(timePlayed[0].equalsIgnoreCase("00")){
            text.add(ChatColor.GREEN + "Jumps: " + ChatColor.AQUA + playersData.getJumps() + ChatColor.GREEN + "   Checkpoints: " + ChatColor.AQUA + playersData.getCheckpoints() + ChatColor.GREEN + "    PlayTime: " + ChatColor.AQUA + timePlayed[1] + " Hours and " + timePlayed[2]+ " Minutes");
        }else{
            text.add(ChatColor.GREEN + "Jumps: " + ChatColor.AQUA + playersData.getJumps() + ChatColor.GREEN + "   Checkpoints: " + ChatColor.AQUA + playersData.getCheckpoints() + ChatColor.GREEN + "    PlayTime: " + ChatColor.AQUA + timePlayed[0] +" Days, "+ timePlayed[1] + " Hours and " + timePlayed[2]+ " Minutes");
        }

        //-Last Seen
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(playersData.getLastSeen());
        //If player is online
        String[] timeComponents = Maths.splitToComponentTimes(System.currentTimeMillis() - calender.getTimeInMillis());

        if(playerToBeChecked.isOnline()){

            text.add(ChatColor.GOLD + "Player is " + ChatColor.GREEN +"ONLINE " +ChatColor.GOLD+"Since: " + ChatColor.WHITE + (timeComponents[1].equalsIgnoreCase("00") ? "":ChatColor.WHITE + timeComponents[1] + " Hours, ") + timeComponents[2] + " Minutes, "+ timeComponents[3] + " Seconds " + ChatColor.GOLD +"ago");
        }else{

            text.add(ChatColor.GOLD + "Player is " + ChatColor.RED +"OFFLINE " +ChatColor.GOLD+"Last Seen: " + ChatColor.GRAY+ (timeComponents[0].equalsIgnoreCase("00") ? "":ChatColor.WHITE + timeComponents[0] + " Days, ") + (timeComponents[1].equalsIgnoreCase("00") ? "":ChatColor.WHITE + timeComponents[1] + " Hours, ") + timeComponents[2] + " Minutes, "+ timeComponents[3] + " Seconds " + ChatColor.GOLD +"ago");

        }



        text.add(ChatColor.RED + "                       Map Stats " + ChatColor.GREEN + " COMING SOON!");
        text.add(" ");
        return text;


    }
    public void refreshPlayer(Player p) {
        commandDelay.add(p.getName());
        new BukkitRunnable() {
            int i = 1;

            public void run() {
                if (i == 0) {
                    commandDelay.remove(p.getName());
                    cancel();
                }
                i--;
            }

        }.runTaskTimer(Core.getInstance(), 0L, 200L);
    }
}