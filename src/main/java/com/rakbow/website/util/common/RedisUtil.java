package com.rakbow.website.util.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-09 13:13
 * @Description: Redis工具类
 */
@Component
public class RedisUtil {
    private final RedisTemplate<String, Object> _redisTemplate;
    private final HashOperations<String, String, Object> _hashOperations;
    private final HashMapper<Object, String, Object> _hashMapper;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> v_redisTemplate) {
        _redisTemplate = v_redisTemplate;
        _hashOperations = _redisTemplate.opsForHash();
        _hashMapper = new Jackson2HashMapper(true);
    }


    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                _redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return _redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        if(!StringUtils.hasLength(key)){
            return false;
        }
        return _redisTemplate.hasKey( key);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                _redisTemplate.delete(key[0]);
            } else {
                _redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }
    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : _redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public <T>  T get(String key,  Class<T> pValClass) throws Exception {
        Object data = _redisTemplate.opsForValue().get(key);
        if(null == data){
            return null;
        }
        if(data.getClass().equals(pValClass)){
            return (T)data;
        }
        else if(data instanceof JSONObject){
            T value =  JSON.to(pValClass,data);
            return value;
        }else if(data instanceof String){
            if(pValClass == String.class){
                return (T)data.toString();
            }
            T value =  JSON.parseObject((String)data,pValClass);
            return value;
        }
        else {
            throw new Exception("redis 数据错误，无法完成转换");
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            _redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Description: 如果key存在, 则返回false, 如果不存在,
     * 则将key=value放入redis中, 并返回true
     * @author: bixuejun(bxjgood@163.com)
     * @date:  2021/11/26 14:36
     * @param
     * @return
     */
    public boolean setIfAbsent(String key, String value) {
        return _redisTemplate.opsForValue().setIfAbsent( key, value);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                _redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long increment(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return _redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decrement(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return _redisTemplate.opsForValue().decrement(key, delta);
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hashGet(String key, String item) {
        return _redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hashMapGet(String key) {
        return _redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public <T> Map<String, T> hashMapGet(String key, Class<T> pValClass) {
        Map<Object, Object> map = _redisTemplate.opsForHash().entries(key);
        Map<String, T> result = new HashMap<>();
        for(Map.Entry<Object, Object> data : map.entrySet()){
            String subKey = data.getKey().toString();
            T value =  JSON.to(pValClass,data.getValue());
            result.put(subKey,value);
        }
        return result;
    }


    /**
     * 使用hget获取Redis对象
     *
     * @param key 键
     * @return 返回对象
     */
    public <T> T hmgetObj(String key) {
        Map<String, Object> mpData = _hashOperations.entries(key);
        if(mpData.size() == 0){
            return null;
        }
        return (T) _hashMapper.fromHash(mpData);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hashMapSet(String key, Map<String, Object> map) {
        try {
            _redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 使用hset对指定键放入对象的所有值
     *
     * @param key     键
     * @param v_obj   对象
     * @param <T>对象类型
     * @return 是否成功
     */
    public <T> boolean hmsetObject(String key, T v_obj) {
        try {
            Map<String, Object> mappedHash = _hashMapper.toHash(v_obj);
            _hashOperations.putAll(key, mappedHash);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hashMapSet(String key, Map<String, Object> map, long time) {
        try {
            _redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 使用hset对指定键放入对象的所有值，并设置过期时间
     *
     * @param key     键
     * @param v_obj   对象
     * @param <T>对象类型
     * @return 是否成功
     */
    public <T> boolean hmsetObject(String key, T v_obj, long time) {
        try {
            Map<String, Object> mappedHash = _hashMapper.toHash(v_obj);
            _hashOperations.putAll(key, mappedHash);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hashSet(String key, String item, Object value) {
        try {
            _redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hashSet(String key, String item, Object value, long time) {
        try {
            _redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        _redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return _redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hashIncrease(String key, String item, double by) {
        return _redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hashDecrease(String key, String item, double by) {
        return _redisTemplate.opsForHash().increment(key, item, -by);
    }
    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return _redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return _redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return _redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = _redisTemplate.opsForSet().add(key, values);
            if (time > 0){
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return _redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = _redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return _redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long getListSize(String key) {
        try {
            return _redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object listGet(String key, long index) {
        try {
            return _redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean listSet(String key, long index, Object value) {
        try {
            _redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 把值放入队尾
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean rPush(String key, Object value) {
        try {
            _redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 把值放入队首
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lPush(String key, Object value) {
        try {
            _redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量插入队尾
     *
     * @param key
     * @param value
     * @return
     */
    public boolean rPushAll(String key, List<Object> value){
        try {
            _redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量插入队首
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lPushAll(String key, List<Object> value){
        try {
            _redisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从队首推出一个元素
     *
     * @param key 键
     * @return 队首元素
     */
    public Object lPop(String key){
        try {
            Object obj = _redisTemplate.opsForList().leftPop(key);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从队尾推出一个元素
     *
     * @param key 键
     * @return 队首元素
     */
    public Object rPop(String key){
        try {
            Object obj = _redisTemplate.opsForList().rightPop(key);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*********************************pipeline*****************/

    public <T> List<Object> executePipelined(RedisCallback<T> pFnCallBack){
        return _redisTemplate.executePipelined(pFnCallBack,_redisTemplate.getValueSerializer());
    }

    public List<String> keys(String pPattern){
        return _redisTemplate.keys(pPattern).stream().collect(Collectors.toList());
    }

    public void batchRemove(List<String> pKeyList){
        new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (String key : pKeyList) {
                    connection.expire(key.getBytes(),1);
                }
                return 0;
            }
        };
    }
}

