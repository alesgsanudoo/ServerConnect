package me.mralecroyt.server;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.config.*;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.event.*;
import java.util.*;

public class ServerCMD extends Command
{
    public String PREFIX;

    public ServerCMD() {
        super("gserver", "groyland.server", new String[0]);
        this.PREFIX = Main.PREFIX;
    }

    public void execute(final CommandSender sender, final String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer p = (ProxiedPlayer) sender;
            if (p.getServer().getInfo().getName().equals("AuthLobby")) {
                return;
            }
        }
        final Map<String, ServerInfo> servers = (Map<String, ServerInfo>) ProxyServer.getInstance().getServers();
        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
                final ProxiedPlayer p2 = (ProxiedPlayer) sender;
                sender.sendMessage((BaseComponent) new TextComponent(String.valueOf(this.PREFIX) + "§fEstas conectado en: §a" + p2.getServer().getInfo().getName()));
            }
            final TextComponent serverList = new TextComponent(String.valueOf(this.PREFIX) + ProxyServer.getInstance().getTranslation("server_list", new Object[0]));
            serverList.setColor(ChatColor.GOLD);
            for (final ServerInfo server : servers.values()) {
                if (server.canAccess(sender)) {
                    final TextComponent serverTextComponent = new TextComponent(", " + server.getName());
                    final int count = server.getPlayers().size();
                    serverTextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(String.valueOf(count) + ((count == 1) ? " jugador" : " jugadores") + "\n").italic(true).color(ChatColor.WHITE).append("Click para conectarte al servidor").bold(true).color(ChatColor.GOLD).create()));
                    serverTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gserver " + server.getName()));
                    serverList.addExtra((BaseComponent) serverTextComponent);
                }
            }
            sender.sendMessage((BaseComponent) serverList);
        } else {
            if (!(sender instanceof ProxiedPlayer)) {
                return;
            }
            final ProxiedPlayer player = (ProxiedPlayer) sender;
            final ServerInfo server2 = servers.get(args[0]);
            if (server2 == null) {
                player.sendMessage((BaseComponent) new TextComponent(String.valueOf(this.PREFIX) + "§fEste §aservidor §7no existe"));
            } else if (!server2.canAccess((CommandSender) player)) {
                player.sendMessage((BaseComponent) new TextComponent(String.valueOf(this.PREFIX) + "§cComando desconocido."));
            } else {
                if (server2.getName().equals("AuthLobby")) {
                    return;
                }
                player.connect(server2, ServerConnectEvent.Reason.COMMAND);
                player.sendMessage((BaseComponent) new TextComponent(String.valueOf(this.PREFIX) + "§cComando desconocido."));
            }

        }
    }

    public void onChat(final ChatEvent e) {
        final ProxiedPlayer p = (ProxiedPlayer)e.getSender();
        if (p.getServer().getInfo().getName().equals("AuthLobby")) {
            if (e.getMessage().startsWith("/l") || e.getMessage().startsWith("/reg") || e.getMessage().startsWith("/login") || e.getMessage().startsWith("/register") || e.getMessage().startsWith("/code"))  {
                return;
            }
            e.setCancelled(true);
        }
    }
}

