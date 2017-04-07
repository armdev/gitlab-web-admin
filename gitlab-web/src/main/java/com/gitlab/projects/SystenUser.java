package com.gitlab.projects;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

/**
 *
 * @author armdev
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SystenUser implements Serializable {    

    private static final long serialVersionUID = -3192576805761029194L;
    
    @Setter
    @Getter
    private ObjectId _id;

    @Setter
    @Getter
    private String id;
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String url;
    @Setter
    @Getter
    private String privateToken;
    @Setter
    @Getter
    private Date registeredDate;

}
