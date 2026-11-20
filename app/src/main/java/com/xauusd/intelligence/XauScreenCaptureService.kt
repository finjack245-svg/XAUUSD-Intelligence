package com.xauusd.intelligence

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.atomic.AtomicBoolean

class XauScreenCaptureService : Service() {
    companion object {
        const val EXTRA_RESULT_CODE = "xau_capture_result_code"
        const val EXTRA_RESULT_DATA = "xau_capture_result_data"
        const val ACTION_PAUSE = "com.xauusd.intelligence.PAUSE"
        const val ACTION_RESUME = "com.xauusd.intelligence.RESUME"
        const val ACTION_STOP = "com.xauusd.intelligence.STOP"
        private const val CHANNEL = "xau_screen_analysis"
        private const val NOTIFICATION_ID = 7310
        private const val INTERVAL_MS = 1500L
    }

    private var projection: MediaProjection? = null
    private var display: VirtualDisplay? = null
    private var reader: ImageReader? = null
    private var worker: HandlerThread? = null
    private var handler: Handler? = null
    private val recognizer by lazy { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }
    private val busy = AtomicBoolean(false)
    @Volatile private var paused = false
    @Volatile private var lastCheck = 0L
    private var checks = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PAUSE -> {
                paused = true
                reader?.setOnImageAvailableListener(null, null)
                notifyStatus("Paused · no new screen frames are being analysed")
                return START_NOT_STICKY
            }
            ACTION_RESUME -> {
                if (projection == null) notifyStatus("Capture permission required · press START in the app")
                else {
                    paused = false
                    attachListener()
                    notifyStatus("Resumed · checking the shared screen about every 1.5 seconds")
                }
                return START_NOT_STICKY
            }
            ACTION_STOP -> { stopSelf(); return START_NOT_STICKY }
        }

        createChannel()
        val notification = notification("Starting MT5 screen analysis…")
        if (Build.VERSION.SDK_INT >= 29) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        val code = intent?.getIntExtra(EXTRA_RESULT_CODE, 0) ?: 0
        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent?.getParcelableExtra(EXTRA_RESULT_DATA, Intent::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra(EXTRA_RESULT_DATA)
        }
        if (data != null && projection == null) beginCapture(code, data)
        return START_NOT_STICKY
    }

    private fun beginCapture(code: Int, data: Intent) {
        projection = (getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager).getMediaProjection(code, data)
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay.getRealMetrics(metrics)
        worker = HandlerThread("XauScreenOCR").also { it.start() }
        handler = Handler(worker!!.looper)
        reader = ImageReader.newInstance(metrics.widthPixels.coerceAtLeast(1), metrics.heightPixels.coerceAtLeast(1), PixelFormat.RGBA_8888, 2)
        attachListener()
        projection?.registerCallback(object : MediaProjection.Callback() {
            override fun onStop() {
                notifyStatus("Android ended screen sharing · start again and approve a new session")
                stopSelf()
            }
        }, handler)
        display = projection?.createVirtualDisplay(
            "XAUUSD-MT5-Screen", metrics.widthPixels, metrics.heightPixels, metrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, reader?.surface, null, handler
        )
    }

    private fun attachListener() {
        if (paused) return
        reader?.setOnImageAvailableListener({ source ->
            val image = source.acquireLatestImage() ?: return@setOnImageAvailableListener
            val now = SystemClock.elapsedRealtime()
            if (now - lastCheck < INTERVAL_MS || !busy.compareAndSet(false, true)) {
                image.close()
                return@setOnImageAvailableListener
            }
            lastCheck = now
            recognizer.process(InputImage.fromMediaImage(image, 0))
                .addOnSuccessListener { result ->
                    checks++
                    val visible = result.text.lines().map { it.trim() }.filter { it.isNotEmpty() }
                        .take(8).joinToString(" · ")
                    notifyStatus(if (visible.isBlank()) {
                        "Screen checks: $checks · no readable text detected"
                    } else {
                        "Screen checks: $checks · visible text: ${visible.take(150)}"
                    })
                }
                .addOnFailureListener { notifyStatus("OCR check failed · capture remains active") }
                .addOnCompleteListener {
                    image.close()
                    busy.set(false)
                }
        }, handler)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(CHANNEL, "XAUUSD screen analysis", NotificationManager.IMPORTANCE_LOW)
            )
        }
    }

    private fun notification(message: String): Notification =
        NotificationCompat.Builder(this, CHANNEL)
            .setSmallIcon(android.R.drawable.ic_menu_view)
            .setContentTitle("XAUUSD Intelligence")
            .setContentText(message)
            .setOngoing(true)
            .build()

    private fun notifyStatus(message: String) {
        getSystemService(NotificationManager::class.java).notify(NOTIFICATION_ID, notification(message))
    }

    override fun onDestroy() {
        reader?.setOnImageAvailableListener(null, null)
        display?.release()
        reader?.close()
        projection?.stop()
        worker?.quitSafely()
        recognizer.close()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
