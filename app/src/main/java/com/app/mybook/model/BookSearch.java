package com.app.mybook.model;

import java.io.Serializable;

/**
 * Created by 王海 on 2015/4/13.
 */
public class BookSearch implements Serializable{
    private String bookSearchId;  //书店ID
    private String isbn10;  //书的isbn10编码
    private String isbn13;  //书的isbn13编码
    private String title;   //书名
    private String author;  //作者
    private String publisher;   //出版社
    private String pubdate; //出版时间
    private String price;   //价格
    private String image;
    private String translator;
    private Rating rating;  //书的其他属性

    public String getBookSearchId() {
        return bookSearchId;
    }

    public void setBookSearchId(String bookSearchId) {
        this.bookSearchId = bookSearchId;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
