package com.koral.application.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private Long id;


    private String artist;

    private String name;

    @ManyToMany(mappedBy = "songs")
    private List<User> users;

    public Song(String artist, String name) {
        this.artist = artist;
        this.name = name;
    }

    public Song() {

    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Song{" +
                "artist='" + artist + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String query(){
        return this.artist + this.name;
    }
}
