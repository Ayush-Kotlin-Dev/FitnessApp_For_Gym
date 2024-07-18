package domain.usecases.userinfo

import data.models.UserInfoData
import data.models.UserInfoResponseData
import data.userinfo.domain.UserInfoRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.Result

class SubmitUserInfoUseCase : KoinComponent {
    private val repository: UserInfoRepository by inject()

    suspend operator fun invoke(
        userInfoData: UserInfoData
    ) : Result<UserInfoResponseData> {
        return repository.addUserInfo(userInfoData)
    }
}