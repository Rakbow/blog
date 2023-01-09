package com.rakbow.website.controller;

import com.rakbow.website.util.system.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequestMapping("/redis")
@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {

    private final RedisUtil redisUtil;
    private final String REDIS_HASH_KEY = "HASH:";
    private final String REDIS_SINGLE_KEY = "SINGLE:";

    //设置全部 key: redis-key, pData: 类型配置
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/hash/{key}")
    @ResponseBody
    public String hashMapSet(
            @PathVariable(name = "key") String pKey,
            @RequestBody Map<String,Object> pData){
//        redisUtil.hmset(REDIS_HASH_KEY +pKey,pData);
        redisUtil.hashMapSet(pKey,pData);
        return "设置成功";
    }

    //设置 key: 类型Id, item: , pData: 类型配置
    @Transactional(rollbackFor = Exception.class)
    @PutMapping("/hash/{key}")
    @ResponseBody
    public String hashSet(@PathVariable(name = "key") String pKey,
                          @RequestParam(name = "item") String pItem,
                          @RequestBody Object pData){
//        redisUtil.hset(REDIS_HASH_KEY +pKey,pItem,pData);
        redisUtil.hashSet(pKey,pItem,pData);
        redisUtil.hashSet(pKey,pItem,pData);
        return "设置成功";
    }

    //设置 key: 类型Id
    @GetMapping("/hash/{key}")
    @ResponseBody
    public Map hashMapGet(@PathVariable(name = "key") String pKey){
//        Map rtn = redisUtil.hmget(REDIS_HASH_KEY +pKey);
        Map rtn = redisUtil.hashMapGet(pKey);
        return rtn;
    }

    //hash-获取单个值 key: 类型Id, item:
    @PutMapping ("/hash/{key}/{item}")
    @ResponseBody
    public Object hashGet(@PathVariable(name = "key") String pKey,
                          @RequestParam(name = "item") String pItem){
//        Object data = redisUtil.hget(REDIS_HASH_KEY +pKey,pItem);
        Object data = redisUtil.hashGet(pKey,pItem);
        return data;
    }

    //hash-删除键 key: 类型Id, item:
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping ("/hash/{key}/{item}")
    @ResponseBody
    public String hashDelete(@PathVariable(name = "key") String pKey,
                             @RequestParam(name = "item") String pItem){
        redisUtil.hdel(REDIS_HASH_KEY +pKey,pItem);
        return "删除成功";
    }

    //普通-设置值 key: redis-key, pData: 类型配置
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/single/{key}")
    @ResponseBody
    public String set(
            @PathVariable(name = "key") String pKey,
            @RequestBody Object pData){
//        redisUtil.set(REDIS_SINGLE_KEY +pKey,pData);
        redisUtil.set(pKey,pData);
        return "设置成功";
    }

    //普通-获取值 key: 类型Id
    @GetMapping("/single/{key}")
    @ResponseBody
    public Object get(@PathVariable(name = "key") String pKey){
//        Object rtn = redisUtil.get(REDIS_SINGLE_KEY +pKey);
        Object rtn = redisUtil.get(pKey);
        return rtn;
    }

    //普通-删除键 key: 类型Id
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping ("/single/{key}/{item}")
    @ResponseBody
    public String delete(@PathVariable(name = "key") String pKey){
        redisUtil.delete(REDIS_SINGLE_KEY +pKey);
        return "删除成功";
    }

}
