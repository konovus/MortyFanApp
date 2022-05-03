package com.konovus.simplemortyr.di

import android.app.Application
import androidx.room.Room
import com.konovus.simplemortyr.api.MortyApi
import com.konovus.simplemortyr.data.MortyDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =  Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()


    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(MortyApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideMortyApi(retrofit: Retrofit): MortyApi =
        retrofit.create(MortyApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MortyDatabase =
        Room.databaseBuilder(app, MortyDatabase::class.java, "characters_db")
            .fallbackToDestructiveMigration()
            .build()


    @Provides
    fun provideMortyDao(db: MortyDatabase) = db.mortyDao()
}