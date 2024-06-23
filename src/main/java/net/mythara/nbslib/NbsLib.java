package net.mythara.nbslib;

import lombok.Getter;
import net.mythara.nbslib.listener.SongPlayerRangeListener;
import net.mythara.nbslib.player.PositionedSongPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class NbsLib extends JavaPlugin {

    private final Set<PositionedSongPlayer> positionedSongPlayers = ConcurrentHashMap.newKeySet();
    private boolean disabling;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SongPlayerRangeListener(this), this);
    }

    @Override
    public void onDisable() {
        disabling = true;
    }
}
