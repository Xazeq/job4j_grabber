package ru.job4j.grabber;

import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.html.SqlRuParse;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {
    private final Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = cnn.prepareStatement(
                "insert into post (name, description, link, created) "
                        + "values (?, ?, ?, ?) "
                        + "on conflict do nothing;",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement(
                "select * from post;")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("link"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement = cnn.prepareStatement(
                "select * from post where id = ?;"
        )) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    post = new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("link"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    );
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        Properties cfg = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("app.properties")) {
            cfg.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        PsqlStore store = new PsqlStore(cfg);
        SqlRuParse parse = new SqlRuParse(new SqlRuDateTimeParser());
        List<Post> posts = parse.list("https://www.sql.ru/forum/job-offers");
        posts.forEach(store::save);
        List<Post> postsFromDB = store.getAll();
        postsFromDB.forEach(System.out::println);
        System.out.println("------------------------------------");
        Post postFoundById = store.findById(1);
        System.out.println(postFoundById);
    }
}
