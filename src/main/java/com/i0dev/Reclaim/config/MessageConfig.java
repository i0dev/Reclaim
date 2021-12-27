package com.i0dev.Reclaim.config;

import com.i0dev.Reclaim.Heart;
import com.i0dev.Reclaim.templates.AbstractConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MessageConfig extends AbstractConfiguration {


    String helpPageHeader = "&7&m_______&r&8[&r &c&lReclaim &8]&m_______";
    String reloadUsage = " &c* &7/Reclaim reload";
    String helpUsage = " &c* &7/Reclaim help";
    String baseUsage = " &c* &7/Reclaim";
    String resetUsage = " &c* &7/Reclaim reset";
    String removeUsage = " &c* &7/Reclaim remove <player>";

    String resetSuccess = "&aReset data successfully";
    String removeSuccess = "&aRemoved {player}'s claim status successfully";
    String claimSuccess = "&aYou reclaimed your reward for {displayName}";

    String alreadyClaimed = "&cYou already claimed your reclaim!";
    String userNotReclaimed = "&cUser has not reclaimed yet";
    String reloadedConfig = "&7You have&a reloaded&7 the configuration.";
    String noPermission = "&cYou don not have permission to run that command.";
    String cantFindPlayer = "&cThe player: &f{player}&c cannot be found!";
    String invalidNumber = "&cThe number &f{num} &cis invalid! Try again.";
    String cantRunAsConsole = "&cYou cannot run this command from console.";

    public MessageConfig(Heart heart, String path) {
        this.path = path;
        this.heart = heart;
    }
}
