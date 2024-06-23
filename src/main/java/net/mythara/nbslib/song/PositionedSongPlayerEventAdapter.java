package net.mythara.nbslib.song;

import org.bukkit.entity.Player;

public interface PositionedSongPlayerEventAdapter extends SongPlayerEventAdapter {

    void onPlayerEnteredRange(Player player);
    void onPlayerLeftRange(Player player);

}
