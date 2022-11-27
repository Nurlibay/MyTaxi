package uz.nurlibaydev.mytaxi.di

import android.content.Context
import android.location.Geocoder
import androidx.room.PrimaryKey
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MapModule {

    @[Provides Singleton]
    fun provideGeocoder(@ApplicationContext context: Context) =
        Geocoder(context, Locale.getDefault())

    @[Provides Singleton]
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)
}