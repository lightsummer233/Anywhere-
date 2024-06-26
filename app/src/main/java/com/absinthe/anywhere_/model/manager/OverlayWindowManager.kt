package com.absinthe.anywhere_.model.manager

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.services.overlay.IOverlayService
import com.absinthe.anywhere_.utils.AppUtils
import com.absinthe.anywhere_.view.home.OverlayView
import timber.log.Timber

class OverlayWindowManager(private val context: Context, private val binder: IOverlayService) {

  private val mWindowManager: WindowManager =
    context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  private val overlayMap = mutableMapOf<String, OverlayView>()

  fun addView(entity: AnywhereEntity) {
    val layoutParams = WindowManager.LayoutParams().apply {
      x = width
      y = height / 2
      width = WindowManager.LayoutParams.WRAP_CONTENT
      height = WindowManager.LayoutParams.WRAP_CONTENT
      gravity = Gravity.END or Gravity.CENTER_VERTICAL
      format = PixelFormat.RGBA_8888
      flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

      type = if (AppUtils.atLeastO()) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
      } else {
        WindowManager.LayoutParams.TYPE_PHONE
      }
    }
    val overlayView = OverlayView(context, binder, layoutParams).apply {
      this.entity = entity
      measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    }
    mWindowManager.addView(overlayView, layoutParams)
    overlayMap[entity.id] = overlayView
    Timber.d("Overlay window addView.")
  }

  fun removeView(entity: AnywhereEntity) {
    overlayMap.remove(entity.id)?.let {
      mWindowManager.removeView(it)
      Timber.d("Overlay window removeView.")
    }
  }

  fun release() {
    overlayMap.clear()
  }

}
