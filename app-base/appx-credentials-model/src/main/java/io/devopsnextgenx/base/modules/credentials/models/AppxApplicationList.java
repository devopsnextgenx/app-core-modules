package io.devopsnextgenx.base.modules.credentials.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppxApplicationList {
    private List<AppxApplication> applicationList;
}
