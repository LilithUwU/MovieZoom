package com.example.moviezoom.presentation.activity

import com.example.moviezoom.domain.model.MoviePage
import com.example.moviezoom.domain.usecase.GetTopRatedMoviesUseCase
import com.example.moviezoom.domain.usecase.SearchMovieUseCase
import com.example.moviezoom.presentation.uistate.ErrorType
import com.example.moviezoom.presentation.uistate.MovieUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val getMovies: GetTopRatedMoviesUseCase = mockk()
    private val search: SearchMovieUseCase = mockk()
    private val vm = MainViewModel(getMovies, search)

    @Before fun setup() = Dispatchers.setMain(UnconfinedTestDispatcher())
    @After fun cleanState() = Dispatchers.resetMain()

    @Test fun `getMovies success case`() {
        coEvery { getMovies(any()) } returns MoviePage(1, emptyList())
        vm.getMovies()
        assert(vm.uiState.value is MovieUiState.Success)
    }

    @Test fun `getMovies network failure case `() {
        coEvery { getMovies(any()) } throws UnknownHostException()
        vm.getMovies()
        val state = vm.uiState.value as MovieUiState.Error
        assert(state.errorType is ErrorType.Network)
    }

    @Test fun `search movies success case`() {
        coEvery { search(any(), any()) } returns MoviePage(1, emptyList())
        vm.search("q")
        assert(vm.uiState.value is MovieUiState.Success)
    }

    @Test fun `search movies failure case`() {
        coEvery { search(any(), any()) } throws Exception("error")
        vm.search("q")
        val state = vm.uiState.value as MovieUiState.Error
        assert(state.errorType is ErrorType.Unknown)
    }
}
