package com.ssm.promotion.core.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ssm.promotion.core.common.Constants;
import com.ssm.promotion.core.dao.ArticleDao;
import com.ssm.promotion.core.entity.Article;
import com.ssm.promotion.core.redis.RedisUtil;
import com.ssm.promotion.core.service.ArticleService;
import org.springframework.stereotype.Service;


@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleDao articleDao;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<Article> findArticle(Map<String, Object> map) {
        return articleDao.findArticles(map);
    }

    @Override
    public Long getTotalArticle(Map<String, Object> map) {
        return articleDao.getTotalArticles(map);
    }

    @Override
    public int addArticle(Article article) {
        if (article.getArticleTitle() == null || article.getArticleContent() == null || getTotalArticle(null) > 90 || article.getArticleContent().length() > 50000) {
            return 0;
        }
        if (articleDao.insertArticle(article) > 0) {
            //向mysql数据库中插入成功后，存入redis中
            redisUtil.put(Constants.ARTICLE_CACHE_KEY + article.getId(), article);
            return 1;
        }
        return 0;
    }

    @Override
    public int updateArticle(Article article) {
        if (article.getArticleTitle() == null || article.getArticleContent() == null || getTotalArticle(null) > 90 || article.getArticleContent().length() > 50000) {
            return 0;
        }
        if (articleDao.updArticle(article) > 0) {
            //向mysql数据库中修改成功后，修改redis中的数据
            redisUtil.del(Constants.ARTICLE_CACHE_KEY + article.getId());
            redisUtil.put(Constants.ARTICLE_CACHE_KEY + article.getId(), article);
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteArticle(String id) {
        return articleDao.delArticle(id);
    }

    @Override
    public Article findById(String id) {
        //首先通过redis查询
        //此种写法存在缓存击穿问题
        Article article = (Article) redisUtil.get(Constants.ARTICLE_CACHE_KEY + id, Article.class);
        //如果缓存中已经存在则直接返回，不再通过mysql数据库
        if (article != null) {
            return article;
        }
        //如果缓存中不存在或者已过期，则依然通过mysql数据库查询
        Article articleFromMysql = articleDao.getArticleById(id);
        if (articleFromMysql != null) {
            //查询到数据后，存入redis缓存中
            redisUtil.put(Constants.ARTICLE_CACHE_KEY + articleFromMysql.getId(), articleFromMysql);
            return articleFromMysql;
        }
        return null;
    }

}
