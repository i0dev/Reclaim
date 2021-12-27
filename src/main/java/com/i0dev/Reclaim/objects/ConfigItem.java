package com.i0dev.Reclaim.objects;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfigItem {

    public String displayName = "";
    public int amount = 0;
    public short data = 0;
    public String material = "";
    public List<String> lore = new ArrayList<>();
    public boolean glow = true;

}