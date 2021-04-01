package com.example.mymoviememoir.entities;

public class Memoir {
    private int memid;
    private String moviename;
    private String releasedate;
    private String watcheddatetime;
    private String comment;
    private Double ratingscore;
    private Cinema cinemaid;
    private Person personid;

    public Memoir(int memid, String moviename, String releasedate, String watcheddatetime, String comment, Double ratingscore) {
        this.memid = memid;
        this.moviename = moviename;
        this.releasedate = releasedate;
        this.watcheddatetime = watcheddatetime;
        this.comment = comment;
        this.ratingscore = ratingscore;
    }

    public int getMemid() {
        return memid;
    }

    public void setMemid(int memid) {
        this.memid = memid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getWatcheddatetime() {
        return watcheddatetime;
    }

    public void setWatcheddatetime(String watcheddatetime) {
        this.watcheddatetime = watcheddatetime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getRatingscore() {
        return ratingscore;
    }

    public void setRatingscore(Double ratingscore) {
        this.ratingscore = ratingscore;
    }

    public Cinema getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(int cinemaid) {
        this.cinemaid = new Cinema(cinemaid);
    }

    public Person getPersonid() {
        return personid;
    }

    public void setPersonid(int personid) {
        this.personid = new Person(personid);
    }
}
