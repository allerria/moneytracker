package com.allerria.moneytracker.ui.main.transaction

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.allerria.moneytracker.R
import com.allerria.moneytracker.Screens
import com.allerria.moneytracker.entity.*
import com.allerria.moneytracker.model.interactor.WalletInteractor
import com.allerria.moneytracker.ui.common.BaseDialogFragment
import com.allerria.moneytracker.ui.common.BaseFragment
import com.allerria.moneytracker.ui.main.balance.BalanceFragment
import com.arellomobile.mvp.MvpDialogFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_about.view.*
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class AddTransactionFragment : BaseFragment(), AddTransactionView {

    override val TAG = Screens.ADD_TRANSACTION_SCREEN
    override val layoutRes = R.layout.fragment_add_transaction
    lateinit var localWallets: List<Wallet>

    @Inject
    @InjectPresenter
    lateinit var presenter: AddTransactionPresenter

    @ProvidePresenter
    fun providePresenter(): AddTransactionPresenter {
        return presenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transaction_type_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) { }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                lateinit var adapter: ArrayAdapter<String>
                when (transaction_type_spinner.selectedItem) {
                    getString(R.string.expense) -> adapter = ArrayAdapter(context, android.R.layout.simple_selectable_list_item, resources.getStringArray(R.array.transaction_expense_categories))
                    getString(R.string.income) -> adapter = ArrayAdapter(context, android.R.layout.simple_selectable_list_item, resources.getStringArray(R.array.transaction_income_categories))
                }
                transaction_category_spinner.adapter = adapter
            }

            override fun onNothingSelected(p0: AdapterView<*>?){ }
        }
        presenter.initWallets()
        add_transaction_button.setOnClickListener {
            val transaction = createTransaction()
            if (transaction.money.value < 0) {
                Toast.makeText(this@AddTransactionFragment.context, R.string.fill_value, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@AddTransactionFragment.context, R.string.success, Toast.LENGTH_LONG).show()
                presenter.addTransaction(transaction)
            }
        }
    }

    override fun onDestroy() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity?.currentFocus
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        super.onDestroy()
    }

    override fun setWallets(wallets: List<Wallet>) {
        transaction_wallet_spinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_selectable_list_item, wallets.map { it ->
            val type = when (it.type) {
                WalletType.CARD -> getString(R.string.card)
                WalletType.CASH -> getString(R.string.cash)
            }
            "${it.name} - $type - ${it.currency.sign}"
        })
        localWallets = wallets
    }

    private fun createTransaction(): Transaction {
        lateinit var transactionType: TransactionType
        lateinit var transactionCategory: TransactionCategory
        lateinit var money: Money
        when (transaction_type_spinner.selectedItem.toString()) {
            getString(R.string.expense) -> transactionType = TransactionType.EXPENSE
            getString(R.string.income) -> transactionType = TransactionType.INCOME
            else -> TransactionType.EXPENSE
        }
        when (transaction_category_spinner.selectedItem.toString()) {
            getString(R.string.auto) -> transactionCategory = TransactionCategory.AUTO
            getString(R.string.clothing) -> transactionCategory = TransactionCategory.CLOTHING
            getString(R.string.house) -> transactionCategory = TransactionCategory.HOUSE
            getString(R.string.cafe_and_restaurants) -> transactionCategory = TransactionCategory.CAFE_AND_RESTAURANT
            getString(R.string.entertainment) -> transactionCategory = TransactionCategory.ENTERTAINMENT
            getString(R.string.health) -> transactionCategory = TransactionCategory.HEALTH
            getString(R.string.salary) -> transactionCategory = TransactionCategory.SALARY
            getString(R.string.gift) -> transactionCategory = TransactionCategory.GIFT
            getString(R.string.other) -> transactionCategory = TransactionCategory.OTHER
            else -> TransactionCategory.OTHER
        }
        val details: String = transaction_details_edit_text.text.toString()

        val wallet: Wallet = localWallets.find {
            val type = when (it.type) {
                WalletType.CARD -> getString(R.string.card)
                WalletType.CASH -> getString(R.string.cash)
            }
            "${it.name} - $type - ${it.currency.sign}" == transaction_wallet_spinner.selectedItem.toString()
        }!!
        val transactionValue = if (transaction_value_edit_text.text.toString().isNotEmpty()) transaction_value_edit_text.text.toString() else "-1.0"
        money = Money(wallet.currency, transactionValue.toDouble())
        return Transaction(UUID.randomUUID().toString(), transactionType, transactionCategory, money, wallet.uid, details, Calendar.getInstance().time)
    }
}