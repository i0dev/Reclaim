package com.i0dev.Reclaim.objects;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class IndexableConfigItem extends ConfigItem {

    public int index = 0;

    public IndexableConfigItem(String displayName, int amount, short data, String material, List<String> lore, boolean glow, int index) {
        this.displayName = displayName;
        this.amount = amount;
        this.data = data;
        this.material = material;
        this.lore = lore;
        this.glow = glow;
        this.index = index;
    }
}