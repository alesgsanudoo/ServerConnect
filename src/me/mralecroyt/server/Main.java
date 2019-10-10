package me.mralecroyt.server;


import net.md_5.bungee.api.plugin.*;

public class Main extends Plugin
{
    public static String PREFIX;

    static {
        Main.PREFIX = "";
    }

    public void onEnable() {
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new ServerCMD());
    }
}
