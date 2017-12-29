package com.utaite.player.view.list

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.utaite.player.R
import com.utaite.player.data.Data
import com.utaite.player.util.SettingUtil
import com.utaite.player.util.getView
import kotlinx.android.synthetic.main.fragment_list_view.view.*


private const val BACKGROUND_ALPHA = 170
private const val IMAGE_HEIGHT_VALUE = 0.7
private const val TITLE_HEIGHT_VALUE = 0.3
private const val TITLE_PADDING_VALUE = 0.2


class ListAdapter(private val context: Context,
                  private val dataSet: MutableList<Data>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private val requestManager: RequestManager by lazy { Glide.with(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(getView(parent.context, R.layout.fragment_list_view)).apply {

            }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .format(DecodeFormat.PREFER_ARGB_8888)
        requestManager
                .asBitmap()
                .apply(requestOptions)
                .load("http://tn.smilevideo.jp/smile?i=${dataSet[position].url}")
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>) {
                        holder.itemView.listViewImage.run {
                            setImageBitmap(bitmap)
                            layoutParams = layoutParams.apply {
                                height = ((context.resources.displayMetrics.widthPixels / SettingUtil.RECYCLER_SPAN_COUNT) / bitmap.width) * (bitmap.height * IMAGE_HEIGHT_VALUE).toInt()
                            }
                        }

                        holder.itemView.listViewTitle.run {
                            val dominantColor = bitmap.getDominantColor()
                            setTextColor(dominantColor)
                            setBackgroundColor(dominantColor.getOppositionColor())
                            background.alpha = BACKGROUND_ALPHA
                            text = context.getString(dataSet[position].title)
                            layoutParams = layoutParams.apply {
                                height = (holder.itemView.listViewImage.layoutParams.height * TITLE_HEIGHT_VALUE).toInt()
                                setPadding((height * TITLE_PADDING_VALUE).toInt())
                            }
                        }
                    }
                })
    }

    override fun getItemCount(): Int
            = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private fun Bitmap.getDominantColor(): Int {
        val resize = Bitmap.createScaledBitmap(this, 1, 1, true)
        val color = resize.getPixel(0, 0)
        resize.recycle()
        return color
    }

    private fun Int.getOppositionColor(): Int =
            Color.rgb(255 - Color.red(this), 255 - Color.green(this), 255 - Color.blue(this))

    private fun View.setPadding(value: Int) =
            setPadding(value, value, value, value)

}
