package com.bat.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;


public class BookForm implements Serializable {
    public Long id;
    public String name;
    public int price;
    public String author;
    public MultipartFile image;
    public Category category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BookForm() {
        this.name = "Giang";
    }

    public BookForm(String name, int price, String author, MultipartFile image, Category category) {
        this.name = name;
        this.price = price;
        this.author = author;
        this.image = image;
        this.category = category;
    }

    public BookForm(Long id, String name, int price, String author, MultipartFile image, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.author = author;
        this.image = image;
        this.category = category;
    }
}
