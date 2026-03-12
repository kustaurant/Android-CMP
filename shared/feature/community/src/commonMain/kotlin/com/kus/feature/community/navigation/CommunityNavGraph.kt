package com.kus.feature.community.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kus.core.serialization.KusJson
import com.kus.core.serialization.RouteCodec
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_LIST_REFRESH
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_DELETE_ID
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_EDIT_RESULT
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_UPDATE_PAYLOAD
import com.kus.feature.community.model.CommunityPostModifyPayload
import com.kus.feature.community.ui.CommunityScreen
import com.kus.feature.community.ui.CommunityViewModel
import com.kus.feature.community.ui.detail.CommunityDetailScreen
import com.kus.feature.community.ui.detail.CommunityDetailViewModel
import com.kus.feature.community.ui.write.CommunityEditorRenderer
import com.kus.feature.community.ui.write.CommunityWriteScreen
import com.kus.feature.community.ui.write.image.PlatformImagePickerFactory
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object Community

@Serializable
data class CommunityDetail(val postId: Long)

@Serializable
data object CommunityWrite

@Serializable
data class CommunityWriteModify(
    val encoded: String,
)

fun NavController.navigateToCommunityDetail(
    postId: Long,
    navOptions: NavOptions?=null,
) = navigate(CommunityDetail(postId), navOptions)

fun NavGraphBuilder.communityNavGraph(
    onShowMessage: (String) -> Unit,
    onBackButtonClick: () -> Unit,
    onPostClick: (Long) -> Unit,
    onPostCreated: (Long) -> Unit,
    onPostModified: (CommunityPostModifyPayload) -> Unit,
    onPostWriteClick: () -> Unit,
    onSearchClick: () -> Unit,
    onPostModifyClick: (String) -> Unit,
    onPostModifiedInDetail: (CommunityPostModifyPayload) -> Unit,
    onDetailBackClick: (CommunityPostModifyPayload?) -> Unit,
    onPostDeletedInDetail: (Long) -> Unit,
) {
    composable<Community> { backStackEntry ->
        val viewModel: CommunityViewModel = koinViewModel()

        val shouldRefresh by backStackEntry.savedStateHandle
            .getStateFlow(COMMUNITY_LIST_REFRESH, false)
            .collectAsStateWithLifecycle()

        LaunchedEffect(shouldRefresh) {
            if (shouldRefresh) {
                backStackEntry.savedStateHandle[COMMUNITY_LIST_REFRESH] = false
                viewModel.fetchFirstPosts()
            }
        }

        val updatePayloadJson by backStackEntry.savedStateHandle
            .getStateFlow<String?>(COMMUNITY_POST_UPDATE_PAYLOAD, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(updatePayloadJson) {
            val json = updatePayloadJson ?: return@LaunchedEffect
            val payload = KusJson.json.decodeFromString(
                CommunityPostModifyPayload.serializer(), json
            )
            viewModel.updatePost(payload)
            backStackEntry.savedStateHandle.remove<String>(COMMUNITY_POST_UPDATE_PAYLOAD)
        }

        val deletedPostId by backStackEntry.savedStateHandle
            .getStateFlow<Long?>(COMMUNITY_POST_DELETE_ID, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(deletedPostId) {
            val postId = deletedPostId ?: return@LaunchedEffect
            viewModel.deletePost(postId)
            backStackEntry.savedStateHandle.remove<Long>(COMMUNITY_POST_DELETE_ID)
        }

        CommunityScreen(
            onPostClick = onPostClick,
            onWriteClick = onPostWriteClick,
            onBackClick = onBackButtonClick,
            onSearchClick = onSearchClick,
            onShowMessage = onShowMessage,
        )
    }

    composable<CommunityWrite> {
        val editorRenderer: CommunityEditorRenderer = koinInject()
        val imagePickerFactory: PlatformImagePickerFactory = koinInject()
        val imagePicker = imagePickerFactory.rememberPicker()

        CommunityWriteScreen(
            initial = null,
            onBackClick = onBackButtonClick,
            onFinishCreate = onPostCreated,
            onFinishModify = {},
            onShowMessage = onShowMessage,
            editorRenderer = editorRenderer,
            imagePicker = imagePicker
        )
    }

    composable<CommunityWriteModify> { backStackEntry ->
        val args = backStackEntry.toRoute<CommunityWriteModify>()

        val payloadJson = RouteCodec.decode(args.encoded)
        val payload = KusJson.json.decodeFromString<CommunityPostModifyPayload>(payloadJson)

        val editorRenderer: CommunityEditorRenderer = koinInject()
        val imagePickerFactory: PlatformImagePickerFactory = koinInject()
        val imagePicker = imagePickerFactory.rememberPicker()

        CommunityWriteScreen(
            initial = payload,
            onBackClick = onBackButtonClick,
            onFinishCreate = {},
            onFinishModify = onPostModified,
            onShowMessage = onShowMessage,
            editorRenderer = editorRenderer,
            imagePicker = imagePicker
        )
    }

    composable<CommunityDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<CommunityDetail>()
        val viewModel: CommunityDetailViewModel = koinViewModel()

        val editJson by backStackEntry.savedStateHandle
            .getStateFlow<String?>(COMMUNITY_POST_EDIT_RESULT, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(editJson) {
            val json = editJson ?: return@LaunchedEffect

            val payload = KusJson.json.decodeFromString(
                CommunityPostModifyPayload.serializer(),
                json
            )

            if (payload.postId == args.postId) {
                viewModel.applyEditResult(payload)
            }

            backStackEntry.savedStateHandle.remove<String>(COMMUNITY_POST_EDIT_RESULT)
            onPostModifiedInDetail(payload)
        }

        CommunityDetailScreen(
            postId = args.postId,
            onBackButtonClick = { onDetailBackClick(viewModel.currentPayload()) },
            onPostModifyClick = onPostModifyClick,
            onPostDeleted = { onPostDeletedInDetail(args.postId) },
            onShowMessage = onShowMessage
        )
    }
}