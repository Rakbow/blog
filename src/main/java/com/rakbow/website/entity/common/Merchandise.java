package com.rakbow.website.entity.common;

import com.rakbow.website.data.emun.entry.EntryCategory;
import com.rakbow.website.util.common.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-02 16:36
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Merchandise extends MetaEntry {

    public Merchandise() {
        this.setId(0);
        this.setCategory(EntryCategory.getAttribute(EntryCategory.MERCHANDISE.getId()));
        this.setName("");
        this.setNameEn("");
        this.setNameZh("");
        this.setAlias(new ArrayList<>());
        this.setLinks(new ArrayList<>());
        this.setDescription("");
        this.setRemark("");
        this.setAddedTime(DateUtil.getCurrentTime());
        this.setEditedTime(DateUtil.getCurrentTime());
    }

}
