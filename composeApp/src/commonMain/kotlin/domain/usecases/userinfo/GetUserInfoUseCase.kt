package domain.usecases.userinfo

import data.models.UserInfoResponseData
import data.userinfo.domain.UserInfoRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.Result

class GetUserInfoUseCase  : KoinComponent{

    private val repository: UserInfoRepository by inject()

    suspend operator fun invoke(
        userId: Long
    ) : Result<UserInfoResponseData> {
        return repository.getUserInfo(userId)
    }

}