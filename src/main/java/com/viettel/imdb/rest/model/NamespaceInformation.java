package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.openhft.chronicle.core.annotation.NotNull;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="NamespaceInformation", description = "Namespace Information")
public class NamespaceInformation {
    @ApiModelProperty(value= "name", example = "qwer")
    private String name;
    @ApiModelProperty(value= "Table Information", example = "abc")
    private List<TableInformation> tables = new ArrayList<>();


    public void addTableInfo(String name, int recordCount) {
        tables.add(new TableInformation(name, 4096000L, recordCount, 2, 2048L));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value="TableInformation", description = "Table Information")
    public class TableInformation{
        @ApiModelProperty(value= "name", example = "qwer")
        private String name;
        @ApiModelProperty(value= "Table Size", example = "3834940")
        private long size;
        @ApiModelProperty(value= "record Count", example = "120")
        private int recordCount;
        @ApiModelProperty(value= "Index Count", example = "2")
        private int indexCount;
        @ApiModelProperty(value= "Index Size", example = "2048")
        private long indexSize;
    }
}
