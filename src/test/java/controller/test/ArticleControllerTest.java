package controller.test;

import com.alibaba.fastjson.JSONObject;
import com.ssm.promotion.core.entity.Article;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Springmvc 单元测试类
 *
 * @author 13 2016-07-06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:/spring-context.xml", "classpath*:/spring-context-mvc.xml", "classpath*:/mybatis-config.xml"})
public class ArticleControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testArticleList() throws Exception {
        //创建文章列表的请求
        //请求方式为get
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.request(HttpMethod.GET, "/articles");
        //此请求并不需要添加请求参数
        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void testArticleAdd() throws Exception {
        //创建文章对象
        Article article = new Article();
        article.setAddName("13");
        article.setArticleTitle("title");
        article.setArticleContent("content");
        String requestParam = JSONObject.toJSONString(article);
        //请求方式为post
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/articles");
        mockMvc.perform(mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON).content(requestParam)).andExpect(status().isOk())
                .andDo(print());

    }
}  