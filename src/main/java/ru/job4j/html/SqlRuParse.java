package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import java.time.LocalDateTime;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        int page = 1;
        while (page <= 5) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + page++).get();
            Elements row = doc.select(".postslisttopic");
            Elements date = doc.select(".altCol");
            int index = 1;
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(date.get(index).text());
                index += 2;
            }
        }
    }

    public static Post getPost(String url) throws Exception {
        Document doc = Jsoup.connect(url).get();
        Elements header = doc.select(".messageHeader");
        Elements body = doc.select(".msgBody");
        Elements footer = doc.select(".msgFooter");
        String title = header.get(0).text()
                .split(" \\[new]")[0];
        String desc = body.get(1).text();
        LocalDateTime created = new SqlRuDateTimeParser()
                .parse(footer.first().text().split(" \\[")[0]);
        return new Post(title, url, desc, created);
    }
}
