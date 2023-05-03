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
import com.rakbow.website.entity.Entry;
import com.rakbow.website.entity.common.Company;
import com.rakbow.website.entity.common.Merchandise;
import com.rakbow.website.entity.common.Personnel;
import com.rakbow.website.entity.common.Role;
import com.rakbow.website.util.common.CommonUtil;
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

        JSONArray orgCompanies = JSON.parseArray(json);
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

    public List<Company> getAllCompanies() {
        List<Entry> entries = entryMapper.getEntryByCategory(EntryCategory.COMPANY.getId());
        return convertMapper.toCompany(entries);
    }

    public List<Personnel> getAllPersonnel() {
        List<Entry> entries = entryMapper.getEntryByCategory(EntryCategory.PERSONNEL.getId());
        return convertMapper.toPersonnel(entries);
    }

    public List<Merchandise> getAllMerchandises() {
        List<Entry> entries = entryMapper.getEntryByCategory(EntryCategory.MERCHANDISE.getId());
        return convertMapper.toMerchandise(entries);
    }

    public List<Role> getAllRoles() {
        List<Entry> entries = entryMapper.getEntryByCategory(EntryCategory.ROLE.getId());
        return convertMapper.toRole(entries);
    }

}
