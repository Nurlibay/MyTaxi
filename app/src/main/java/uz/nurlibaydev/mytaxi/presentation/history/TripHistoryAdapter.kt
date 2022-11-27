package uz.nurlibaydev.mytaxi.presentation.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.data.models.TripHistoryData
import uz.nurlibaydev.mytaxi.databinding.ItemTripHistoryBinding
import uz.nurlibaydev.mytaxi.utils.changeFormat
import uz.nurlibaydev.mytaxi.utils.onClick

class TripHistoryAdapter : ListAdapter<TripHistoryData, TripHistoryAdapter.TripHistoryViewHolder>(TripHistoryItemCallBack) {

    inner class TripHistoryViewHolder(private val binding: ItemTripHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(absoluteAdapterPosition)
            binding.apply {
                item.tripDate.keys.forEach {
                    if(it == 0) {
                        tvDate.isVisible = true
                        tvDate.text = item.tripDate[it]
                    } else {
                        tvDate.visibility = View.GONE
                    }
                }
                tvFirstAddress.text = item.destinationData.fromWhere
                tvSecondAddress.text = item.destinationData.toWhere
                tvTime.text = item.tripTime
                tvMoney.text = item.tripPrice.changeFormat()
                when (item.carType) {
                    1 -> ivCarType.setImageResource(R.drawable.icon_white_car)
                    2 -> ivCarType.setImageResource(R.drawable.icon_yellow_car)
                    else -> ivCarType.setImageResource(R.drawable.icon_black_car)
                }
                tripCardView.onClick {
                    itemClick.invoke(item)
                }
            }
        }
    }

    private var itemClick: (groupData: TripHistoryData) -> Unit = {}
    fun setOnItemClickListener(block: (TripHistoryData) -> Unit){
        itemClick = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripHistoryViewHolder {
        return TripHistoryViewHolder(ItemTripHistoryBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_trip_history, parent,false)))
    }

    override fun onBindViewHolder(holder: TripHistoryViewHolder, position: Int) {
        holder.bind()
    }
}

object TripHistoryItemCallBack : DiffUtil.ItemCallback<TripHistoryData>() {
    override fun areItemsTheSame(oldItem: TripHistoryData, newItem: TripHistoryData): Boolean {
        return oldItem.destinationData.fromWhere == newItem.destinationData.fromWhere &&
                oldItem.destinationData.toWhere == newItem.destinationData.toWhere
    }

    override fun areContentsTheSame(oldItem: TripHistoryData, newItem: TripHistoryData): Boolean {
        return oldItem.destinationData.fromWhere == newItem.destinationData.fromWhere &&
                oldItem.destinationData.toWhere == newItem.destinationData.toWhere &&
                oldItem.tripTime == newItem.tripTime && oldItem.tripPrice == newItem.tripPrice
                && oldItem.carType == newItem.carType && oldItem.tripDate == newItem.tripDate
    }
}