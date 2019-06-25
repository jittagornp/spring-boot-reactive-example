/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.collection;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Data
@Document(collection = LoginHistory.COLLECTION_NAME)
public class LoginHistory implements Serializable {

    public static final String COLLECTION_NAME = "login_history";

    @Id
    private String id;

    private LocalDateTime loginDate;

    private LocalDateTime logoutDate;

    private String sessionId;

    private String agentId;

    private String userId;

    private String ipAddress;

}
