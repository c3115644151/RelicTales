package com.relictales;

import com.relictales.registry.RelicRegisters;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(RelicTales.MOD_ID)
public class RelicTales {
    public static final String MOD_ID = "relictales";
    public static final Logger LOGGER = LoggerFactory.getLogger("relictales");

    public RelicTales(IEventBus modEventBus) {
        RelicRegisters.init(modEventBus);
    }
}
