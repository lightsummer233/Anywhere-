package com.absinthe.anywhere_.model.viewholder

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable

data class AppListBean(
  val id: String,
  val appName: String = "",
  val packageName: String = "",
  val className: String = "",
  var icon: Drawable = Color.TRANSPARENT.toDrawable(),
  val type: Int,
  val isExported: Boolean = false,
  var isLaunchActivity: Boolean = false
)
