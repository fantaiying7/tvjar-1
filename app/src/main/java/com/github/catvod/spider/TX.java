package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.okhttp.OkHttpUtil;
import com.github.catvod.utils.Misc;

import org.json.JSONArray;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class HOME extends Spider {

    private static final String siteUrl = "https://v.qq.com";
    private static final String siteHost = "v.qq.com";
    @Override
    public void init(Context context) {
        super.init(context);
    }

    public String homeContent(boolean filter) {
        try {

        JSONObject result = new JSONObject();
        JSONArray classes = new JSONArray();

        JSONObject dianying = new JSONObject();
        JSONObject dianshiju = new JSONObject();
        JSONObject dongman = new JSONObject();
        JSONObject shaoer = new JSONObject();
        JSONObject jilupian = new JSONObject();

        dianying.put("type_id", "dianying");
        dianying.put("type_name", "电影");

        dianshiju.put("type_id", "dianshiju");
        dianshiju.put("type_name", "电视剧");

        dongman.put("type_id", "dongman");
        dongman.put("type_name", "动漫");

        shaoer.put("type_id", "shaoer");
        shaoer.put("type_name", "少儿");

        jilupian.put("type_id", "jilupian");
        jilupian.put("type_name", "jilupian");

        classes.put(dianying);
        classes.put(dianshiju);
        classes.put(dongman);
        classes.put(shaoer);
        classes.put(jilupian);

        result.put("classes", classes);
        return result.toString();
                }
            }
            // System.out.println("=============："+classes);
            return result.toString();
        }catch(Exception e){
            SpiderDebug.log(e);
        }
        return "";
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
        }catch(Exception e){
            SpiderDebug.log(e);
        }
        return "";
    }


    public String detailContent(List<String> ids) {
        try {
        }catch(Exception e){
            SpiderDebug.log(e);
        }


        return "";
    }
    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
            }
            return "";
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

}

