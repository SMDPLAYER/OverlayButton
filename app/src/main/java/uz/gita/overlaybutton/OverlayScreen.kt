package uz.gita.overlaybutton

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.overlaybutton.databinding.ScreenOverlayBinding

class OverlayScreen : Fragment(R.layout.screen_overlay) {
    private val viewBinding: ScreenOverlayBinding by viewBinding(ScreenOverlayBinding::bind)
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            startOverlay()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.btnStart.setOnClickListener {
            checkOverlayWindowPermission {
                startOverlay()
            }
        }
        viewBinding.btnStop.setOnClickListener {
            closeOverlay()
        }
    }

    private fun checkOverlayWindowPermission(block: () -> Unit) {
        if (Build.VERSION.SDK_INT < 23) {
            block()
            return
        }
        if (!Settings.canDrawOverlays(requireContext())) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${requireActivity().packageName}"))
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                launcher.launch(intent)
            }
        } else {
            block()
        }
    }

    private fun startOverlay(){
        val intent = Intent(requireContext(), OverLayService::class.java)
        requireActivity().startService(intent)
    }

    private fun closeOverlay(){
        val intent = Intent(requireContext(), OverLayService::class.java)
        requireActivity().stopService(intent)
    }
}