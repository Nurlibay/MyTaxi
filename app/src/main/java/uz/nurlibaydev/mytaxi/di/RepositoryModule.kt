package uz.nurlibaydev.mytaxi.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.nurlibaydev.mytaxi.domain.repository.MainRepository
import uz.nurlibaydev.mytaxi.domain.repository.MainRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun bindMainRepository(impl: MainRepositoryImpl): MainRepository

}