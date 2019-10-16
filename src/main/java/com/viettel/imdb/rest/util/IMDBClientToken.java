package com.viettel.imdb.rest.util;

import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * @author quannh22
 * @since 03/10/2019
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="AddClusterNodeRequest", description = "Request to add a node to the current managed cluster")
public class IMDBClientToken {
    private String token;
    private String username;
    private byte[] password;
}
