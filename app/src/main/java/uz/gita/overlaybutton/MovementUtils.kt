package uz.gita.overlaybutton

import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View

/**
 * Created by Siddikov Mukhriddin on 1/27/21
 */
var dX = 0f
var dY = 0f
var shouldClick = false;
fun onTouch(view: View, event: MotionEvent, mContainer: DisplayMetrics): Boolean {
    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            shouldClick=true
            dX = view.x - event.rawX
            dY = view.y - event.rawY
        }
        MotionEvent.ACTION_UP->{
            val k = view.animate()
            when{
                view.x<mContainer.widthPixels/2->{
                    k.x(0f)
                }
                view.x>mContainer.widthPixels/2->{
                    k.x((mContainer.widthPixels-view.width).toFloat())
                }

            }
            when{
                view.y<mContainer.heightPixels/2->{
                    k.y(0f)
                }
                view.y>mContainer.heightPixels/2->{
                    k.y((mContainer.heightPixels-1.5*view.height).toFloat())
                }
            }

            k.setDuration(0)
            k.start()

            if (shouldClick)
            view.performClick()
        }
        MotionEvent.ACTION_MOVE -> {
            if ((event.getEventTime() - event.getDownTime())>200)
            shouldClick=false
            val k = view.animate()
            when {
                event.rawX + dX + view.width > mContainer.widthPixels -> k.x((mContainer.widthPixels - view.width).toFloat())
                event.rawX + dX < 0 -> k.x(0F)
                else -> k.x(event.rawX + dX)
            }
            when {
                event.rawY + dY + view.height > mContainer.heightPixels -> k.y((mContainer.heightPixels - view.height).toFloat())
                event.rawY + dY < 0 -> k.y(0F)
                else -> k.y(event.rawY + dY)
            }
//                if (event.rawX + dX + view.width <= mContainer.width && event.rawX + dX > 0 &&
//                    event.rawY + dY + view.height <= mContainer.height && event.rawY + dY > 0
//                )
            k.setDuration(0)
            k.start()
        }
        else -> return false
    }
    return true

}