package com.i0dev.Reclaim.config;

import com.i0dev.Reclaim.Heart;
import com.i0dev.Reclaim.templates.AbstractConfiguration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StorageConfig extends AbstractConfiguration {

    public StorageConfig(Heart heart, String path) {
        this.path = path;
        this.heart = heart;
    }

    List<String> claimed = new ArrayList<>();


}
