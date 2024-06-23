package net.mythara.nbslib.song;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Note {

    private final byte instrument, key, velocity;
    private final short pitch;

    public Note(final byte instrument, final byte key, final byte velocity, final short pitch) {
        this.instrument = instrument;
        this.key = key;
        this.velocity = velocity;
        this.pitch = pitch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return instrument == note.instrument &&
                key == note.key &&
                velocity == note.velocity &&
                pitch == note.pitch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, key, velocity, pitch);
    }

    @Override
    public String toString() {
        return "Note{" +
                "instrument=" + instrument +
                ", key=" + key +
                ", velocity" + velocity +
                ", pitch =" + pitch +
                "}";
    }

}
