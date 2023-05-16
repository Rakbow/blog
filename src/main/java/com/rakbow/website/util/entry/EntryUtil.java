package com.rakbow.website.util.entry;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.EntryMapper;
import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.data.emun.common.CompanyRole;
import com.rakbow.website.data.emun.entry.EntryCategory;
import com.rakbow.website.data.emun.system.SystemLanguage;
import com.rakbow.website.util.common.DataFinder;
import com.rakbow.website.util.common.RedisUtil;
import com.rakbow.website.util.common.SpringUtil;
import com.rakbow.website.util.convertMapper.entry.EntryConvertMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-02 1:04
 * @Description:
 */
@Component
public class EntryUtil {

    @Resource
    private EntryMapper entryMapper;

    private final EntryConvertMapper convertMapper = EntryConvertMapper.INSTANCES;

    public static JSONArray getCompanies(String json) {
        JSONArray orgCompanies = JSON.parseArray(json);
        if(orgCompanies.isEmpty()) return new JSONArray();

        String lang = LocaleContextHolder.getLocale().getLanguage();
        RedisUtil redisUtil = SpringUtil.getBean("redisUtil");
        String key;
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            key = RedisCacheConstant.COMPANY_SET_EN;
        }else {
            key = RedisCacheConstant.COMPANY_SET_ZH;
        }
        List<Attribute> allCompanies = JSON.parseArray(JSON.toJSONString(redisUtil.get(key))).toJavaList(Attribute.class);

        JSONArray companies = new JSONArray();
        for (int i = 0; i < orgCompanies.size(); i++) {
            JSONObject orgCompany = orgCompanies.getJSONObject(i);
            JSONObject company = new JSONObject();

            company.put("role", CompanyRole.getAttribute(orgCompany.getIntValue("role")));

            List<Attribute> members = new ArrayList<>();
            List<Integer> memberIds = orgCompany.getList("members", Integer.class);
            memberIds.forEach(id -> {
                Attribute attribute = DataFinder.findAttributeByValue(id, allCompanies);
                if (attribute != null) {
                    members.add(attribute);
                }
            });

            company.put("members", members);
            companies.add(company);
        }

        return companies;
    }

    public static JSONArray getPersonnel(String json) {
        JSONArray orgPersonnel = JSON.parseArray(json);
        if(orgPersonnel.isEmpty()) return new JSONArray();
        List<Attribute> allPersonnel = getAttributesForRedis(EntryCategory.PERSONNEL.getId());
        List<Attribute> allRole = getAttributesForRedis(EntryCategory.ROLE.getId());

        JSONArray personnel = new JSONArray();
        for (int i = 0; i < orgPersonnel.size(); i++) {
            JSONObject orgItem = orgPersonnel.getJSONObject(i);
            JSONObject newItem = new JSONObject();

            if(orgItem.getIntValue("main") == 1) {
                newItem.put("main", 1);
            }

            newItem.put("role", DataFinder.findAttributeByValue(orgItem.getIntValue("role"), allRole));

            List<Attribute> members = new ArrayList<>();
            List<Integer> memberIds = orgItem.getList("members", Integer.class);
            memberIds.forEach(id -> {
                Attribute attribute = DataFinder.findAttributeByValue(id, allPersonnel);
                if (attribute != null) {
                    members.add(attribute);
                }
            });

            newItem.put("members", members);
            personnel.add(newItem);
        }

        return personnel;
    }

    public static JSONArray getSpecs(String json) {
        JSONArray specs = JSON.parseArray(json);
        if(specs.isEmpty()) return new JSONArray();
        List<Attribute> allSpecParameter = getAttributesForRedis(EntryCategory.SPEC_PARAMETER.getId());
        for (int i = 0; i < specs.size(); i++) {
            JSONObject item = specs.getJSONObject(i);

            item.put("label", DataFinder.findAttributeByValue(item.getIntValue("label"), allSpecParameter));
        }

        return specs;
    }

    public static Attribute getSerial(int serial) {
        List<Attribute> allPublications = getAttributesForRedis(EntryCategory.PUBLICATION.getId());
        return DataFinder.findAttributeByValue(serial, allPublications);
    }

    public static List<Attribute> getAttributesForRedis(int category) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        RedisUtil redisUtil = SpringUtil.getBean("redisUtil");

        JSONArray attributes = new JSONArray();
        String key;
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            key = EntryCategory.categoryMapEn.get(category);
        }else {
            key = EntryCategory.categoryMapZH.get(category);
        }

        attributes.addAll(JSON.parseArray(JSON.toJSONString(redisUtil.get(key))));

        return attributes.toJavaList(Attribute.class);
    }

}
