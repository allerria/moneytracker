package com.allerria.moneytracker.model.data.datasource.local

import android.content.Context
import com.allerria.moneytracker.R
import com.allerria.moneytracker.entity.Currency
import com.allerria.moneytracker.entity.Wallet
import com.allerria.moneytracker.entity.WalletType
import java.util.*
import javax.inject.Inject

class WalletCache @Inject constructor(context: Context) {
    var wallets: MutableList<Wallet> = mutableListOf(Wallet(UUID.randomUUID().toString(), WalletType.CASH, 1.0, context.getString(R.string.other), Currency.USD), Wallet(UUID.randomUUID().toString(), WalletType.CARD, 1.0, context.getString(R.string.salary), Currency.RUB))
}