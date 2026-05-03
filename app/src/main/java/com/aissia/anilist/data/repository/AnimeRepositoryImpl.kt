package com.aissia.anilist.data.repository

import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.AnimePreview
import com.aissia.anilist.domain.model.Character
import com.aissia.anilist.domain.model.HomeSections
import com.aissia.anilist.domain.model.MediaStatus
import com.aissia.anilist.domain.model.PaginatedResult
import com.aissia.anilist.domain.model.Trailer
import com.aissia.anilist.domain.repository.AnimeRepository
import com.aissia.anilist.graphql.GetAnimeDetailQuery
import com.aissia.anilist.graphql.GetHomeSectionsQuery
import com.aissia.anilist.graphql.GetNowShowingQuery
import com.aissia.anilist.graphql.GetPopularAnimeQuery
import com.aissia.anilist.graphql.type.MediaStatus as GraphQlMediaStatus
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

    // --- Home data mappers ---

    private fun GetHomeSectionsQuery.Medium.toAnimePreview() = AnimePreview(
        id = id,
        title = title?.english ?: title?.romaji ?: "Unknown",
        coverImageLarge = coverImage?.large,
        coverImageExtraLarge = coverImage?.extraLarge,
        coverImageColor = coverImage?.color,
        averageScore = averageScore?.div(10.0),
        genres = genres?.filterNotNull() ?: emptyList(),
        duration = duration,
    )

    private fun GetHomeSectionsQuery.Medium1.toAnimePreview() = AnimePreview(
        id = id,
        title = title?.english ?: title?.romaji ?: "Unknown",
        coverImageLarge = coverImage?.large,
        coverImageExtraLarge = coverImage?.extraLarge,
        coverImageColor = coverImage?.color,
        averageScore = averageScore?.div(10.0),
        genres = genres?.filterNotNull() ?: emptyList(),
        duration = duration,
    )

    // --- List mappers ---

    private fun GetPopularAnimeQuery.Medium.toAnimePreview() = AnimePreview(
        id = id,
        title = title?.english ?: title?.romaji ?: "Unknown",
        coverImageLarge = coverImage?.large,
        coverImageExtraLarge = coverImage?.extraLarge,
        coverImageColor = coverImage?.color,
        averageScore = averageScore?.div(10.0),
        genres = genres?.filterNotNull() ?: emptyList(),
        duration = duration,
    )

    private fun GetNowShowingQuery.Medium.toAnimePreview() = AnimePreview(
        id = id,
        title = title?.english ?: title?.romaji ?: "Unknown",
        coverImageLarge = coverImage?.large,
        coverImageExtraLarge = coverImage?.extraLarge,
        coverImageColor = coverImage?.color,
        averageScore = averageScore?.div(10.0),
        genres = genres?.filterNotNull() ?: emptyList(),
        duration = duration,
    )

    // --- Detail mapper ---

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
        status = status?.toDomainStatus(),
        seasonYear = seasonYear,
        countryOfOrigin = countryOfOrigin,
        characters = characters?.edges?.filterNotNull()?.mapNotNull { edge ->
            val node = edge.node ?: return@mapNotNull null
            Character(
                id = node.id,
                name = node.name?.full ?: return@mapNotNull null,
                imageUrl = node.image?.large,
                role = edge.role?.rawValue,
            )
        } ?: emptyList(),
    )

    private fun GetAnimeDetailQuery.Trailer.toTrailer() =
        Trailer(id = id ?: "", site = site ?: "youtube", thumbnail = thumbnail)

    private fun String.stripHtml(): String = replace(Regex("<[^>]++>"), "").trim()

    private fun GraphQlMediaStatus.toDomainStatus(): MediaStatus = when (this) {
        GraphQlMediaStatus.FINISHED -> MediaStatus.FINISHED
        GraphQlMediaStatus.RELEASING -> MediaStatus.RELEASING
        GraphQlMediaStatus.NOT_YET_RELEASED -> MediaStatus.NOT_YET_RELEASED
        GraphQlMediaStatus.CANCELLED -> MediaStatus.CANCELLED
        GraphQlMediaStatus.HIATUS -> MediaStatus.HIATUS
        GraphQlMediaStatus.UNKNOWN__ -> MediaStatus.UNKNOWN
    }
}
