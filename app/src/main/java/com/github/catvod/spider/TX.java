package com.github.catvod.spider;

import android.content.Context;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.crawler.Spider;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class TX extends Spider {

    private static final String siteUrl = "https://v.qq.com";
    private static final String siteHost = "v.qq.com";

    @Override
    public void init(Context context) {
        super.init(context);
    }

    protected HashMap<String, String> getHeaders(String url) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("method", "GET");
        headers.put("Host", siteHost);
        headers.put("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        headers.put("accept-encoding", "gzip");
        return headers;
    }

    private String formatUrl(String Url) {
        if (Url.startsWith("//")) {

            return "http:" + Url;
        }

        return "";
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
            jilupian.put("type_name", "纪录片");

            classes.put(dianying);
            classes.put(dianshiju);
            classes.put(dongman);
            classes.put(shaoer);
            classes.put(jilupian);

            result.put("class", classes);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {

        int page = Integer.parseInt(pg);
        String cateUrl = siteUrl
                + "/x/bu/pagesheet/list?_all=1&append=1&channel=" + tid + "&listpage=1&offset=" + (page - 1) * 21
                + "&pagesize=21&sort=18";
        String content = OkHttpUtil.string(cateUrl, getHeaders(cateUrl));
        String remarks;
        JSONObject result = new JSONObject();
        try {
            Elements listItems = Jsoup.parse(content).select(".list_item");
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < listItems.size(); i++) {
                Element item = listItems.get(i);

                String Pd = item.select("a").attr("title");
                String pic = formatUrl(item.select("img").attr("src"));
                if (item.select(".figure_caption") == null) {
                    remarks = "";
                } else {
                    remarks = item.select(".figure_caption").text();
                }
                String Pd2 = item.select("a").attr("data-float");
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("vod_id", Pd2);
                jSONObject2.put("vod_name", Pd);
                jSONObject2.put("vod_pic", pic);
                jSONObject2.put("vod_remarks", remarks);
                jSONArray.put(jSONObject2);
            }
            result.put("page", pg);
            result.put("pagecount", Integer.MAX_VALUE);
            result.put("limit", 90);
            result.put("total", Integer.MAX_VALUE);
            result.put("list", jSONArray);
            return result.toString();
        } catch (Exception e) {

            SpiderDebug.log(e);
        }

        return "";
    }


    public String detailContent(List<String> ids) {
        return "";
    }

    public String playerContent(String flag, String id, List<String> vipFlags) {
        return "";
    }

}
