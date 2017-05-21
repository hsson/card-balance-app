package se.creotec.chscardbalance2.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.github.paolorotolo.appintro.ISlidePolicy
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.util.CardNumberMask

class AppFirstRunAddCardFragment : Fragment(), ISlidePolicy {

    private var cardNumberEdit: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_app_first_run_add_card, container, false)
        cardNumberEdit = view.findViewById(R.id.card_number_edit) as EditText
        cardNumberEdit?.addTextChangedListener(CardNumberMask())
        // Make max length = total number of digits in card number + spaces between with good formatting
        val filters = Array<InputFilter>(1, { _ -> InputFilter.LengthFilter(Constants.CARD_NUMBER_LENGTH + 3) })
        cardNumberEdit?.filters = filters
        return view
    }

    override fun isPolicyRespected(): Boolean {
        cardNumberEdit?.let {
            val cardNumber = it.text.toString().replace(" ", "")
            println(cardNumber)
        }
        return false
    }

    override fun onUserIllegallyRequestedNextPage() {
        Toast.makeText(activity, "Card is invalid", Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance(): AppFirstRunAddCardFragment {
            val fragment = AppFirstRunAddCardFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
