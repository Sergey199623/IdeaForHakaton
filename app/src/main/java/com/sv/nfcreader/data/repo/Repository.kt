package com.sv.nfcreader.data.repo

import com.sv.nfcreader.data.*

class Repository {
    fun getAccounts(): List<Account> {
        return listOf(
            Vk("https://ibb.co/KDBVgX3", 1, "https://vk.com/durov"),
            Facebook("https://ibb.co/LN979kn",2, "https://ru-ru.facebook.com/durov"),
            Twitter("https://ibb.co/sVG9qhp",3, "https://twitter.com/durov")
        )
    }
}