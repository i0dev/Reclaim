package com.i0dev.Reclaim.commands;

import com.i0dev.Reclaim.Heart;
import com.i0dev.Reclaim.config.GeneralConfig;
import com.i0dev.Reclaim.config.MessageConfig;
import com.i0dev.Reclaim.objects.ReclaimOption;
import com.i0dev.Reclaim.templates.AbstractCommand;
import com.i0dev.Reclaim.utility.ConfigUtil;
import com.i0dev.Reclaim.utility.MsgUtil;
import com.i0dev.Reclaim.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdReclaim extends AbstractCommand {

    MessageConfig msg;
    GeneralConfig config;

    public CmdReclaim(Heart heart, String command) {
        super(heart, command);
    }

    @Override
    public void initialize() {
        msg = getHeart().getConfig(MessageConfig.class);
        config = getHeart().getConfig(GeneralConfig.class);
    }

    @Override
    public void deinitialize() {
        msg = null;
        config = null;
    }

    public List<ReclaimOption> getReclaimOptions(Player player) {

        List<ReclaimOption> ret = new ArrayList<>();
        for (ReclaimOption option : heart.cnf().getReclaimOptions()) {
            if (player.hasPermission(option.getPermission())) ret.add(option);
        }
        return ret;
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            if (!sender.hasPermission("reclaim.base.cmd")) {
                MsgUtil.msg(sender, msg.getNoPermission());
                return;
            }
            if (sender instanceof ConsoleCommandSender) {
                MsgUtil.msg(sender, msg.getCantRunAsConsole());
                return;
            }

            if (heart.storage().getClaimed().contains(((Player) sender).getUniqueId().toString())) {
                MsgUtil.msg(sender, msg.getAlreadyClaimed());
                return;
            }

            for (ReclaimOption reclaimOption : getReclaimOptions((Player) sender)) {
                for (String command : reclaimOption.getCommands()) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", sender.getName()));
                }
                MsgUtil.msg(sender, msg.getClaimSuccess(), new MsgUtil.Pair<>("{displayName}", reclaimOption.getDisplayName()));
            }
            heart.storage().getClaimed().add(((Player) sender).getUniqueId().toString());
            ConfigUtil.save(heart.storage(), heart.storage().getPath());
            return;
        }


        if (args[0].equalsIgnoreCase("help")) {
            MsgUtil.msg(sender, msg.getHelpPageHeader());
            MsgUtil.msg(sender, msg.getHelpUsage());
            MsgUtil.msg(sender, msg.getReloadUsage());
            MsgUtil.msg(sender, msg.getResetUsage());
            MsgUtil.msg(sender, msg.getRemoveUsage());
            MsgUtil.msg(sender, msg.getBaseUsage());
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("reclaim.reload.cmd")) {
                MsgUtil.msg(sender, msg.getNoPermission());
                return;
            }
            getHeart().reload();
            MsgUtil.msg(sender, msg.getReloadedConfig());
            return;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            if (!sender.hasPermission("reclaim.reset.cmd")) {
                MsgUtil.msg(sender, msg.getNoPermission());
                return;
            }

            heart.storage().getClaimed().clear();
            ConfigUtil.save(heart.storage(), heart.storage().getPath());
            MsgUtil.msg(sender, msg.getReloadedConfig());
            return;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (!sender.hasPermission("reclaim.remove.cmd")) {
                MsgUtil.msg(sender, msg.getNoPermission());
                return;
            }
            if (args.length == 1) {
                MsgUtil.msg(sender, msg.getRemoveUsage());
                return;
            }
            Player player = MsgUtil.getPlayer(args[1]);
            if (player == null) {
                MsgUtil.msg(sender, msg.getCantFindPlayer());
                return;
            }

            if (!heart.storage().getClaimed().contains(player.getUniqueId().toString())) {
                MsgUtil.msg(sender, msg.getUserNotReclaimed());
                return;
            }

            heart.storage().getClaimed().remove(player.getUniqueId().toString());
            ConfigUtil.save(heart.storage(), heart.storage().getPath());
            MsgUtil.msg(sender, msg.getRemoveSuccess(), new MsgUtil.Pair<>("{player}", player.getName()));
            return;
        }
    }

    List<String> blank = new ArrayList<>();

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) return tabCompleteHelper(args[0], Arrays.asList("reload", "help", "reset", "remove"));
        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) return null;
        return blank;
    }
}
