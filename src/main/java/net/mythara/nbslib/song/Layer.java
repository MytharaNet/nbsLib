package net.mythara.nbslib.song;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public final class Layer {

    private final Int2ObjectMap<Note> noteMap = new Int2ObjectOpenHashMap<>();

    @Setter
    private String name;
    @Setter
    private byte volume;

    @Override
    public boolean equals(final Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        Layer layer = (Layer) o;
        return volume == layer.volume &&
                noteMap.equals(layer.noteMap) &&
                name.equals(layer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteMap, name, volume);
    }

    @Override
    public String toString() {
        return "Layer{" +
                "noteMap=" + noteMap +
                ", name='" + name + '\'' +
                ", volume=" + volume +
                '}';
    }

}
