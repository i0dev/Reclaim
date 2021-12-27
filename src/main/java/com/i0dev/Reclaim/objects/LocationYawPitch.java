package com.i0dev.Reclaim.objects;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LocationYawPitch extends Location {

    public LocationYawPitch(double x, double y, double z, String worldName, float yaw, float pitch) {
        super(x, y, z, worldName);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    float yaw, pitch;
}