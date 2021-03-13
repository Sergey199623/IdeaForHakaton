package com.sv.nfcreader.data.repo

import com.sv.nfcreader.data.*

object Repository {
    fun getAccounts(): List<Account> {
        return listOf(
            Account(1, "vk", "https://ibb.co/KDBVgX3", "https://vk.com/durov"),
            Account(2, "facebook", "https://ibb.co/LN979kn", "https://ru-ru.facebook.com/durov"),
            Account(3, "twitter", "https://ibb.co/sVG9qhp", "https://twitter.com/durov")
        )
    }
}