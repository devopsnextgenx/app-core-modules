package io.devopsnextgenx.microservices.modules.access.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import lombok.Data;

import java.io.IOException;
import java.util.Base64;
import java.util.Set;

@Data
public class AccessData {
    private String userId;
    private Set<ROLE> roleList;
    private String organizationId;
    private String companyId;
    private boolean appAuth;

    public AccessData(String userId, Set<ROLE> roleList, String organizationId, String companyId, boolean appAuth) {
        this.userId = userId;
        this.roleList = roleList;
        this.organizationId = organizationId;
        this.companyId = companyId;
        this.appAuth = appAuth;
    }

    public AccessData() {
    }

    public static AccessData fromString(String accessDataString) {
        AccessData accessData = null;
        if (accessDataString != null) {
            try {
                accessData = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(accessDataString)), AccessData.class);
            } catch (IOException e) {
                throw new AppException(AppException.ERROR_CODE.ACCESS_DENIED, "Failed to decode access data");
            }
        }
        return accessData;
    }

    public String toString() {
        String accessDataString;
        try {
            accessDataString = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new AppException(AppException.ERROR_CODE.ACCESS_DENIED, "Failed to encode access data");
        }
        return accessDataString;
    }
}
