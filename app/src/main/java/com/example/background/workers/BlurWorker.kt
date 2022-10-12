package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R

private const val TAG = "BlueWorker"
class BlurWorker(context : Context, params: WorkerParameters): Worker(context, params) {

    override fun doWork(): Result {
        val appCtx = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        makeStatusNotification("BLurring Image", appCtx)

        sleep()
        return try {
//            val picture = BitmapFactory.decodeResource(
//                appCtx.resources,
//                R.drawable.android_cupcake)
//            // Kenapa bitmap factory pas decode perlu app context buat nge blur
//            val output = blurBitmap(picture, appCtx)
//
//            val outputUri = writeBitmapToFile(appCtx, output)
//            makeStatusNotification("Output is $outputUri", appCtx)
//
//            Result.success()
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG,"Invalid Input Uri")
                throw IllegalArgumentException("Invalid Input Uri")
            }

            val resolver = appCtx.contentResolver
            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))
            val output = blurBitmap(picture, appCtx)

            val outputUri = writeBitmapToFile(appCtx, output)
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(outputData)
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            throwable.printStackTrace()
            Result.failure()
        }
    }



}