package com.aissia.anilist

import androidx.lifecycle.SavedStateHandle
import com.aissia.anilist.presentation.UiState
import com.aissia.anilist.presentation.detail.DetailContract
import com.aissia.anilist.presentation.detail.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepo: FakeAnimeRepository

    private fun savedStateHandleFor(id: Int) = SavedStateHandle(mapOf("animeId" to id))

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
    fun `loads anime detail successfully on init`() = runTest {
        fakeRepo.animeDetailResult = Result.success(fakeAnime)

        val viewModel = DetailViewModel(fakeRepo, savedStateHandleFor(1))

        val state = viewModel.state.value
        assertTrue(state.detail is UiState.Success)
        assertEquals(fakeAnime, (state.detail as UiState.Success).data)
    }

    @Test
    fun `sets error state when loading fails`() = runTest {
        fakeRepo.animeDetailResult = Result.failure(Exception("Not found"))

        val viewModel = DetailViewModel(fakeRepo, savedStateHandleFor(1))

        val state = viewModel.state.value
        assertTrue(state.detail is UiState.Error)
        assertEquals("Not found", (state.detail as UiState.Error).message)
    }

    @Test
    fun `retry reloads detail after failure`() = runTest {
        fakeRepo.animeDetailResult = Result.failure(Exception("error"))
        val viewModel = DetailViewModel(fakeRepo, savedStateHandleFor(1))
        assertTrue(viewModel.state.value.detail is UiState.Error)

        fakeRepo.animeDetailResult = Result.success(fakeAnime)
        viewModel.onEvent(DetailContract.Event.RetryLoad)

        val state = viewModel.state.value
        assertTrue(state.detail is UiState.Success)
        assertEquals(fakeAnime, (state.detail as UiState.Success).data)
    }

    @Test
    fun `OnBackClicked sends NavigateBack effect`() = runTest {
        val viewModel = DetailViewModel(fakeRepo, savedStateHandleFor(1))

        viewModel.onEvent(DetailContract.Event.OnBackClicked)

        val effect = viewModel.effect.first()
        assertTrue(effect is DetailContract.Effect.NavigateBack)
    }
}