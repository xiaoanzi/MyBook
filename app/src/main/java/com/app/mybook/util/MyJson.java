package com.app.mybook.util;

import android.util.Log;

import com.app.mybook.model.AuthorUser;
import com.app.mybook.model.BookInfo;
import com.app.mybook.model.BookListNote;
import com.app.mybook.model.Rating;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王海 on 2015/4/16.
 */
public class MyJson {
    public static List<BookInfo> bookSearchList = new ArrayList<BookInfo>();
    public static List<BookListNote> bookListNoteList = new ArrayList<BookListNote>();

    //解析搜索图书返回回来的数据
    public static List<BookInfo> jsonObjectBooks(JSONObject jsonObject){
        try {
            bookSearchList.clear();
            JSONArray jsonArray = (jsonObject.getJSONArray("books"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                BookInfo bookSearch = new BookInfo();
                bookSearch.setBookSearchId(temp.getString("id"));
                bookSearch.setTitle(temp.getString("title"));

                String acthor = "";
                JSONArray jsonArrayTemp = (temp.getJSONArray("author"));
                for (int j = 0; j < jsonArrayTemp.length(); j++) {
                    acthor += jsonArrayTemp.get(j).toString();
                    if(j != jsonArrayTemp.length() - 1){
                        acthor += "、";
                    }
                }
                bookSearch.setAuthor(acthor);
                bookSearch.setPublisher(temp.getString("publisher"));
                bookSearch.setPubdate(temp.getString("pubdate"));
                bookSearch.setPrice(temp.getString("price"));
                bookSearch.setImage(temp.getString("image"));
                String translator = "";
                JSONArray translatorTemp = (temp.getJSONArray("translator"));
                for (int k = 0; k < translatorTemp.length(); k++) {
                    acthor += translatorTemp.get(k).toString();
                    if(k != translatorTemp.length() - 1){
                        translator += "、";
                    }
                }
                bookSearch.setTranslator(translator);
                JSONObject rating = temp.getJSONObject("rating");
                Rating rating1 = new Rating();
                rating1.setNumRaters(rating.getString("numRaters"));
                rating1.setAverage(rating.getString("average"));
                bookSearch.setRating(rating1);
                bookSearchList.add(bookSearch);
            }
        }catch (Exception e){
            Log.e("jsonObjectBooks",e.toString());
            e.printStackTrace();
            bookSearchList = null;
        }
        return bookSearchList;
    }

    //解析某本图书具体信息返回回来的数据
    public static BookInfo jsonObjectBookIsbn(JSONObject jsonObject){
        BookInfo bookInfo = new BookInfo();
        try {
            bookInfo.setIsbn13(jsonObject.get("isbn13").toString());
            String tags = "";
            JSONArray jsonArrayTemp = (jsonObject.getJSONArray("tags"));
            for (int j = 0; j < jsonArrayTemp.length(); j++) {
                JSONObject temp = jsonArrayTemp.getJSONObject(j);
                tags += temp.getString("name");
                if(j != jsonArrayTemp.length() - 1){
                    tags += "、";
                }
            }
            bookInfo.setTags(tags);
            bookInfo.setPages(jsonObject.getString("pages"));
            bookInfo.setAuthor_intro(jsonObject.getString("author_intro"));
            bookInfo.setSummary(jsonObject.getString("summary"));
            bookInfo.setCatalog(jsonObject.getString("catalog"));
            bookInfo.setBookSearchId(jsonObject.get("id").toString());
            bookInfo.setTitle(jsonObject.get("title").toString());
            String acthor = "";
            JSONArray jsonArrayTemp2 = (jsonObject.getJSONArray("author"));
            for (int j = 0; j < jsonArrayTemp2.length(); j++) {
                acthor += jsonArrayTemp2.get(j).toString();
                if(j != jsonArrayTemp2.length() - 1){
                    acthor += "、";
                }
            }
            bookInfo.setAuthor(acthor);
            bookInfo.setPublisher(jsonObject.get("publisher").toString());
            bookInfo.setPubdate(jsonObject.get("pubdate").toString());
            bookInfo.setPrice(jsonObject.get("price").toString());
            bookInfo.setImage(jsonObject.get("image").toString());

            JSONObject rating = jsonObject.getJSONObject("rating");
            Rating rating2 = new Rating();
            rating2.setNumRaters(rating.getString("numRaters"));
            rating2.setAverage(rating.getString("average"));
            bookInfo.setRating(rating2);
        }catch (Exception e){
            bookInfo = null;
            Log.e("tag",e.toString());
        }
        return bookInfo;
    }

    //解析某本图书的的笔记列表返回回来的的数据
    public List<BookListNote> jsonObjectNotes(JSONObject jsonObject){
        try {
            bookListNoteList.clear();
            JSONArray jsonArray = (jsonObject.getJSONArray("annotations"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                BookListNote bookListNote = new BookListNote();
                bookListNote.setBookListNoteId(temp.getString("id"));

                JSONObject jsonUser = temp.getJSONObject("author_user");
                AuthorUser authorUser = new AuthorUser();
                authorUser.setName(jsonUser.getString("name"));
                authorUser.setAvatar(jsonUser.getString("avatar"));
                bookListNote.setAuthor_user(authorUser);

                bookListNote.setChapter(temp.getString("chapter"));
                bookListNote.setPage_no(temp.getString("page_no"));
                bookListNote.setA_abstract(temp.getString("abstract"));
                bookListNote.setTime(temp.getString("time"));
                bookListNoteList.add(bookListNote);
            }
        }catch (Exception e){
            bookListNoteList = null;
            Log.e("tag",e.toString());
        }
        return bookListNoteList;
    }
}
