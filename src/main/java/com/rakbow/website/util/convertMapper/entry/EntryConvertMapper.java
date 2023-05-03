package com.rakbow.website.util.convertMapper.entry;

import com.alibaba.fastjson2.JSON;
import com.rakbow.website.data.emun.common.Region;
import com.rakbow.website.data.emun.entry.EntryCategory;
import com.rakbow.website.data.vo.entry.EntryVOAlpha;
import com.rakbow.website.entity.Entry;
import com.rakbow.website.entity.common.Company;
import com.rakbow.website.entity.common.Merchandise;
import com.rakbow.website.entity.common.Personnel;
import com.rakbow.website.entity.common.Role;
import com.rakbow.website.util.common.DateUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-02 1:12
 * @Description:
 */
@Mapper(componentModel = "spring")
public interface EntryConvertMapper {

    EntryConvertMapper INSTANCES = Mappers.getMapper(EntryConvertMapper.class);

    default EntryVOAlpha toEntryVOAlpha(Entry entry) {
        if(entry == null) {
            return null;
        }
        EntryVOAlpha VO = new EntryVOAlpha();

        VO.setId(entry.getId());
        VO.setName(entry.getName());
        VO.setNameZh(entry.getNameZh());
        VO.setNameEn(entry.getNameEn());
        VO.setCategory(EntryCategory.getAttribute(entry.getCategory()));
        VO.setAlias(JSON.parseArray(entry.getAlias()).toJavaList(String.class));
        VO.setLinks(JSON.parseArray(entry.getLinks()).toJavaList(String.class));
        VO.setAddedTime(DateUtil.timestampToString(entry.getAddedTime()));
        VO.setEditedTime(DateUtil.timestampToString(entry.getEditedTime()));
        VO.setRemark(entry.getRemark());
        return VO;
    }

    default List<EntryVOAlpha> toEntryVOAlpha(List<Entry> entries) {
        List<EntryVOAlpha> entryVOAlphas = new ArrayList<>();
        if(!entries.isEmpty()) {
            entries.forEach(entry -> {
                entryVOAlphas.add(toEntryVOAlpha(entry));
            });
        }
        return entryVOAlphas;
    }

    default Company toCompany(Entry entry) {
        if(entry == null) {
            return null;
        }
        Company company = new Company();

        company.setId(entry.getId());
        company.setName(entry.getName());
        company.setNameZh(entry.getNameZh());
        company.setNameEn(entry.getNameEn());
        company.setLinks(JSON.parseArray(entry.getLinks()).toJavaList(String.class));
        company.setAlias(JSON.parseArray(entry.getLinks()).toJavaList(String.class));
        company.setDescription(entry.getDescription());
        company.setAddedTime(DateUtil.timestampToString(entry.getAddedTime()));
        company.setEditedTime(DateUtil.timestampToString(entry.getEditedTime()));
        company.setRemark(entry.getRemark());
        company.setRegion(Region.getRegion(JSON.parseObject(entry.getDetail()).getString("region")));
        return company;
    }

    default List<Company> toCompany(List<Entry> entries) {
        List<Company> companies = new ArrayList<>();
        if(!entries.isEmpty()) {
            entries.forEach(entry -> {
                companies.add(toCompany(entry));
            });
        }
        return companies;
    }

    default Personnel toPersonnel(Entry entry) {
        if(entry == null) {
            return null;
        }
        Personnel personnel = new Personnel();

        personnel.setId(entry.getId());
        personnel.setName(entry.getName());
        personnel.setNameZh(entry.getNameZh());
        personnel.setNameEn(entry.getNameEn());
        personnel.setLinks(JSON.parseArray(entry.getLinks()).toJavaList(String.class));
        personnel.setDescription(entry.getDescription());
        personnel.setAddedTime(DateUtil.timestampToString(entry.getAddedTime()));
        personnel.setEditedTime(DateUtil.timestampToString(entry.getEditedTime()));
        personnel.setRemark(entry.getRemark());
        return personnel;
    }

    default List<Personnel> toPersonnel(List<Entry> entries) {
        List<Personnel> personnel = new ArrayList<>();
        if(!entries.isEmpty()) {
            entries.forEach(entry -> {
                personnel.add(toPersonnel(entry));
            });
        }
        return personnel;
    }

    default Role toRole(Entry entry) {
        if(entry == null) {
            return null;
        }
        Role role = new Role();

        role.setId(entry.getId());
        role.setName(entry.getName());
        role.setNameZh(entry.getNameZh());
        role.setNameEn(entry.getNameEn());
        role.setLinks(JSON.parseArray(entry.getLinks()).toJavaList(String.class));
        role.setDescription(entry.getDescription());
        role.setAddedTime(DateUtil.timestampToString(entry.getAddedTime()));
        role.setEditedTime(DateUtil.timestampToString(entry.getEditedTime()));
        role.setRemark(entry.getRemark());
        return role;
    }

    default List<Role> toRole(List<Entry> entries) {
        List<Role> roles = new ArrayList<>();
        if(!entries.isEmpty()) {
            entries.forEach(entry -> {
                roles.add(toRole(entry));
            });
        }
        return roles;
    }

    default Merchandise toMerchandise(Entry entry) {
        if(entry == null) {
            return null;
        }
        Merchandise merchandise = new Merchandise();

        merchandise.setId(entry.getId());
        merchandise.setName(entry.getName());
        merchandise.setNameZh(entry.getNameZh());
        merchandise.setNameEn(entry.getNameEn());
        merchandise.setLinks(JSON.parseArray(entry.getLinks()).toJavaList(String.class));
        merchandise.setDescription(entry.getDescription());
        merchandise.setAddedTime(DateUtil.timestampToString(entry.getAddedTime()));
        merchandise.setEditedTime(DateUtil.timestampToString(entry.getEditedTime()));
        merchandise.setRemark(entry.getRemark());
        return merchandise;
    }

    default List<Merchandise> toMerchandise(List<Entry> entries) {
        List<Merchandise> merchandises = new ArrayList<>();
        if(!entries.isEmpty()) {
            entries.forEach(entry -> {
                merchandises.add(toMerchandise(entry));
            });
        }
        return merchandises;
    }

}
