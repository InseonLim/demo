package com.example.demo.payload;

public class DemoResponse {

    private String fileName;
    private String fileDownloadURi;
    private String fileType;
    private long size;

    public DemoResponse(String fileName, String fileDownloadURi, String fileType, long size){
        this.fileName = fileName;
        this.fileDownloadURi = fileDownloadURi;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName(){
        return fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFileDownloadURi(){
        return fileDownloadURi;
    }
    public void setFileDownloadURi(String fileDownloadURi){
        this.fileDownloadURi = fileDownloadURi;
    }
    public String getFileType(){
        return fileType;
    }
    public void setFileType(String fileType){
        this.fileType = fileType;
    }
    public long getSize(){
        return size;
    }
    public void setSize(long size){
        this.size = size;
    }
}
