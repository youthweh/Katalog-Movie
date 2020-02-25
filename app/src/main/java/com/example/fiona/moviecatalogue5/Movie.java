package com.example.fiona.moviecatalogue5;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String foto;
    private String nama;
    private String deskripsi;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.foto);
        dest.writeString(this.nama);
        dest.writeString(this.deskripsi);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.foto = in.readString();
        this.nama = in.readString();
        this.deskripsi = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
