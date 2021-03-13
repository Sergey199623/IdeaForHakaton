package com.sv.nfcreader.data.repo

import com.sv.nfcreader.data.*

object Repository {
    fun getAccounts(): List<Account> {
        return listOf(
            AccountTemp(1, "https://vk.com/durov"),
            AccountTemp(2, "https://ru-ru.facebook.com/durov"),
            AccountTemp(3, "https://twitter.com/durov")
        )
    }
}