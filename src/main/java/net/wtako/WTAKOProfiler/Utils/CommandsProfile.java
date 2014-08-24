package net.wtako.WTAKOProfiler.Utils;

import net.wtako.WTAKOProfiler.Main;
import net.wtako.WTAKOProfiler.Commands.Profile.ArgHelp;
import net.wtako.WTAKOProfiler.Commands.Profile.ArgReload;
import net.wtako.WTAKOProfiler.Commands.Profile.ArgTSh;

public enum CommandsProfile implements BaseCommands {

    MAIN_COMMAND(Lang.HELP_HELP.toString(), ArgHelp.class, Main.artifactId + ".use"),
    H(Lang.HELP_HELP.toString(), ArgHelp.class, Main.artifactId + ".use"),
    HELP(Lang.HELP_HELP.toString(), ArgHelp.class, Main.artifactId + ".use"),
    TSH(Lang.HELP_TSH.toString(), ArgTSh.class, Main.artifactId + ".use"),
    RELOAD(Lang.HELP_RELOAD.toString(), ArgReload.class, Main.artifactId + ".reload");

    private String   helpMessage;
    private Class<?> targetClass;
    private String   permission;

    private CommandsProfile(String helpMessage, Class<?> targetClass, String permission) {
        this.helpMessage = helpMessage;
        this.targetClass = targetClass;
        this.permission = permission;
    }

    @Override
    public String getHelpMessage() {
        return helpMessage;
    }

    @Override
    public Class<?> getTargetClass() {
        return targetClass;
    }

    @Override
    public String getRequiredPermission() {
        return permission;
    }
}
