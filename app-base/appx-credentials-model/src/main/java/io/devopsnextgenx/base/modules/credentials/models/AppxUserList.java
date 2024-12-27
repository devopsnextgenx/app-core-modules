package io.devopsnextgenx.base.modules.credentials.models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppxUserList {
    private List<AppxUser> userList;
}
