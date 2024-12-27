package io.devopsnextgenx.base.modules.credentials.models;

import java.util.List;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class AppxUserList {
    private List<AppxUser> userList;
}
