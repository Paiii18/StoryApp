package com.example.submission1storyapp.setting

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.submission1storyapp.data.database.Entities
import com.example.submission1storyapp.data.database.Remotekeys
import com.example.submission1storyapp.data.database.StoryDatabase
import com.example.submission1storyapp.data.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class RemoteMediaStory(
    private val token: String,
    private val Database: StoryDatabase,
    private val Apiservice: ApiService,
) : RemoteMediator<Int, Entities>() {

    override suspend fun initialize(): RemoteMediator.InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Entities>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            Log.i("RemoteMediatorStory", "load: $token")
            val responseData =
                Apiservice.getStory("Bearer $token", page, state.config.pageSize).listStory
            Database.withTransaction {
                if (LoadType.REFRESH == loadType) {
                    Database.remoteKeysDao().deleteRemoteKeys()
                    Database.storyDao().deleteAllStory()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (LoadState.Loading.endOfPaginationReached) null else page + 1
                val keys = responseData?.map {
                    Remotekeys(
                        id = it?.id!!,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                val newList = responseData?.map {
                    Entities(
                        idStory = it?.id!!, photoUrl = it.photoUrl!!, createdAt = it.createdAt!!,
                        sender = it.name!!,
                        description = it.description!!,
                        lat = it.lat?.toDouble() ?: 0.0,
                        lon = it.lon?.toDouble() ?: 0.0
                    )
                }

                Database.remoteKeysDao().insertAll(keys!!)
                Database.storyDao().insertStory(newList!!)

            }
            return MediatorResult.Success(endOfPaginationReached = LoadState.Loading.endOfPaginationReached)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d("TAG", "load: ${ex.message}")
            return MediatorResult.Error(ex)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Entities>): Remotekeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            Database.remoteKeysDao().getRemoteKeysId(data.idStory!!)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Entities>): Remotekeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            Database.remoteKeysDao().getRemoteKeysId(data.idStory!!)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Entities>): Remotekeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.idStory?.let { id ->
                Database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}