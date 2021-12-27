package com.i0dev.Reclaim.objects;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ColorConfigItem extends ConfigItem {

    public int red = 0;
    public int blue = 0;
    public int green = 0;

    public ColorConfigItem(String displayName, int amount, short data, String material, List<String> lore, boolean glow, int red, int blue, int green) {
        this.displayName = displayName;
        this.amount = amount;
        this.data = data;
        this.material = material;
        this.lore = lore;
        this.glow = glow;
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

}