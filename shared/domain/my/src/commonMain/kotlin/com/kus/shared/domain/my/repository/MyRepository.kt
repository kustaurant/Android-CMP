import com.kus.shared.domain.model.my.MyInfo
import com.kus.shared.domain.model.my.ProfileInfo

interface MyRepository {
    suspend fun getMyInfo(): MyInfo
    suspend fun postFeedback(content: String): String
    suspend fun patchProfileInfo(nickname: String?, phoneNumber: String?): ProfileInfo
}
