package com.unsarten.app

data class Constants(val name: String, val value: String) {
    companion object {
        // DON'T CHANGE THIS VALUE
        const val URL_DEFAULT = "http://mac-mini-de-ivan.local:3000/"

        // CHANGE THIS VALUE
        const val URL_SERVICES = "http://mac-mini-de-ivan.local:3000/"
    }
}
