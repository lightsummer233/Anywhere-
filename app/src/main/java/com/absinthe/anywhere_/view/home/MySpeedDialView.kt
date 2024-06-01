package com.absinthe.anywhere_.view.home

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.absinthe.libraries.utils.extensions.dp
import com.absinthe.libraries.utils.extensions.dpToDimensionPixelSize
import com.leinardi.android.speeddial.FabWithLabelView
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView

class MySpeedDialView : SpeedDialView {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  )

  init {
    // Work around ripple bug on Android 12 when useCompatPadding = true.
    // @see https://github.com/material-components/material-components-android/issues/2617
    mainFab.apply {
      updateLayoutParams<MarginLayoutParams> {
        setMargins(0)
      }
      useCompatPadding = true
    }
    ViewCompat.setOnApplyWindowInsetsListener(mainFab) { v, insets ->
      val inset = Insets.max(
        insets.getInsets(WindowInsetsCompat.Type.systemBars()),
        insets.getInsets(WindowInsetsCompat.Type.displayCutout())
      )
      updatePadding(bottom = inset.bottom)
      return@setOnApplyWindowInsetsListener insets
    }
  }

  override fun addActionItem(
    actionItem: SpeedDialActionItem,
    position: Int,
    animate: Boolean
  ): FabWithLabelView? {
    return super.addActionItem(actionItem, position, animate)?.apply {
      fab.useCompatPadding = true
    }
  }
}
