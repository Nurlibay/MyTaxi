package uz.nurlibaydev.mytaxi.data.models

data class TripHistoryData(
    var tripDate: String,
    val destinationData: DestinationData,
    var tripTime: String,
    var tripPrice: Int,
    var carType: Int
)