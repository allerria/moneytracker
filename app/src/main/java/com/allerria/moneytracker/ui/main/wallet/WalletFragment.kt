package com.allerria.moneytracker.ui.main.wallet

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.allerria.moneytracker.R
import com.allerria.moneytracker.Screens
import com.allerria.moneytracker.entity.Transaction
import com.allerria.moneytracker.entity.Wallet
import com.allerria.moneytracker.entity.WalletType
import com.allerria.moneytracker.extensions.formatMoney
import com.allerria.moneytracker.ui.common.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_wallet.*
import timber.log.Timber
import javax.inject.Inject

class WalletFragment : BaseFragment(), WalletView {

    override val layoutRes = R.layout.fragment_wallet
    override val TAG = Screens.WALLET_SCREEN

    companion object {
        fun newInstance(uid: String) = WalletFragment().apply {
            Timber.d(uid)
            arguments = Bundle().apply { putString(TAG, uid) }
        }
    }

    private val transactionsAdapter by lazy { TransactionsAdapter() }

    lateinit var walletUid: String

    @Inject
    @InjectPresenter
    lateinit var presenter: WalletPresenter

    @ProvidePresenter
    fun providePresenter(): WalletPresenter {
        return presenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletUid = arguments?.getString(TAG) ?: ""
        with(transactions_recycler_view) {
            adapter = transactionsAdapter
            setHasFixedSize(true)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)

                    val position = parent.getChildAdapterPosition(view)
                    val last = parent.adapter!!.itemCount - 1

                    outRect.top = resources.getDimension(R.dimen.margin_8).toInt()
                    outRect.left = resources.getDimension(R.dimen.margin_16).toInt()
                    outRect.right = resources.getDimension(R.dimen.margin_16).toInt()

                    if (position == 0) {
                        outRect.top = resources.getDimension(R.dimen.margin_20).toInt()
                    }

                    if (position == last) {
                        outRect.bottom = resources.getDimension(R.dimen.margin_20).toInt()
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.initView(walletUid)
    }

    override fun loadWallet(wallet: Wallet, transactions: List<Transaction>) {
        with(wallet_card_view) {
            when(wallet.type) {
                WalletType.CASH -> wallet_image_view.setImageDrawable(context.getDrawable(R.drawable.ic_wallet_cash))
                WalletType.CARD -> wallet_image_view.setImageDrawable(context.getDrawable(R.drawable.ic_wallet_card))
            }
            name_text_view.text = wallet.name
            balance_text_view.text = wallet.value.formatMoney() + wallet.currency.sign
            if (transactions.isEmpty()) {
                show_charts_image_button.visibility = View.GONE
            } else {
                show_charts_image_button.visibility = View.VISIBLE
                show_charts_image_button.setOnClickListener {
                    val walletChartDialogFragment = WalletChartDialogFragment.newInstance(wallet.uid)
                    walletChartDialogFragment.show(childFragmentManager, Screens.WALLET_CHART_SCREEN)
                }
            }
        }
        transactionsAdapter.setData(transactions)
    }

}