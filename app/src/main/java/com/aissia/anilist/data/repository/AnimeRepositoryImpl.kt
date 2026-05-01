package com.aissia.anilist.data.repository

import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.Trailer
import com.aissia.anilist.domain.repository.AnimeRepository
import com.aissia.anilist.graphql.GetAnimeDetailQuery
import com.aissia.anilist.graphql.GetPopularAnimeQuery
import com.aissia.anilist.graphql.GetTrendingAnimeQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : AnimeRepository {

    override suspend fun getTrendingAnime(page: Int, perPage: Int): Result<List<Anime>> = runCatching {
        val response = apolloClient.query(
            GetTrendingAnimeQuery(
                page = Optional.Present(page),
                perPage = Optional.Present(perPage)
            )
        ).execute()
        response.data?.Page?.media?.filterNotNull()?.map { it.toAnime() } ?: emptyList()
    }

    override suspend fun getPopularAnime(page: Int, perPage: Int): Result<Pair<List<Anime>, Boolean>> = runCatching {
        val response = apolloClient.query(
            GetPopularAnimeQuery(
                page = Optional.Present(page),
                perPage = Optional.Present(perPage)
            )
        ).execute()
        val pageData = response.data?.Page
        val list = pageData?.media?.filterNotNull()?.map { it.toAnime() } ?: emptyList()
        val hasNextPage = pageData?.pageInfo?.hasNextPage ?: false
        Pair(list, hasNextPage)
    }

    override suspend fun getAnimeDetail(id: Int): Result<Anime> = runCatching {
        val response = apolloClient.query(
            GetAnimeDetailQuery(id = Optional.Present(id))
        ).execute()
        response.data?.Media?.toAnime() ?: error("Anime not found")
    }

    private fun GetTrendingAnimeQuery.Medium.toAnime() = Anime(
        id = id,
        title = title?.english ?: title?.romaji ?: "Unknown",
        coverImageLarge = coverImage?.large,
        coverImageExtraLarge = coverImage?.extraLarge,
        coverImageColor = coverImage?.color,
        bannerImage = bannerImage,
        averageScore = averageScore?.div(10.0),
        popularity = popularity,
        genres = genres?.filterNotNull() ?: emptyList(),
        description = description?.stripHtml(),
        trailer = trailer?.toTrailer(),
        status = status?.rawValue,
        seasonYear = seasonYear
    )

    private fun GetPopularAnimeQuery.Medium.toAnime() = Anime(
        id = id,
        title = title?.english ?: title?.romaji ?: "Unknown",
        coverImageLarge = coverImage?.large,
        coverImageExtraLarge = coverImage?.extraLarge,
        coverImageColor = coverImage?.color,
        bannerImage = bannerImage,
        averageScore = averageScore?.div(10.0),
        popularity = popularity,
        genres = genres?.filterNotNull() ?: emptyList(),
        description = description?.stripHtml(),
        trailer = trailer?.toTrailer(),
        status = status?.rawValue,
        seasonYear = seasonYear
    )

    private fun GetAnimeDetailQuery.Media.toAnime() = Anime(
        id = id,
        title = title?.english ?: title?.romaji ?: "Unknown",
        coverImageLarge = coverImage?.large,
        coverImageExtraLarge = coverImage?.extraLarge,
        coverImageColor = coverImage?.color,
        bannerImage = bannerImage,
        averageScore = averageScore?.div(10.0),
        popularity = popularity,
        genres = genres?.filterNotNull() ?: emptyList(),
        description = description?.stripHtml(),
        trailer = trailer?.toTrailer(),
        status = status?.rawValue,
        seasonYear = seasonYear
    )

    private fun GetTrendingAnimeQuery.Trailer.toTrailer() =
        Trailer(id = id ?: "", site = site ?: "youtube", thumbnail = thumbnail)

    private fun GetPopularAnimeQuery.Trailer.toTrailer() =
        Trailer(id = id ?: "", site = site ?: "youtube", thumbnail = thumbnail)

    private fun GetAnimeDetailQuery.Trailer.toTrailer() =
        Trailer(id = id ?: "", site = site ?: "youtube", thumbnail = thumbnail)

    private fun String.stripHtml(): String = replace(Regex("<[^>]++>"), "").trim()
}
