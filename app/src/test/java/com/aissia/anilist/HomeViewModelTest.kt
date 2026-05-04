package com.aissia.anilist

import com.aissia.anilist.domain.model.HomeSections
import com.aissia.anilist.presentation.UiState
import com.aissia.anilist.presentation.home.HomeContract
import com.aissia.anilist.presentation.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepo: FakeAnimeRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeAnimeRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        fakeRepo.homeSectionsResult = Result.success(
            HomeSections(nowShowing = emptyList(), popular = emptyList())
        )
        // Before construction, the default state is Loading
        // This verifies the contract's default
        val defaultState = HomeContract.State()
        assertTrue(defaultState.homeSections is UiState.Loading)
    }

    @Test
    fun `loads sections successfully on init`() = runTest {
        val expected = HomeSections(
            nowShowing = listOf(fakeAnimePreview),
            popular = listOf(fakeAnimePreview),
        )
        fakeRepo.homeSectionsResult = Result.success(expected)

        val viewModel = HomeViewModel(fakeRepo)

        val state = viewModel.state.value
        assertTrue(state.homeSections is UiState.Success)
        assertEquals(expected, (state.homeSections as UiState.Success).data)
    }

    @Test
    fun `sets error state when loading fails`() = runTest {
        fakeRepo.homeSectionsResult = Result.failure(Exception("Network error"))

        val viewModel = HomeViewModel(fakeRepo)

        val state = viewModel.state.value
        assertTrue(state.homeSections is UiState.Error)
        assertEquals("Network error", (state.homeSections as UiState.Error).message)
    }

    @Test
    fun `retry reloads sections after failure`() = runTest {
        fakeRepo.homeSectionsResult = Result.failure(Exception("error"))
        val viewModel = HomeViewModel(fakeRepo)
        assertTrue(viewModel.state.value.homeSections is UiState.Error)

        val expected = HomeSections(nowShowing = listOf(fakeAnimePreview), popular = emptyList())
        fakeRepo.homeSectionsResult = Result.success(expected)
        viewModel.onEvent(HomeContract.Event.Retry)

        val state = viewModel.state.value
        assertTrue(state.homeSections is UiState.Success)
        assertEquals(expected, (state.homeSections as UiState.Success).data)
    }

    @Test
    fun `AnimeClicked sends NavigateToDetail effect`() = runTest {
        val viewModel = HomeViewModel(fakeRepo)

        viewModel.onEvent(HomeContract.Event.AnimeClicked(animeId = 42))

        val received = viewModel.effect.first()
        assertTrue(received is HomeContract.Effect.NavigateToDetail)
        assertEquals(42, (received as HomeContract.Effect.NavigateToDetail).animeId)
    }
}
