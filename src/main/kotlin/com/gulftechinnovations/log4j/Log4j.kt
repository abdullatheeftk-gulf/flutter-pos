package com.gulftechinnovations.log4j

import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.Logging

class Log4j:Logging {
    override val logger: KotlinLogger
        get() = super.logger
}