package com.app.mybook.api;

/**
 * Created by 王海 on 2015/4/13.
 */
public class Api {

    //符合搜索结果的所有图书信息
    public static final String BOOK_SEARCH = "https://api.douban.com/v2/book/search?q=";
    public static final String BOOK_SEARCH_FIELDS = "&fields=id,title,author,publisher,pubdate,price,image,translator,rating";

    //某本书的详细信息 BOOK_INFO+"bookId"
    public static final String BOOK_INFO = "https://api.douban.com/v2/book/";
    public static final String BOOK_INFO_FIELDS = "?fields=isbn13,tags,pages,author_intro,summary,catalog";

    public static final String BOOK_INFO_ISBN = "https://api.douban.com/v2/book/isbn/";
    public static final String BOOK_INFO_FIELDS_ISBN = "?fields=id,title,author,publisher,pubdate,price,image,translator,rating,isbn13,tags,pages,author_intro,summary,catalog";

    //BOOK_INFO+"bookId"+BOOK_LIST_NOTE 获得某本图书的所有笔记
    public static final String BOOK_LIST_NOTE = "/annotations";
    public static final String BOOK_LIST_NOTE_FIELDS = "?fields=id,author_user,chapter,page_no,abstract,time";

    //某篇笔记的详细信息 BOOK_INFO_NOTE+"annotationId"
    public static final String BOOK_INFO_NOTE = "https://api.douban.com/v2/book/annotation/";
    public static final String BOOK_INFO_NOTE_FIELDS = "?fields=content";

    //条件筛选----待定
    public static final String BOOK_ = "&fields=id,title,url,publisher,image";

}
