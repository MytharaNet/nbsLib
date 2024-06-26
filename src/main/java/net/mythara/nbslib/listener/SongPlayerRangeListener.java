package net.mythara.nbslib.listener;

import net.mythara.nbslib.NbsLib;
import net.mythara.nbslib.player.PositionedSongPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class SongPlayerRangeListener implements Listener {

    private final NbsLib plugin;

    public SongPlayerRangeListener(final NbsLib plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        processRange(player, player.getLocation());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        for(final PositionedSongPlayer songPlayer : plugin.getPositionedSongPlayers()) {
            songPlayer.playerLeftRange(player);
        }
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if (event.getTo() == null) return;

        final Player player = event.getPlayer();
        if(event.getFrom().getBlockX() == event.getTo().getBlockX()
            && event.getFrom().getBlockY() == event.getTo().getBlockY()
            && event.getFrom().getBlockZ() == event.getTo().getBlockZ())
            return;

        processRange(player, event.getTo());
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        processRange(player, event.getTo());
    }

    @EventHandler
    public void onWorldChanged(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        for(final PositionedSongPlayer songPlayer : plugin.getPositionedSongPlayers()) {
            songPlayer.playerLeftRange(player);
        }

        processRange(player, player.getLocation());
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        for(final PositionedSongPlayer songPlayer : plugin.getPositionedSongPlayers()) {
            songPlayer.playerLeftRange(player);
        }

        processRange(player, event.getRespawnLocation());
    }

    private void processRange(final Player player, final Location location) {
        for(final PositionedSongPlayer songPlayer : plugin.getPositionedSongPlayers()) {
            if(!Objects.equals(songPlayer.getTargetLocation().getWorld(), location.getWorld()))
                continue;

            final Set<UUID> listeningPlayers = songPlayer.getListeningPlayers();
            if(listeningPlayers.isEmpty() || listeningPlayers.contains(player.getUniqueId())) {
                final double newDistance = player.getLocation().distanceSquared(songPlayer.getTargetLocation());
                if(newDistance > songPlayer.getDistanceSquared()) {
                    songPlayer.playerLeftRange(player);
                } else {
                    songPlayer.playerEnteredRange(player);
                }
            }
        }
    }

}
