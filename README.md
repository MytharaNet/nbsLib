# nbsLib
Lightweight library to play .nbs-Files

Fork of https://github.com/Cytooxien/nbsLib/

## **Examples:**

Playing a song to specific players or whole server (e.g. server-radio):
```java
final Song song = Song.createFromFile(new File("songFile.nbs"));
final SimpleSongPlayer songPlayer = new SimpleSongPlayer(song);
// If you're not adding specific players, song will be played for everybody.
        
songPlayer.addListeningPlayer(uuid); // Replace with uuid of player to be added.
songPlayer.play();
```

Playing a song to players within a certain range:
```java
final Location centerLocation = new Location(Bukkit.getWorld("world"), 0, 100, 0);
final Song song = Song.createFromFile(new File("songFile.nbs"));
final PositionedSongPlayer songPlayer = new PositionedSongPlayer(centerLocation, song);
songPlayer.setDistance(10); // Radius in blocks

// If your not adding specific players, song will be played for everybody.
songPlayer.addListeningPlayer(uuid); // Replace with uuid of player to be added.
songPlayer.play();
```

Add own event-adapter:
```java
final PositionedSongPlayer songPlayer = new PositionedSongPlayer(new Location(Bukkit.getWorld("world"), 0, 100, 0),
        Song.createFromFile(new File("songFile.nbs")));

songPlayer.addEventAdapter(new PositionedSongPlayerEventAdapter() {  
    @Override
    public void onEnd() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onPlay(Song song) {

    }

    @Override
    public void onPlayerAdded(Player player) {

    }

    @Override
    public void onPlayerRemoved(Player player) {

    }

    @Override
    public void onPlayerEnteredRange(Player player) {

    }

    @Override
    public void onPlayerLeftRange(Player player) {

    }
});
songPlayer.play();
```
