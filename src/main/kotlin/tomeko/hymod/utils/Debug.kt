package tomeko.hymod.utils

import tomeko.hymod.config.HyModConfig

//? if >= 1.21.11-fabric {
import org.slf4j.Logger
import org.slf4j.LoggerFactory
//?}

object Debug {
    //? if >= 1.21.11-fabric {
    private val LOGGER: Logger = LoggerFactory.getLogger(Constants.MOD_ID)
    //?}

    fun log(message: String) {
        if (!HyModConfig.debugModeEnabled) return

        forceLog(message)
    }

    fun forceLog(message: String) {
        //? if = 1.8.9-forge {
        /*println("[${Constants.MOD_NAME}] $message")
        *///?} else {
        LOGGER.info("[${Constants.MOD_NAME}] $message")
        //?}
    }
}