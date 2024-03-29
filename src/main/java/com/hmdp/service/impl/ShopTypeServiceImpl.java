package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryTypeList() {

        //TODO 用Redis中list方法实现
        String shopTypeListJson = stringRedisTemplate.opsForValue().get("shopTypeListJson");
        if (StrUtil.isNotBlank(shopTypeListJson)) {
            List<ShopType>  shopTypeList = JSONUtil.parseArray(shopTypeListJson).toList( ShopType.class);
            return Result.ok(shopTypeList);
        }
        List<ShopType> typeList =query().orderByAsc("sort").list();
        if (typeList==null) {
            return Result.fail("店铺类型不存在");
        }
        stringRedisTemplate.opsForValue().set("shopTypeListJson", JSONUtil.toJsonStr(typeList));
        return Result.ok(typeList);
    }
}
