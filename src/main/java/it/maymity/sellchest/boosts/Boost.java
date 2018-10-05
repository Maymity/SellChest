package it.maymity.sellchest.boosts;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter @AllArgsConstructor
public class Boost {
    private UUID player;
    private long expireTime;
}
