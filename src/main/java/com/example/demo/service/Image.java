package com.example.demo.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="Image")
public class Image {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="file_name")
    private String fileName;

    @Column(name="size")
    private long size;

    @Column(name="mime_type")
    private String mimeType;

    @Column(name="path")
    private String path;

    @CreationTimestamp
    @Column(name="insert_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertDate;

    public Image(String fileName, long size, String mimeType, String path){
        this.fileName = fileName;
        this.size = size;
        this.mimeType = mimeType;
        this.path = path;
    }

    public int getId(){
        return id;
    }
    public void setId(){
        this.id = id;
    }
    public String getFileName(){
        return fileName;
    }
    public void setFileName(){
        this.fileName = fileName;
    }
    public long getSize(){
        return size;
    }
    public void setSize(){
        this.size = size;
    }
    public String getMimeType(){
        return mimeType;
    }
    public void setMimeType(){
        this.mimeType = mimeType;
    }

    @Override
    public String toString(){
        return "Image [id=" + id + ", fileName=" + fileName + ", size=" + size + ", mimeType=" + mimeType + ", insertDate=" + insertDate +"]";
    }
}
