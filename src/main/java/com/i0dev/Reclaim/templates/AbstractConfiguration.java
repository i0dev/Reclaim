package com.i0dev.Reclaim.templates;

import com.i0dev.Reclaim.Heart;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractConfiguration {

    public transient Heart heart = null;
    public transient String path = "";

}
