package com.rakbow.website.data.entity.franchise;

import com.rakbow.website.entity.Franchise;
import lombok.Data;

import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-08 18:04
 * @Description: 系列的元系列相关信息
 */
public class MetaInfo {
    public int isMeta;//是否为元系列
    public List<Integer> childFranchises;//子系列
}
