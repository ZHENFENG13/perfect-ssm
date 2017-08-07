package com.ssm.promotion.core.admin;

import com.ssm.promotion.core.common.Result;
import com.ssm.promotion.core.common.ResultGenerator;
import com.ssm.promotion.core.entity.PageBean;
import com.ssm.promotion.core.entity.Picture;
import com.ssm.promotion.core.service.PictureService;
import com.ssm.promotion.core.util.DateUtil;
import com.ssm.promotion.core.util.ResponseUtil;
import org.apache.log4j.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pictures")
public class PictureController {
    @Resource
    private PictureService pictureService;
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(PictureController.class);// 日志文件

    /**
     * 查找相应的数据集合
     *
     * @param page
     * @param rows
     * @param picture
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/datagrid")
    public String list(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "rows", required = false) String rows,
            Picture picture, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (page != null && rows != null) {
            PageBean pageBean = new PageBean(Integer.parseInt(page),
                    Integer.parseInt(rows));
            map.put("start", pageBean.getStart());
            map.put("size", pageBean.getPageSize());
        }
        if (picture != null) {
            map.put("id", picture.getId() + "");
            map.put("type", picture.getType() + "");
            map.put("grade", picture.getGrade() + "");
        }
        List<Picture> pictureList = pictureService.findPicture(map);
        Long total = pictureService.getTotalPicture(map);
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(pictureList);
        result.put("rows", jsonArray);
        result.put("total", total);
        log.info("request: pictures/datagrid , map: " + map.toString());
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 查找相应的数据集合
     *
     * @param page
     * @param rows
     * @param picture
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Result list(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "rows", required = false) String rows,
            Picture picture) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (page != null && rows != null) {
            PageBean pageBean = new PageBean(Integer.parseInt(page),
                    Integer.parseInt(rows));
            map.put("start", pageBean.getStart());
            map.put("size", pageBean.getPageSize());
        }
        if (picture != null) {
            map.put("id", picture.getId() + "");
            map.put("type", picture.getType() + "");
            map.put("grade", picture.getGrade() + "");
        }
        List<Picture> pictureList = pictureService.findPicture(map);
        Result result = ResultGenerator.genSuccessResult();
        Long total = pictureService.getTotalPicture(map);
        Map data = new HashMap();
        data.put("rows", pictureList);
        data.put("total", total);
        log.info("request: picture/list , map: " + map.toString());
        result.setData(data);
        return result;
    }


    /**
     * 添加
     *
     * @param picture
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody Picture picture)
            throws Exception {
        int resultTotal = 0;

        picture.setTime(DateUtil.getCurrentDateStr());

        resultTotal = pictureService.addPicture(picture);

        log.info("request: picture/save , " + picture.toString());

        if (resultTotal > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("添加失败");
        }
    }

    /**
     * 修改
     *
     * @param picture
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public Result update(@RequestBody Picture picture)
            throws Exception {
        int resultTotal = 0;

        picture.setTime(DateUtil.getCurrentDateStr());

        resultTotal = pictureService.updatePicture(picture);

        log.info("request: picture/update , " + picture.toString());

        if (resultTotal > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }


    /**
     * 删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result delete(@PathVariable(value = "ids") String ids
    ) throws Exception {
        if (ids.length() > 20) {
            return ResultGenerator.genFailResult("ERROR");
        }
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            pictureService.deletePicture(idsStr[i]);
        }
        log.info("request: picture/delete , ids: " + ids);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 根据id查找
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result findById(@PathVariable("id") String id) throws Exception {
        Picture picture = pictureService.findById(id);
        log.info("request: picture/findById");
        if (picture != null) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(picture);
            return result;
        } else {
            return ResultGenerator.genFailResult("无资源");
        }
    }
}