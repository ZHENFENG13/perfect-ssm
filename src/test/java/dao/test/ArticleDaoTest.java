package dao.test;

import com.ssm.promotion.core.dao.ArticleDao;
import com.ssm.promotion.core.entity.Article;
import com.ssm.promotion.core.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 13 on 2018/1/17.
 */
@RunWith(SpringJUnit4ClassRunner.class) //指定测试用例的运行器 这里是指定了Junit4
@ContextConfiguration("classpath:spring-context.xml")
public class ArticleDaoTest {
    @Autowired
    private ArticleDao articleDao;

    @Test
    public void insertArticleTest() throws Exception {
        Article article = new Article();
        article.setArticleContent("article");
        article.setAddName("13");
        article.setArticleClassID(1);
        article.setArticleTitle("title");
        article.setArticleCreateDate(DateUtil.getCurrentDateStr());
        Assert.assertTrue(article.getId()==null);
        System.out.println("insert前article的id:"+article.getId());
        articleDao.insertArticle(article);
        Assert.assertTrue(article.getId()!=null);
        System.out.println("insert后article的id:"+article.getId());
    }

}
