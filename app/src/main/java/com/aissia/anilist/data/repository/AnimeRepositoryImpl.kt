package com.aissia.anilist.data.repository

import com.aissia.anilist.data.toAnime
import com.aissia.anilist.data.toAnimePreview
import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.AnimePreview
import com.aissia.anilist.domain.model.HomeSections
import com.aissia.anilist.domain.model.PaginatedResult
import com.aissia.anilist.domain.repository.AnimeRepository
import com.aissia.anilist.graphql.GetAnimeDetailQuery
import com.aissia.anilist.graphql.GetHomeSectionsQuery
import com.aissia.anilist.graphql.GetNowShowingQuery
import com.aissia.anilist.graphql.GetPopularAnimeQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : AnimeRepository {

    override suspend fun getHomeSections(): Result<HomeSections> = runCatching {
        val response = apolloClient.query(GetHomeSectionsQuery()).execute()
        response.exception?.let { throw it }
        HomeSections(
            nowShowing = response.data?.nowShowing?.media
                ?.filterNotNull()?.map { it.toAnimePreview() } ?: emptyList(),
            popular = response.data?.popular?.media
                ?.filterNotNull()?.map { it.toAnimePreview() } ?: emptyList(),
        )
    }

    override suspend fun getPopularAnime(page: Int, perPage: Int): Result<PaginatedResult<AnimePreview>> = runCatching {
        val response = apolloClient.query(
            GetPopularAnimeQuery(
                page = Optional.Present(page),
                perPage = Optional.Present(perPage)
            )
        ).execute()
        response.exception?.let { throw it }
        val pageData = response.data?.Page
        PaginatedResult(
            items = pageData?.media?.filterNotNull()?.map { it.toAnimePreview() } ?: emptyList(),
            hasNextPage = pageData?.pageInfo?.hasNextPage ?: false,
        )
    }

    override suspend fun getNowShowingAnime(page: Int, perPage: Int): Result<PaginatedResult<AnimePreview>> = runCatching {
        val response = apolloClient.query(
            GetNowShowingQuery(
                page = Optional.Present(page),
                perPage = Optional.Present(perPage)
            )
        ).execute()
        response.exception?.let { throw it }
        val pageData = response.data?.Page
        PaginatedResult(
            items = pageData?.media?.filterNotNull()?.map { it.toAnimePreview() } ?: emptyList(),
            hasNextPage = pageData?.pageInfo?.hasNextPage ?: false,
        )
    }

    override suspend fun getAnimeDetail(id: Int): Result<Anime> = runCatching {
        val response = apolloClient.query(
            GetAnimeDetailQuery(id = Optional.Present(id))
        ).execute()
        response.exception?.let { throw it }
        response.data?.Media?.toAnime() ?: error("Anime not found")
    }
}
