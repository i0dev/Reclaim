package com.i0dev.Reclaim.config;

import com.i0dev.Reclaim.Heart;
import com.i0dev.Reclaim.objects.ReclaimOption;
import com.i0dev.Reclaim.templates.AbstractConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GeneralConfig extends AbstractConfiguration {

    public GeneralConfig(Heart heart, String path) {
        this.path = path;
        this.heart = heart;
    }


    List<ReclaimOption> reclaimOptions = Arrays.asList(
            new ReclaimOption(
                    "Knight",
                    "reclaim.rank.knight",
                    Arrays.asList(
                            "eco give {player} 250000",
                            "token add {player} 20000",
                            "crate give {player} Mystery 2"
                    )),
            new ReclaimOption(
                    "Gladiator",
                    "reclaim.rank.gladiator",
                    Arrays.asList(
                            "eco give {player} 1500000",
                            "token add {player} 300000",
                            "crate give {player} Mystery 4",
                            "crate give {player} Ancient 1",
                            "crate give {player} Carnage 2"
                    )),
            new ReclaimOption(
                    "Rival",
                    "reclaim.rank.rival",
                    Arrays.asList(
                            "eco give {player} 4000000",
                            "token add {player} 500000",
                            "crate give {player} Mystery 5",
                            "crate give {player} Ancient 3",
                            "crate give {player} Carnage 2",
                            "crate give {player} Raider 1"

                    ))
    );
}
