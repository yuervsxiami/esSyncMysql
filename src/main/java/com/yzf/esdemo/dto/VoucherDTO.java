package com.yzf.esdemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2020/07/16
 * Time: 5:17 下午
 */
@ApiModel("凭证数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "voucher", type = "voucher")
public class VoucherDTO {

        @ApiModelProperty("凭证id")
        private Long id;
        @ApiModelProperty("账套代码")
        private Long ztdm;
        @ApiModelProperty("制证日期")
        private Date zzrq;
        @ApiModelProperty("会计年度")
        private Integer kjnd;
        @ApiModelProperty("会计期间")
        private Integer kjqj;
        @ApiModelProperty("流水号")
        private Integer lsh;
        @ApiModelProperty("凭证来源")
        @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word",type= FieldType.Text)
        private String ly;
        @ApiModelProperty("凭证字")
        private Integer pzz;
        @ApiModelProperty("状态")
        private Integer zt;
        @ApiModelProperty("制证人")
        private String zzr;
        @ApiModelProperty("记录日期")
        private Date jlrq;
        @ApiModelProperty("总金额")
        private BigDecimal zje;
        @ApiModelProperty("审核人")
        private String fhr;
        @ApiModelProperty("审核日期")
        private Date fhrq;
        @ApiModelProperty("附件数")
        private Integer fjs;
        @ApiModelProperty("开票日期")
        private Date kprq;
        @ApiModelProperty("修改人")
        private String xgr;
        @ApiModelProperty("修改日期")
        private Date xgsj;
        @ApiModelProperty("版本号")
        private Long bbh;

}
