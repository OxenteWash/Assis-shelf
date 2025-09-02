package br.com.oxentech.domain;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.Objects;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "image")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "content_type")
    private String contentType;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return (
            Objects.equals(id, image.id) &&
            Objects.equals(fileName, image.fileName) &&
            Objects.equals(contentType, image.contentType) &&
            Objects.deepEquals(data, image.data)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, contentType, Arrays.hashCode(data));
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return (
            "Image{" +
            "id=" +
            id +
            ", fileName='" +
            fileName +
            '\'' +
            ", contentType='" +
            contentType +
            '\'' +
            ", data=" +
            Arrays.toString(data) +
            '}'
        );
    }
}
