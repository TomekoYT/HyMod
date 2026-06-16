package tomeko.hymod.utils

import tomeko.hymod.config.HyModConfig

//? if >= 1.21.11 {
/*import org.slf4j.Logger
import org.slf4j.LoggerFactory
*///?}

object Debug {
    //? if >= 1.21.11 {
    /*private val LOGGER: Logger = LoggerFactory.getLogger(Constants.MOD_ID)
    *///?}

    fun println(message: String) {
        if (!HyModConfig.debugModeEnabled) return

        //? if = 1.8.9 {
        kotlin.io.println("[${Constants.MOD_NAME}] $message")
        //?} else {
        /*LOGGER.info("[${Constants.MOD_NAME}] $message")
        *///?}
    }
}