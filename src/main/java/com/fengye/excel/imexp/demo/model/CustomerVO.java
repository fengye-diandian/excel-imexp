package com.fengye.excel.imexp.demo.model;



import com.fengye.excel.imexp.annotation.ExcelModelProperty;
import com.fengye.excel.imexp.annotation.ExcelModelTitle;
import com.fengye.excel.imexp.datatype.DataTypeEnum;
import com.fengye.excel.imexp.model.ImportModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ExcelModelTitle(name="客户消费数据", fileName="客户消费数据")
public class CustomerVO extends ImportModel implements Serializable {

	private static final long serialVersionUID = -3434719712955859295L;

	private Long id;
	
	@ExcelModelProperty(name = "商铺名称", colIndex = 0, comment = "这是一个很长很长的的单元格注释", required = true, selectbox=true)
	public String shopName;// 店铺名称
	
	@ExcelModelProperty(name = "客户昵称", colIndex = 1)
	public String nikeName;// 客户昵称
	
	@ExcelModelProperty(name = "客户姓名", colIndex = 2, required = true)
	public String realName;// 客户姓名
	
	@ExcelModelProperty(name = "手机号码", colIndex = 3, required = true)
	public String mobile;//客户手机号码
	
	@ExcelModelProperty(name = "商品编码", colIndex = 4, comment = "商品编码很长很长对对对这是一个个\n很长很长的的单元格注释", required = true)
	public String goodsCode;//商品编码
	
	@ExcelModelProperty(name = "商品名称", colIndex = 5)
	public String goodsName;//商品名称
	
	@ExcelModelProperty(name = "数量", colIndex = 6, required = true, dataType = DataTypeEnum.INTEGER)
	public Integer number;//购买数量
	
	@ExcelModelProperty(name = "商品单价", colIndex = 7, required = true, dataType = DataTypeEnum.NUMBER)
	public BigDecimal price;//单价
	
	@ExcelModelProperty(name = "总价", colIndex = 8, required = true, dataType = DataTypeEnum.NUMBER)
	public BigDecimal totalPrice;// 总价
	
	@ExcelModelProperty(name = "消费日期", colIndex = 9, dataType = DataTypeEnum.DATE)
	public Date customeDate; // 年月日    yyyy-MM-dd



	@Override
	public String toString() {
		return "CustomerDTO [id=" + id + ", shopName=" + shopName + ", nikeName=" + nikeName + ", realName=" + realName
				+ ", mobile=" + mobile + ", goodsCode=" + goodsCode + ", goodsName=" + goodsName + ", number=" + number
				+ ", price=" + price + ", totalPrice=" + totalPrice + "]";
	}
	
	
}
