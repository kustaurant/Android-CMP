import com.kus.shared.domain.model.my.MyInfo

interface MyRepository {
    suspend fun getMyInfo(): MyInfo
    suspend fun postFeedback(content: String): String
}
