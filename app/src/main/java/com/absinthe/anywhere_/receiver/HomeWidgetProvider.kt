package com.absinthe.anywhere_.receiver

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.content.IntentCompat
import androidx.core.net.toUri
import com.absinthe.anywhere_.BuildConfig
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.constants.AnywhereType
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.services.widget.AppRemoteViewsService
import com.absinthe.anywhere_.ui.shortcuts.ShortcutsActivity
import com.absinthe.anywhere_.utils.FlagDelegate

class HomeWidgetProvider : AppWidgetProvider() {
  /**
   * 每次窗口小部件被更新都调用一次该方法
   */
  override fun onUpdate(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetIds: IntArray
  ) {
    appWidgetIds.forEach { appWidgetId ->
      // 创建一个 RemoteView
      val remoteViews = RemoteViews(context.packageName, R.layout.widget_home).apply {

        // 把这个 Widget 绑定到 RemoteViewsService
        val intent = Intent(context, AppRemoteViewsService::class.java).apply {
          putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
          data = toUri(Intent.URI_INTENT_SCHEME).toUri()
        }

        // 设置适配器
        setRemoteAdapter(R.id.lv_list, intent)

        // 设置当显示的 widget_list 为空显示的 View
        setEmptyView(R.id.lv_list, R.layout.widget_home)
      }

      val pendingIntentTemplate: PendingIntent = Intent(
        context,
        HomeWidgetProvider::class.java
      ).run {
        // 设置 Action，方便在 onReceive 中区别点击事件
        action = CLICK_ACTION
        data = toUri(Intent.URI_INTENT_SCHEME).toUri()
        PendingIntent.getBroadcast(
          context, 0, this,
          PendingIntent.FLAG_UPDATE_CURRENT or FlagDelegate.PENDING_INTENT_FLAG_MUTABLE
        )
      }

      remoteViews.setPendingIntentTemplate(R.id.lv_list, pendingIntentTemplate)

      // 更新 Widget
      appWidgetManager.apply {
        updateAppWidget(ComponentName(context, HomeWidgetProvider::class.java), remoteViews)
        notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_list)
      }
    }

    super.onUpdate(context, appWidgetManager, appWidgetIds)
  }

  /**
   * 接收窗口小部件点击时发送的广播
   */
  override fun onReceive(context: Context, intent: Intent) {
    if (CLICK_ACTION == intent.action) {
      val newIntent = Intent(context, ShortcutsActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      val ae: AnywhereEntity? = IntentCompat.getParcelableExtra(intent, Const.INTENT_EXTRA_WIDGET_ENTITY, AnywhereEntity::class.java)

      if (ae != null) {
        if (ae.type == AnywhereType.Card.IMAGE) {
          newIntent.action = ShortcutsActivity.ACTION_START_IMAGE
          newIntent.putExtra(Const.INTENT_EXTRA_SHORTCUTS_CMD, ae.param1)
        } else {
          newIntent.action = ShortcutsActivity.ACTION_START_FROM_WIDGET
          newIntent.putExtra(Const.INTENT_EXTRA_WIDGET_ENTITY, ae)
        }
        context.startActivity(newIntent)
      }
    }

    super.onReceive(context, intent)
  }

  companion object {
    const val CLICK_ACTION = "${BuildConfig.APPLICATION_ID}.action.CLICK"
  }
}
