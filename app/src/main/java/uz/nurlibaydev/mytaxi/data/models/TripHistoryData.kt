package uz.nurlibaydev.mytaxi.data.models

/**
 *  Created by Nurlibay Koshkinbaev on 24/11/2022 15:10
 */

data class TripHistoryData(
    var tripDate: String,
    var fromWhere: String,
    var toWhere: String,
    var tripTime: String,
    var carType: Int,
    var tripPrice: Int
)