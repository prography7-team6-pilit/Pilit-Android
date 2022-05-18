package com.prography.pilit.domain.usecase
import com.prography.pilit.domain.repository.PillRepository
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestMonthlyPillUseCase @Inject constructor(
    private val pillRepository: PillRepository
) {
    suspend operator fun invoke(year: Int, month: Int) = pillRepository.requestMonthlyPill(year, month)
}