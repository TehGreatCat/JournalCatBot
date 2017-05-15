package gov.nasa.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

import java.awt.*;

@Entity(name = "Note")
public class Note {

    @Id
    private Long id;

    @Parent //Parent?
    private Key<?> creator;

    @JsonProperty("Text")
    private String text;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("TimeStamp")
    private String timestamp;

    public Note(String text, String image, String timestamp) {
        this.text = text;
        this.image = image;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Key<?> getCreator() {
        return creator;
    }

    public void setCreator(Key<?> creator) {
        this.creator = creator;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (id != null ? !id.equals(note.id) : note.id != null) return false;
        if (creator != null ? !creator.equals(note.creator) : note.creator != null) return false;
        if (text != null ? !text.equals(note.text) : note.text != null) return false;
        if (image != null ? !image.equals(note.image) : note.image != null) return false;
        return timestamp != null ? timestamp.equals(note.timestamp) : note.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", creator=" + creator +
                ", text='" + text + '\'' +
                ", image=" + image +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}