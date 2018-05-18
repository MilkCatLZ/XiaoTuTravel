package shy.car.sdk.travel.interfaces

import android.view.View

interface BottomSheetDragListener {
    fun onBottomSheetStateChange(bottomSheet: View, slideOffset: Float)
}