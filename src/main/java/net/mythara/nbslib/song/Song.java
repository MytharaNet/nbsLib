package net.mythara.nbslib.song;

import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import lombok.Getter;
import net.mythara.nbslib.util.InputStreamUtil;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

@Getter
public final class Song {

    private static final int TICKS_PER_SECOND = 20;

    private final String title;
    private final String author;
    private final String originalAuthor;
    private final String description;

    private final float speed, delay;
    private final short height, length;

    private final Short2ObjectMap<Layer> layerMap;

    public Song(String title, String author, String originalAuthor, String description, float speed, short height, short length, Short2ObjectMap<Layer> layerMap) {
        this.title = title;
        this.author = author;
        this.originalAuthor = originalAuthor;
        this.description = description;
        this.speed = speed;
        this.delay = TICKS_PER_SECOND / speed;
        this.height = height;
        this.length = length;
        this.layerMap = layerMap;
    }

    @Nullable
    public static Song createFromFile(final File nbsFile) {
        final Short2ObjectMap<Layer> layerMap = new Short2ObjectOpenHashMap<>();

        try(final DataInputStream dataInputStream = new DataInputStream(new FileInputStream(nbsFile))) {
            short length = InputStreamUtil.readShort(dataInputStream);
            int version = 0;
            if(length == 0) {
                version = dataInputStream.readByte();
                dataInputStream.readByte();
                if(version >= 3)
                    length = InputStreamUtil.readShort(dataInputStream);
            }

            final short height = InputStreamUtil.readShort(dataInputStream);
            final String title = InputStreamUtil.readString(dataInputStream);
            final String author = InputStreamUtil.readString(dataInputStream);
            final String originalAuthor = InputStreamUtil.readString(dataInputStream);
            final String description = InputStreamUtil.readString(dataInputStream);
            final float speed = InputStreamUtil.readShort(dataInputStream) / 100f;

            // The following information is unnecessary; we're skipping it
            dataInputStream.readBoolean(); // Auto-save enabled
            dataInputStream.readByte(); // Auto-save period
            dataInputStream.readByte(); // Time signature
            InputStreamUtil.readInt(dataInputStream); // Minutes spent for project
            InputStreamUtil.readInt(dataInputStream); // Left clicks
            InputStreamUtil.readInt(dataInputStream); // Right clicks
            InputStreamUtil.readInt(dataInputStream); // Blocks added
            InputStreamUtil.readInt(dataInputStream); // Blocks removed
            InputStreamUtil.readString(dataInputStream); // .midi / .schem name

            // Only read this information in NBS 4 or higher
            if (version >= 4) {
                dataInputStream.readByte(); // Loop on/off
                dataInputStream.readByte(); // Max Loops
                InputStreamUtil.readShort(dataInputStream); // Loop start
            }

            short currentTick = -1;
            while (true) {
                final short jumpTicks = InputStreamUtil.readShort(dataInputStream);
                if(jumpTicks == 0) break;

                currentTick += jumpTicks;

                short currentLayer = -1;
                while (true) {
                    final short jumpLayers = InputStreamUtil.readShort(dataInputStream);
                    if(jumpLayers == 0) break;

                    currentLayer += jumpLayers;

                    byte instrumentIndex = dataInputStream.readByte();

                    final byte key = dataInputStream.readByte();
                    byte velocity = 100;
                    short pitch = 0;

                    if(version >= 4) {
                        velocity = dataInputStream.readByte();
                        dataInputStream.readByte();
                        pitch = InputStreamUtil.readShort(dataInputStream);
                    }

                    final Layer layer = layerMap.computeIfAbsent(currentLayer, index -> new Layer());
                    layer.getNoteMap().put(currentTick, new Note(instrumentIndex, key, velocity, pitch));
                }
            }

            if (version > 0 && version < 3) {
                length = currentTick;
            }

            for (short i = 0; i < height; i++) {
                final Layer layer = layerMap.get(i);
                final String name = InputStreamUtil.readString(dataInputStream);

                if(version >= 4) {
                    dataInputStream.readByte();
                }

                final byte volume = dataInputStream.readByte();

                if(version >= 2) {
                    dataInputStream.readByte();
                }

                if(layer != null) {
                    layer.setName(name);
                    layer.setVolume(volume);
                }
            }
            return new Song(title, author, originalAuthor, description, speed, height, length, layerMap);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "An error occurred while creating song from '{}'", nbsFile.getName());
        }
        return null;
    }

    public short getDuration() {
        return (short) (length / speed);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return title.equals(song.title) &&
                author.equals(song.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", originalAuthor='" + originalAuthor + '\'' +
                ", description='" + description + '\'' +
                ", speed=" + speed +
                ", delay=" + delay +
                ", height=" + height +
                ", length=" + length +
                ", layerMap=" + layerMap +
                '}';
    }
}

