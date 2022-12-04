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

        JSONObject doudou = new JSONObject();
        JSONObject yangyang = new JSONObject();

        doudou.put("type_id", "doudou");
        doudou.put("type_name", "豆豆");

        yangyang.put("type_id", "yangyang");
        yangyang.put("type_name", "洋洋");

        dianying.put("type_id", "movie");
        dianying.put("type_name", "电影");

        dianshiju.put("type_id", "tv");
        dianshiju.put("type_name", "电视剧");

        dongman.put("type_id", "cartoon");
        dongman.put("type_name", "动漫");

        shaoer.put("type_id", "child");
        shaoer.put("type_name", "少儿");

        jilupian.put("type_id", "doco");
        jilupian.put("type_name", "纪录片");

        classes.put(doudou);
        classes.put(yangyang);
        classes.put(dianshiju);
        classes.put(dianying);
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
        if (tid.equals("doudou") || tid.equals("yangyang")) {
            JSONObject result = new JSONObject();
            ArrayList<String> list = new ArrayList<String>();
            if (tid.equals("doudou")) {
                list.add("汪汪队立大功");
                list.add("超级飞侠");
                list.add("工程车益趣园");
                list.add("工程车城镇救援队");
                list.add("猪猪侠");
                list.add("熊出没");
            } else {
                list.add("奇迹少女");

            }
            JSONArray lists = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                String q_url = "http://node.video.qq.com/x/api/msearch?keyWord=" + list.get(i);
                System.out.println("q_url:" + q_url);
                JSONArray jSONArray = new JSONObject(OkHttpUtil.string(q_url, getHeaders(q_url)))
                        .getJSONArray("uiData");
                ArrayList<String> rlist = new ArrayList<String>();
                for (int j = 0; j < jSONArray.length(); j++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(j).getJSONArray("data").getJSONObject(0);
                    if (!rlist.contains(jSONObject.optString("title"))
                            && !jSONObject.optString("id").equals("相关应用")) {

                        rlist.add(jSONObject.optString("title"));
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("vod_id", jSONObject.optString("id"));
                        jSONObject2.put("vod_name", jSONObject.optString("title"));
                        jSONObject2.put("vod_pic", jSONObject.optString("posterPic"));
                        jSONObject2.put("vod_remarks", jSONObject.optString("publishDate"));
                        lists.put(jSONObject2);
                    }
                }

            }
            result.put("page", 1);
            result.put("pagecount", 1);
            result.put("limit", Integer.MAX_VALUE);
            result.put("total", Integer.MAX_VALUE);
            result.put("list", lists);

            return result.toString();
        } else {
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
                result.put("page", page);
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
