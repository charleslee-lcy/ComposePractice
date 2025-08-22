package cn.thecover.media.feature.review_data

import cn.thecover.media.feature.review_data.repository.ReviewDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import retrofit2.Retrofit
import javax.inject.Singleton

/**
 *  Created by Wing at 10:11 on 2025/8/22
 *
 */

@Module
@InstallIn(SingletonComponent::class)  // 确保使用正确的组件
object RepositoryModule {
    @Provides
    @Singleton
    fun provideReviewDataRepository(
        reviewApiService: ReviewDataApiService
    ): ReviewDataRepository {
        return ReviewDataRepository(reviewApiService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ReviewDataNetworkModule {
    @Provides
    @Singleton
    fun provideReviewDataApiService(
        retrofit: Retrofit
    ): ReviewDataApiService {
        return retrofit.create(ReviewDataApiService::class.java)
    }
}