package com.absinthe.anywhere_.services.tile

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.absinthe.anywhere_.services.overlay.CollectorService
import com.absinthe.anywhere_.services.overlay.ICollectorService

@RequiresApi(api = Build.VERSION_CODES.N)
class CollectorTileService : TileService() {

  private var collectorBinder: ICollectorService? = null
  private var connection: ServiceConnection = object : ServiceConnection {
    override fun onServiceDisconnected(name: ComponentName?) {
      collectorBinder = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
      if (service?.pingBinder() == true) {
        collectorBinder = ICollectorService.Stub.asInterface(service)
        collectorBinder?.startCollector()
      }
    }
  }

  override fun onClick() {
    qsTile?.let {
      collectorBinder?.startCollector() ?: run {
        bindService(
          Intent(this, CollectorService::class.java),
          connection,
          Context.BIND_AUTO_CREATE
        )
      }
    }
  }

  override fun onStopListening() {
    super.onStopListening()
    if (collectorBinder != null) {
      collectorBinder?.stopCollector()
      unbindService(connection)
      collectorBinder = null
    }
  }
}
