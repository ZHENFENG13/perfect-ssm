package test;

import com.ssm.promotion.core.entity.Article;
import com.ssm.promotion.core.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13 on 2017/12/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class RedisTest {
    @Resource
    private RedisUtil redisUtil;

    @Test
    public void redisPutTest() {
        //添加
        redisUtil.put("name", "perfect-ssm");
    }

    @Test
    public void redisGetTest() {
        //获取
        String str = (String) redisUtil.get("name", String.class);
        System.out.println(str);
    }

    @Test
    public void redisDeleteTest() {
        redisUtil.del("name");
    }


    @Test
    public void redisPutListTest() {
        List<String> stringList = new ArrayList<>();
        stringList.add("github");
        stringList.add("13");
        stringList.add("cnblog");
        stringList.add("perfect-ssm");
        //添加
        redisUtil.put("stringList", stringList);
    }

    @Test
    public void redisGetListTest() {
        //获取
        List<String> stringList = (List<String>) redisUtil.get("stringList", List.class);
        if (stringList.size() > 0) {
            for (String string : stringList
                    ) {
                System.out.println(string);
            }
        }
    }

    @Test
    public void redisPutListArticleTest() {
        List<Article> articles = new ArrayList<>();
        Article article1 = new Article();
        article1.setId("1");
        article1.setArticleContent("article1");
        Article article2 = new Article();
        article2.setId("2");
        article2.setArticleContent("article2");
        articles.add(article1);
        articles.add(article2);
        //添加
        redisUtil.put("articles", articles);
    }

    @Test
    public void redisGetArticleListTest() {
        //获取
        List<Article> articles = (List<Article>) redisUtil.get("articles", List.class);
        if (articles.size() > 0) {
            for (Article article : articles
                    ) {
                System.out.println(article);
            }
        }
    }
}
