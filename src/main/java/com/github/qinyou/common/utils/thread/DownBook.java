package com.github.qinyou.common.utils.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.utils.RandomUtils;
import com.github.qinyou.common.utils.RegexUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.jfinal.kit.Ret;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 追书神器 api
 */
@Slf4j
public class DownBook {

    // 请求编码
    private final static String charset = "UTF-8";
    // 请求超时时间
    private final static int connectTimeout = 5000;
    // UA
    private final static String[] userAgents = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36", "" +
            "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1",
            "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Mobile Safari/537.36",
            "Mozilla/5.0 (iPad; CPU OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1"
    };

    public static void main(String[] args) throws InterruptedException {
        String booksDir = "D:/books/";
        Integer showNumber = 50;
        String keywords = scanner("搜索关键字");
        String jsonStr = fuzzySearch(keywords, 0, showNumber);
        if (StringUtils.isEmpty(jsonStr)) {
            System.out.println("无响应,程序结束...");
            return;
        }
        JSONObject json = JSONObject.parseObject(jsonStr);
        if (!json.getBoolean("ok")) {
            System.out.println("接口异常,程序结束...");
            return;
        }
        JSONArray books = json.getJSONArray("books");
        for (int ix = 0; ix < books.size(); ix++) {
            System.out.println(String.format("%-2s .%4s万字. %s(%s)", ix + 1,
                    books.getJSONObject(ix).getInteger("wordCount") / 10000,
                    books.getJSONObject(ix).getString("title"),
                    books.getJSONObject(ix).getString("author")
            ));
            //System.out.println(ix+1+"."+books.getJSONObject(ix).getString("title")+" @ "+books.getJSONObject(ix).getString("author"));
        }

        String ixs = scanner("需下载序号(1~" + showNumber + "),多个之间使用,号分隔");
        String[] ixAry = ixs.split(",");
        Set<Integer> ixSet = new LinkedHashSet<>();
        for (String ix : ixAry) {
            if (!RegexUtils.regExpVali(RegexUtils.pattern_integer_1, ix)) {
                System.out.println("序号格式错误,程序结束...");
                return;
            }
            Integer ixI = Integer.parseInt(ix);
            if (ixI > 0 && ixI <= showNumber) {
                ixSet.add(ixI);
            }
        }

        Ret ret;
        String title;
        String author;
        Integer wordCount;
        for (Integer ix : ixSet) {
            title = books.getJSONObject(ix - 1).getString("title");
            author = books.getJSONObject(ix - 1).getString("author");
            wordCount = books.getJSONObject(ix - 1).getInteger("wordCount") / 10000;
            System.out.println(String.format("开始下载 %-2s .%4s万字. %s(%s)", ix, wordCount, title, author));
            ret = saveToTxtQuick(books.getJSONObject(ix - 1).getString("_id"), booksDir + String.format("%s(%s.%s万).txt", title, author, wordCount));
            System.out.println(ret);
        }

    }

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.notEmpty(ipt)) {
                return ipt;
            }
        }
        throw new RuntimeException("请输入正确的" + tip + "！");
    }


    /**
     * Get 请求代理
     *
     * @param url
     * @return
     */
    public static String getProxy(String url) {
        log.info(" ---- get url: {}", url);

        String resStr = null;
        Content content;
        try {
            content = Request.Get(url).setHeader("User-Agent", userAgents[RandomUtils.number(0, userAgents.length)])
                    .connectTimeout(connectTimeout).execute().returnContent();
            resStr = content.asString(Charset.forName(charset));
            log.debug(resStr);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return resStr;
    }


    /**
     * 关键字模糊查询小说
     *
     * @param keyword
     * @param start
     * @param limit
     * @return
     */
    public static String fuzzySearch(String keyword, Integer start, Integer limit) {
        String url = "http://api.zhuishushenqi.com/book/fuzzy-search?query=" + keyword + "&start=" + start + "&limit=" + limit;
        return getProxy(url);
    }


    /**
     * 小说详情
     *
     * @param nid 小说id
     * @return
     */
    public static String book(String nid) {
        String url = "http://api.zhuishushenqi.com/book/" + nid;
        return getProxy(url);
    }

    /**
     * 章节列表
     *
     * @param nid
     * @return
     */
    public static String chapters(String nid) {
        Map<String, Object> map = new HashMap<String, Object>();
        String url = "http://api.zhuishushenqi.com/mix-atoc/" + nid + "?view=chapters";
        return getProxy(url);
    }

    /**
     * 章节详情
     *
     * @param url
     * @return
     */
    public static String chapter(String url) {
        try {
            url = URLEncoder.encode(url, charset);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return "";
        }
        String dUrl = "http://chapter2.zhuishushenqi.com/chapter/" + url;
        return getProxy(dUrl);
    }

    /**
     * 下载小说
     *
     * @param nid
     * @param savePath
     * @return
     * @throws InterruptedException
     */
    public static Ret saveToTxtQuick(String nid, String savePath) throws InterruptedException {
        Ret ret = Ret.create();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        String chapters = chapters(nid);
        if (StringUtils.isEmpty(chapters)) {
            return ret.setFail().set("msg", "api data blank");
        }
        JSONObject json = JSONObject.parseObject(chapters);
        if (!json.getBoolean("ok")) {
            return ret.setFail().set("msg", "api request error");
        }
        JSONArray jArray = json.getJSONObject("mixToc").getJSONArray("chapters");
        CountDownLatch latch = new CountDownLatch(jArray.size());

        for (int ix = 0; ix < jArray.size(); ix++) {
            ExecutorServiceUtils.pool.execute(new DownChapter(jArray.getJSONObject(ix), latch));
        }

        // throw InterruptedException ( why & when ?)
        latch.await();

        File saveFile = new File(savePath);
        String content;
        for (int ix = 0; ix < jArray.size(); ix++) {
            content = jArray.getJSONObject(ix).getString("title") + "(" + (ix + 1) + ") \n" + jArray.getJSONObject(ix).getString("body") + " \n";
            try {
                FileUtils.writeStringToFile(saveFile, content, Constant.DEFAULT_ENCODEING, true);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        ret.setOk().set("path", savePath);
        return ret;
    }

}

@Slf4j
class DownChapter extends BaseServiceThread {
    private JSONObject jsonObject;

    public DownChapter(JSONObject jsonObject, CountDownLatch latch) {
        super("下载章节线程", latch);
        this.jsonObject = jsonObject;
    }

    @Override
    public void service() {
        if (!jsonObject.getBoolean("unreadble")) {
            String jsonStr = DownBook.chapter(jsonObject.getString("link"));
            if (StringUtils.isEmpty(jsonStr)) {
                log.info("{} api data blank", jsonObject.getString("link"));
                return;
            }
            JSONObject chapterJson = JSONObject.parseObject(jsonStr);
            if (!chapterJson.getBoolean("ok")) {
                log.info("{} api request error", jsonObject.getString("link"));
                return;
            }
            jsonObject.put("body", chapterJson.getJSONObject("chapter").getString("body"));
        }
    }
}
