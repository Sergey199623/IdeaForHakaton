package com.sv.nfcreader.data.repo

import com.sv.nfcreader.data.*

object Repository {
    fun getAccounts(): List<Account> {
        return listOf(
            Account(1, "vk", "https://i.ibb.co/xYwzbDR/image-vk.png", "https://vk.com/durov"),
            Account(2, "facebook", "https://i.ibb.co/Y0fsfRp/image-fb.png", "https://ru-ru.facebook.com/durov"),
            Account(3, "twitter", "https://i.ibb.co/LZWd1MD/image-tw.png", "https://twitter.com/durov")
        )
    }
}