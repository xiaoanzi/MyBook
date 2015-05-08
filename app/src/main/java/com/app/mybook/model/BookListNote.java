package com.app.mybook.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by 王海 on 2015/4/13.
 */
@Table(name = "bookListNote")
public class BookListNote extends Model implements Serializable{
    @Column(name = "bookListNoteId")
    private String bookListNoteId;
    @Column(name = "book_id")
    private String book_id;
    @Column(name = "author_user")
    private AuthorUser author_user;
    @Column(name = "page_no")
    private String page_no; //当前笔记所在的页数
    @Column(name = "chapter")
    private String chapter; //当前笔记所在的章节
    @Column(name = "a_abstract")
    private String a_abstract;  //笔记的简略信息
    @Column(name = "content")
    private String content;   //笔记的详细信息
    @Column(name = "time")
    private String time;

    public String getBookListNoteId() {
        return bookListNoteId;
    }

    public void setBookListNoteId(String bookListNoteId) {
        this.bookListNoteId = bookListNoteId;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public AuthorUser getAuthor_user() {
        return author_user;
    }

    public void setAuthor_user(AuthorUser author_user) {
        this.author_user = author_user;
    }

    public String getPage_no() {
        return page_no;
    }

    public void setPage_no(String page_no) {
        this.page_no = page_no;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getA_abstract() {
        return a_abstract;
    }

    public void setA_abstract(String a_abstract) {
        this.a_abstract = a_abstract;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
