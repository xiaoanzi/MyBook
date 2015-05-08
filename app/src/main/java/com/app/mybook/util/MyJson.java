package com.app.mybook.util;

import android.util.Log;

import com.app.mybook.model.BookSearch;
import com.app.mybook.model.Rating;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王海 on 2015/4/16.
 */
public class MyJson {
    private List<BookSearch> bookSearchList = new ArrayList<BookSearch>();
    public List<BookSearch> jsonObjectBooks(JSONObject jsonObject){
        try {
            Log.e("tag", jsonObject.toString());
            JSONArray jsonArray = (jsonObject.getJSONArray("books"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                BookSearch bookSearch = new BookSearch();
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

            Log.e("tag",e.toString());
        }
        return bookSearchList;
    }
}
