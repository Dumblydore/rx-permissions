@file:JvmName("RxPermission")

package me.mauricee.rxpermission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.requestPermissions(vararg permissions: String): PermissionSingle {
    return PermissionSingle(permissions, this)
}

fun Fragment.requestPermissions(vararg permissions: String): PermissionSingle {
    return PermissionSingle(permissions, requireActivity())
}