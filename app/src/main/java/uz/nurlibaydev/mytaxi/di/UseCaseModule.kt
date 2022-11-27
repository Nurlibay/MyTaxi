package uz.nurlibaydev.mytaxi.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.nurlibaydev.mytaxi.domain.usecases.MainUseCase
import uz.nurlibaydev.mytaxi.domain.usecases.MainUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    fun bindMainUseCase(impl: MainUseCaseImpl): MainUseCase
}