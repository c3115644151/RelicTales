package com.relictales;

import com.relictales.registry.RelicRegisters;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(RelicTales.MOD_ID)
public class RelicTales {
    public static final String MOD_ID = "relictales";

    public RelicTales(IEventBus modEventBus) {
        RelicRegisters.init(modEventBus);
    }
}
