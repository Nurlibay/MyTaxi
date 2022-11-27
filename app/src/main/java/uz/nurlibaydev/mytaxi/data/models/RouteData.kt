package uz.nurlibaydev.mytaxi.data.models

import com.directions.route.Route
import java.util.ArrayList

data class RouteData(
    var routeList: ArrayList<Route>?,
    var shortestRouteIndex: Int
)