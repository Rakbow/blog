package com.rakbow.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.rakbow.blog.dao.SeriesMapper;
import com.rakbow.blog.entity.Series;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-20 1:17
 * @Description:
 */
@Service
public class SeriesService {

    @Autowired
    private SeriesMapper seriesMapper;

    //新增系列
    public int insertSeries(Series series){
        return seriesMapper.insertSeries(series);
    };

    //通过id查找系列
    public Series selectSeriesById(int id){
        return seriesMapper.selectSeriesById(id);
    };

    //修改系列信息
    public int updateSeries(int id, Series series){
        return seriesMapper.updateSeries(id, series);
    };

    //删除系列
    public int deleteSeriesById(int id){
        return seriesMapper.deleteSeriesById(id);
    };

    //获取所有系列数组选项
    public List<JSONObject> getAllSeriesSet(){
        List<JSONObject> list = new ArrayList<>();
        List<Series> list_temp = seriesMapper.getAll();
        for (Series series : list_temp) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", series.getNameZh());
            jsonObject.put("value", series.getId());
            list.add(jsonObject);
        }
        return list;
    }

}
