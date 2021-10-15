package uz.gita.overlaybutton

import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import uz.gita.overlaybutton.databinding.OverlayWindowBinding

class OverLayService : LifecycleService() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate + Job())

    companion object {
        private val windowType = if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        private val windowFlag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        private val windowFormat = PixelFormat.TRANSLUCENT
        private val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            windowType,
            windowFlag,
            windowFormat
        )
    }

    private var _viewBinding: OverlayWindowBinding? = null
    private val viewBinding: OverlayWindowBinding get() = _viewBinding!!

    private var _windowManager: WindowManager? = null
    private val windowManager: WindowManager get() = _windowManager!!

    override fun onCreate() {
        super.onCreate()
        createOverlayButton()
    }

    private fun createOverlayButton() {
        _viewBinding = OverlayWindowBinding.inflate(LayoutInflater.from(this))
        _windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        viewBinding.btnOk.setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
        }

        viewBinding.btnOk.setOnTouchListener { view, motionEvent ->
            onTouch(view, motionEvent, resources.displayMetrics)
        }
        layoutParams.gravity = Gravity.TOP or Gravity.START
        layoutParams.x = 0
        layoutParams.y = 0
        windowManager.addView(viewBinding.root, layoutParams)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.removeView(viewBinding.root)
        _viewBinding = null
        _windowManager = null
    }

}