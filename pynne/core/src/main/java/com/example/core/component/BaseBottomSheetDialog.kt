package com.example.core.component

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomDialog : BottomSheetDialogFragment() {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        isCancelable = false
    }

    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(cancelable)

        val dialog = dialog as BottomSheetDialog
        dialog.setCanceledOnTouchOutside(cancelable)

        val bottomSheetView =
            dialog.window?.decorView?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return
        BottomSheetBehavior.from(bottomSheetView).isHideable = cancelable;
    }

}