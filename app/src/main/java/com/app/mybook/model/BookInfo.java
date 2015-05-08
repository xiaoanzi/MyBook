package com.app.mybook.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by 王海 on 2015/4/13.
 */
@Table(name = "bookInfo")
public class BookInfo extends Model implements Serializable{
    @Column(name = "bookSearchId")
    private String bookSearchId;  //书店ID
    @Column(name = "isbn10")
    private String isbn10;  //书的isbn10编码
    @Column(name = "isbn13")
    private String isbn13;  //书的isbn13编码
    @Column(name = "title")
    private String title;   //书名
    @Column(name = "author")
    private String author;  //作者
    @Column(name = "publisher")
    private String publisher;   //出版社
    @Column(name = "pubdate")
    private String pubdate; //出版时间
    @Column(name = "price")
    private String price;   //价格
    @Column(name = "image")
    private String image;
    @Column(name = "translator")
    private String translator;
    @Column(name = "rating")
    private Rating rating;  //书的其他属性
    @Column(name = "pages")
    private String pages;   //书的总页数
    @Column(name = "tags")
    private String tags; //书的标签
    @Column(name = "summary")
    private String summary; //书的简介
    @Column(name = "author_intro")
    private String author_intro;    //作者信息
    @Column(name = "catalog")
    private String catalog; //目录

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }
}
