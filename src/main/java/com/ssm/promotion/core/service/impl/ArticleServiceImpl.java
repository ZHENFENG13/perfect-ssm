package com.ssm.promotion.core.service.impl;

import com.ssm.promotion.core.common.Constants;
import com.ssm.promotion.core.dao.ArticleDao;
import com.ssm.promotion.core.entity.Article;
import com.ssm.promotion.core.redis.RedisUtil;
import com.ssm.promotion.core.service.ArticleService;
import com.ssm.promotion.core.util.AntiXssUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    private static final Logger log = Logger.getLogger(ArticleService.class);

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
        article.setArticleTitle(AntiXssUtil.replaceHtmlCode(article.getArticleTitle()));
        if (articleDao.insertArticle(article) > 0) {
            log.info("insert article success,save article to redis");
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
        article.setArticleTitle(AntiXssUtil.replaceHtmlCode(article.getArticleTitle()));
        if (articleDao.updArticle(article) > 0) {
            log.info("update article success,delete article in redis and save again");
            redisUtil.del(Constants.ARTICLE_CACHE_KEY + article.getId());
            redisUtil.put(Constants.ARTICLE_CACHE_KEY + article.getId(), article);
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteArticle(String id) {
        redisUtil.del(Constants.ARTICLE_CACHE_KEY + id);
        return articleDao.delArticle(id);
    }

    @Override
    public Article findById(String id) {
        log.info("get article by id:" + id);
        Article article = (Article) redisUtil.get(Constants.ARTICLE_CACHE_KEY + id, Article.class);
        if (article != null) {
            log.info("article in redis");
            return article;
        }
        Article articleFromMysql = articleDao.getArticleById(id);
        if (articleFromMysql != null) {
            log.info("get article from mysql and save article to redis");
            redisUtil.put(Constants.ARTICLE_CACHE_KEY + articleFromMysql.getId(), articleFromMysql);
            return articleFromMysql;
        }
        return null;
    }

}
