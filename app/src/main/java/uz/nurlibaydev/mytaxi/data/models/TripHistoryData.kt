package uz.nurlibaydev.mytaxi.data.models

data class TripHistoryData(
    var tripDate: Map<Int, String>,
    val destinationData: DestinationData,
    var tripTime: String,
    var tripPrice: Long,
    var carType: Int
)