package me.mauricee.rxpermission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.reactivex.SingleObserver
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.*

class PermissionSingle internal constructor(
    private val permissions: Array<out String>,
    private val activity: FragmentActivity
) :
    Single<List<PermissionResult>>() {

    override fun subscribeActual(observer: SingleObserver<in List<PermissionResult>>) {
        activity.supportFragmentManager.beginTransaction().add(
            Listener(permissions, observer).apply { observer.onSubscribe(this) },
            PermissionTag
        ).commitAllowingStateLoss()
    }

    /**
     * A QOL method for checking if all permission requests were granted. */
    fun all(): Single<Boolean> = map { results -> results.all { it.isGranted } }

    class Listener(
        private val permissions: Array<out String>,
        private val observer: SingleObserver<in List<PermissionResult>>
    ) : Fragment(), Disposable {
        private val resultCode by lazy { id }
        private var isDisposed = false
        private lateinit var results: MutableMap<String, Boolean>

        init {
            retainInstance = true
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            results = permissions.map {
                it to (ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED)
            }.toMap().toMutableMap()
            if (results.any { !it.value }) {
                requestPermissions(
                    results.filter { !it.value && !shouldShowRequestPermissionRationale(it.key) }.map { it.key }.toTypedArray(),
                    resultCode
                )
            } else if (!isDisposed) {
                onSuccess()
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            if (requestCode == resultCode && !isDisposed) {
                permissions.forEachIndexed { index, permission ->
                    results[permission] = grantResults[index] == PackageManager.PERMISSION_GRANTED
                }
                onSuccess()
            }
        }

        override fun isDisposed(): Boolean = isDisposed

        override fun dispose() {
            fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
            isDisposed = true
        }

        private fun onSuccess() {
            observer.onSuccess(results.map {
                PermissionResult(
                    it.key,
                    it.value
                )
            })
        }
    }

    companion object {
        private val PermissionTag = Listener::class.java.simpleName
    }
}