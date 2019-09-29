package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
public class ProductAttrValueVO extends ProductAttrValueEntity {
   //重写setAttrValue,接受valueSelected数据
    private List<Object> valueSelected;
    public void ValueSelected(List<Object> valueSelected) {
        if (CollectionUtils.isEmpty(valueSelected)){
            return;
    }
        this.setAttrValue(StringUtils.join(valueSelected,","));
    }

}
