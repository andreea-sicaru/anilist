package com.aissia.anilist.data

import com.aissia.anilist.domain.model.Anime
import com.aissia.anilist.domain.model.AnimePreview
import com.aissia.anilist.domain.model.Character
import com.aissia.anilist.domain.model.MediaStatus
import com.aissia.anilist.domain.model.Trailer
import com.aissia.anilist.graphql.GetAnimeDetailQuery
import com.aissia.anilist.graphql.GetHomeSectionsQuery
import com.aissia.anilist.graphql.GetNowShowingQuery
import com.aissia.anilist.graphql.GetPopularAnimeQuery
import com.aissia.anilist.graphql.type.MediaStatus as GraphQlMediaStatus

internal fun GetHomeSectionsQuery.Medium.toAnimePreview() = AnimePreview(
    id = id,
    title = title?.english ?: title?.romaji ?: "Unknown",
    coverImageLarge = coverImage?.large,
    coverImageExtraLarge = coverImage?.extraLarge,
    coverImageColor = coverImage?.color,
    averageScore = averageScore?.div(10.0),
    genres = genres?.filterNotNull() ?: emptyList(),
    duration = duration,
)

internal fun GetHomeSectionsQuery.Medium1.toAnimePreview() = AnimePreview(
    id = id,
    title = title?.english ?: title?.romaji ?: "Unknown",
    coverImageLarge = coverImage?.large,
    coverImageExtraLarge = coverImage?.extraLarge,
    coverImageColor = coverImage?.color,
    averageScore = averageScore?.div(10.0),
    genres = genres?.filterNotNull() ?: emptyList(),
    duration = duration,
)

internal fun GetPopularAnimeQuery.Medium.toAnimePreview() = AnimePreview(
    id = id,
    title = title?.english ?: title?.romaji ?: "Unknown",
    coverImageLarge = coverImage?.large,
    coverImageExtraLarge = coverImage?.extraLarge,
    coverImageColor = coverImage?.color,
    averageScore = averageScore?.div(10.0),
    genres = genres?.filterNotNull() ?: emptyList(),
    duration = duration,
)

internal fun GetNowShowingQuery.Medium.toAnimePreview() = AnimePreview(
    id = id,
    title = title?.english ?: title?.romaji ?: "Unknown",
    coverImageLarge = coverImage?.large,
    coverImageExtraLarge = coverImage?.extraLarge,
    coverImageColor = coverImage?.color,
    averageScore = averageScore?.div(10.0),
    genres = genres?.filterNotNull() ?: emptyList(),
    duration = duration,
)

internal fun GetAnimeDetailQuery.Media.toAnime() = Anime(
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
    duration = duration,
    isAdult = isAdult ?: false,
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

internal fun GetAnimeDetailQuery.Trailer.toTrailer() =
    Trailer(id = id ?: "", site = site ?: "youtube", thumbnail = thumbnail)

internal fun GraphQlMediaStatus.toDomainStatus(): MediaStatus = when (this) {
    GraphQlMediaStatus.FINISHED -> MediaStatus.FINISHED
    GraphQlMediaStatus.RELEASING -> MediaStatus.RELEASING
    GraphQlMediaStatus.NOT_YET_RELEASED -> MediaStatus.NOT_YET_RELEASED
    GraphQlMediaStatus.CANCELLED -> MediaStatus.CANCELLED
    GraphQlMediaStatus.HIATUS -> MediaStatus.HIATUS
    GraphQlMediaStatus.UNKNOWN__ -> MediaStatus.UNKNOWN
}

private fun String.stripHtml(): String = replace(Regex("<[^>]++>"), "").trim()
