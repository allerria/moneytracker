package com.allerria.moneytracker.model.data.repository

import com.allerria.moneytracker.R
import com.allerria.moneytracker.entity.Currency
import com.allerria.moneytracker.entity.Wallet
import com.allerria.moneytracker.entity.WalletType
import com.allerria.moneytracker.model.data.datasource.local.WalletCache
import java.util.*
import javax.inject.Inject

class WalletRepository @Inject constructor(private val walletCache: WalletCache) {

    fun getBalance(uid: String) = walletCache.wallets.find { it.uid == uid }!!

    fun setBalance(uid: String, value: Double) {
        walletCache.wallets.find { it.uid == uid }!!.value = value
    }

    fun getWallet(uid: String) = walletCache.wallets.find { it.uid == uid }

    fun getWallets() = walletCache.wallets

    fun addWallet(wallet: Wallet) {
        walletCache.wallets.add(wallet)
    }

    fun clear() {
        walletCache.wallets.clear()
    }
}