package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                posts.add(detail(href.attr("href")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        String title = "default";
        String desc = "default";
        LocalDateTime created = LocalDateTime.now();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements header = doc.select(".messageHeader");
            Elements body = doc.select(".msgBody");
            Elements footer = doc.select(".msgFooter");
            title = header.get(0).text()
                    .split(" \\[new]")[0];
            desc = body.get(1).text();
            created = dateTimeParser.parse(footer.first().text().split(" \\[")[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Post(title, link, desc, created);
    }
}
