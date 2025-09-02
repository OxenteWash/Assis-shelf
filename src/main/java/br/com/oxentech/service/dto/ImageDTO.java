package br.com.oxentech.service.dto;

import br.com.oxentech.domain.Book;
import br.com.oxentech.domain.Image;

public class ImageDTO {

    private Long id;
    private String contentType;
    private String fileName;
    private byte[] file;
    private Book book;

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.contentType = image.getContentType();
        this.fileName = image.getFileName();
        this.book = image.getBook();
        this.file = image.getData();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
