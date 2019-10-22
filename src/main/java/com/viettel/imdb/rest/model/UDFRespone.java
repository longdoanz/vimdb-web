package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

//@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="UDFRespone", description = "  ")
public class UDFRespone {
    @ApiModelProperty(value= "UDFs", example = "    ")
    private List<UDFInfo> UDFs;

    public UDFRespone(List<UDFInfo> udfList) {
        this.UDFs = udfList;
    }
    public UDFRespone() {
    }


}
