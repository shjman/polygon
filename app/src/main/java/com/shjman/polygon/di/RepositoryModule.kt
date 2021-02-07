package com.shjman.polygon.di

import android.content.Context
import com.shjman.polygon.data.local.database.AppDatabase
import com.shjman.polygon.data.repository.CarDescriptionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesCarDescriptionRepository(
        @ApplicationContext context: Context,
        appDatabase: AppDatabase,
    ) = CarDescriptionRepository(context, appDatabase)
}
