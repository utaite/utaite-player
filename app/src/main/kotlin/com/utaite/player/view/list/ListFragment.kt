package com.utaite.player.view.list

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.utaite.player.R
import com.utaite.player.base.BaseFragment
import com.utaite.player.rest.*
import com.utaite.player.util.IS_SORTED
import com.utaite.player.util.PreferenceUtil
import com.utaite.player.util.SettingUtil
import com.utaite.player.view.detail.DetailActivity
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list_view.view.*


const val SORTED_NEWEST_UPLOAD = 0
const val SORTED_MOST_VIEW = 1
const val SORTED_TITLE = 2

private const val BACKGROUND_ALPHA = 170
private const val IMAGE_HEIGHT_VALUE = 0.7
private const val TITLE_HEIGHT_VALUE = 0.3
private const val TITLE_PADDING_VALUE = 0.2


class ListFragment : BaseFragment(), OnDataClickListener<Data> {

    override val layoutId: Int = R.layout.fragment_list
    override val self = this@ListFragment

    private val requestManager: RequestManager by lazy { Glide.with(self) }

    override fun init() {
        val sortedIndex: Int = PreferenceUtil.getInstance(activity.applicationContext).getInt(IS_SORTED, SORTED_NEWEST_UPLOAD)
        val realm = Realm.getDefaultInstance()
        val dataSet: MutableList<Data> = when (sortedIndex) {
            SORTED_NEWEST_UPLOAD -> realm.getDataSet().findAllSorted(INDEX, Sort.DESCENDING)
            SORTED_MOST_VIEW -> realm.getDataSet().findAllSorted(COUNT, Sort.DESCENDING)
            SORTED_TITLE -> realm.getDataSet().findAllSorted(TITLE)
            else -> realm.getDataSet().findAll()
        }.toMutableList()
        listRecyclerView.run {
            layoutManager = GridLayoutManager(activity, SettingUtil.RECYCLER_SPAN_COUNT)
            adapter = ListAdapter(dataSet, self)
        }
    }

    private fun Realm.getDataSet(): RealmQuery<Data> =
            where(Data::class.java).equalTo(UTAITE, arguments.getInt(UTAITE))

    override fun onDataClick(data: Data) {
        val intent: Intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(UTAITE, data.utaite)
            putExtra(TITLE, data.title)
            putExtra(WATCH, data.watch)
        }
        startActivity(intent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, data: Data) {
        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .format(DecodeFormat.PREFER_ARGB_8888)
        requestManager
                .asBitmap()
                .apply(requestOptions)
                .load("http://tn.smilevideo.jp/smile?i=${data.watch}")
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
                            text = data.title
                            layoutParams = layoutParams.apply {
                                height = (holder.itemView.listViewImage.layoutParams.height * TITLE_HEIGHT_VALUE).toInt()
                                setPadding((height * TITLE_PADDING_VALUE).toInt())
                            }
                        }
                    }
                })
    }

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
