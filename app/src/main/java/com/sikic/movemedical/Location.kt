package com.sikic.movemedical

enum class Location(val locationName: String,  val latitude: Double, val longitude: Double) {
    SAN_DIEGO("San Diego", 32.7157, -117.1611),
    ST_GEORGE("St. George", 37.1041, -113.5841),
    PARK_CITY(
        "Park City",
        40.6461,
        -111.4980
    ),
    DALLAS("Dallas", 32.7767, -96.7970),
    MEMPHIS("Memphis", 35.1495, -90.0490),
    ORLANDO("Orlando", 28.5383, -81.3792);
}