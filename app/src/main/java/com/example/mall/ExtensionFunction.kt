package com.example.mall

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun Fragment.navigateNextWithDefaultAnim(fragmentInstance: Fragment, tag: String) {
    requireActivity().supportFragmentManager.beginTransaction().apply {
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        replace(R.id.frag_container, fragmentInstance, tag)
        addToBackStack(backStackName)
        commit()
    }
}

fun Fragment.navigateNextWithCustomAnim(fragmentInstance: Fragment, tag: String = "") {
    requireActivity().supportFragmentManager.beginTransaction().apply {
        (activity as MainActivity).bottomNavigationView.menu.getItem(3).isChecked = true
        setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        replace(R.id.frag_container, fragmentInstance, tag)
        addToBackStack(backStackName)
        commit()
    }
}


fun String.rupeeString(price: Int): String {
    return "₹ $price"
}

fun String.rupeeString(price: String): String {
    return "₹ $price"
}