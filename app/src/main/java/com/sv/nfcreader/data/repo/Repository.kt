package com.sv.nfcreader.data.repo

import com.sv.nfcreader.data.Account
import com.sv.nfcreader.data.Facebook
import com.sv.nfcreader.data.Twitter
import com.sv.nfcreader.data.Vk

class Repository {
    fun getAccounts(): List<Account> {
        return listOf(
            Vk(1, "https://vk.com/durov"),
            Facebook(2, "https://ru-ru.facebook.com/durov"),
            Twitter(3, "https://twitter.com/durov")
        )
    }
}