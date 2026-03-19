package com.kus.feature.community.ui.write

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.domain.community.model.PostCategory
import com.kus.feature.community.model.CommunityPostModifyPayload
import com.kus.feature.community.ui.write.image.PlatformImagePicker
import kotlinx.coroutines.launch
import kustaurant.shared.core.designsystem.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes
import kustaurant.shared.feature.community.generated.resources.Res
import kustaurant.shared.feature.community.generated.resources.ic_drop_down
import kustaurant.shared.feature.community.generated.resources.ic_undo
import kustaurant.shared.feature.community.generated.resources.ic_insert_image
import kustaurant.shared.feature.community.generated.resources.ic_bold
import kustaurant.shared.feature.community.generated.resources.ic_redo

@Composable
fun CommunityWriteScreen(
    initial: CommunityPostModifyPayload?,
    onBackClick: () -> Unit,
    onFinishCreate: (Long) -> Unit,
    onFinishModify: (CommunityPostModifyPayload) -> Unit,
    onShowMessage: (String) -> Unit,
    editorRenderer: CommunityEditorRenderer,
    imagePicker: PlatformImagePicker,
) {
    val viewModel: CommunityWriteViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val controller = rememberCommunityEditorController()

    LaunchedEffect(initial?.postId) {
        viewModel.init(initial)
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            onShowMessage(it)
            viewModel.clearToast()
        }
    }


    LaunchedEffect(uiState.finishState) {
        when (uiState.finishState) {
            PostFinishState.CREATE_OK -> {
                onShowMessage("게시글이 성공적으로 업로드되었습니다.")
                viewModel.consumeFinishState()
                uiState.editPostId?.let(onFinishCreate)
            }

            PostFinishState.MODIFY_OK -> {
                onShowMessage("게시글이 성공적으로 수정되었습니다.")
                viewModel.consumeFinishState()

                val postId = uiState.editPostId ?: return@LaunchedEffect
                val category = uiState.category ?: return@LaunchedEffect

                onFinishModify(
                    CommunityPostModifyPayload(
                        postId = postId,
                        category = category,
                        title = uiState.title,
                        body = uiState.html,
                        totalLikes = uiState.totalLikes,
                        commentCount = uiState.commentCount
                    )
                )
            }

            PostFinishState.ERR -> viewModel.consumeFinishState()
            PostFinishState.IDLE -> Unit
        }
    }

    LaunchedEffect(uiState.initialHtmlToSet) {
        uiState.initialHtmlToSet?.let { html ->
            controller.setHtml(html)
            viewModel.consumeInitialHtml()
        }
    }

    val scope = rememberCoroutineScope()

    val onPickImageClick = {
        imagePicker.pickImage { imagePath ->
            if (imagePath == null) return@pickImage
            scope.launch {
                val url = viewModel.uploadImageAndGetUrl(imagePath) ?: return@launch
                controller.insertImage(url)
            }
        }
    }

    val titleText = if (uiState.isEditMode) "게시글 수정" else "게시글 작성"
    val canSubmit = uiState.isSendReady && uiState.phase != CommunityWritePhase.Loading

    Scaffold(
        topBar = {
            KusTopBarSlots(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                left = {
                    Icon(
                        painter = painterResource(CoreRes.drawable.ic_arrow_back),
                        contentDescription = "뒤로가기 버튼입니다.",
                        modifier = Modifier.noRippleClickable { onBackClick() }
                    )
                },
                right = {
                    Box(
                        modifier = Modifier
                            .size(width = 54.dp, height = 32.dp)
                            .background(
                                color = if (canSubmit) KusTheme.colors.c_43AB38 else KusTheme.colors.c_AAAAAA,
                                shape = RoundedCornerShape(100.dp)
                            )
                            .noRippleClickable { if (canSubmit) viewModel.submit() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "완료",
                            style = KusTheme.typography.type14r,
                            color = KusTheme.colors.c_FFFFFF,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            ) {
                Text(
                    text = titleText,
                    style = KusTheme.typography.type17sb,
                    color = KusTheme.colors.c_323232
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_FFFFFF),
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(KusTheme.colors.c_FFFFFF)
                .padding(inner)
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            CommunityCategorySelectorPopup(
                selected = uiState.category,
                onSelect = viewModel::onCategoryChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(39.dp))

            BasicTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                singleLine = true,
                textStyle = KusTheme.typography.type17sb.copy(color = KusTheme.colors.c_323232),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(32.dp),
                decorationBox = { innerTextField ->
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (uiState.title.isEmpty()) {
                                Text(
                                    text = "제목을 입력해주세요",
                                    style = KusTheme.typography.type17sb,
                                    color = KusTheme.colors.c_AAAAAA
                                )
                            }
                            innerTextField()
                        }

                        Spacer(Modifier.height(4.dp))

                        HorizontalDivider(
                            modifier = Modifier.height(0.5.dp),
                            color = KusTheme.colors.c_AAAAAA
                        )
                    }
                }
            )

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clipToBounds()
            ) {
                editorRenderer.Render(
                    controller = controller,
                    modifier = Modifier.fillMaxSize(),
                    onHtmlChange = viewModel::onHtmlChange,
                    onEditorReady = viewModel::onEditorReady
                )

                if (uiState.phase == CommunityWritePhase.Loading || uiState.isImageUploading) {
                    Box(Modifier
                        .fillMaxSize()
                        .background(
                            color = KusTheme.colors.c_FFFFFF.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                    ) {
                        KusLoadingAnimation()
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = KusTheme.colors.c_FFFFFF)
            ) {
                CommunityWriteBottomToolbar(
                    onPickImage = onPickImageClick,
                    onUndo = controller::undo,
                    onRedo = controller::redo,
                    onBold = controller::toggleBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun CommunityCategorySelectorPopup(
    selected: PostCategory?,
    onSelect: (PostCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val shape = RoundedCornerShape(14.dp)

    var anchorWidthPx by remember { mutableIntStateOf(0) }
    var iconWidthPx by remember { mutableIntStateOf(0) }
    var anchorHeightPx by remember { mutableIntStateOf(0) }
    val yOffsetDp = with(density) { (-anchorHeightPx).toDp() }
    val spacerPx = with(density) { 6.dp.roundToPx() }
    val menuWidthDp = with(density) {
        (anchorWidthPx - iconWidthPx - spacerPx).toDp()
    }

    Box(modifier) {
        Row(
            modifier = Modifier
                .width(102.dp)
                .onSizeChanged {
                    anchorWidthPx = it.width
                    anchorHeightPx = it.height
                }
                .clip(shape)
                .background(KusTheme.colors.c_FFFFFF)
                .border(1.dp, KusTheme.colors.c_43AB38, shape)
                .noRippleClickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected?.displayName ?: "게시판 선택",
                style = KusTheme.typography.type12m,
                color = KusTheme.colors.c_43AB38,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                painter = painterResource(Res.drawable.ic_drop_down),
                contentDescription = null,
                tint = KusTheme.colors.c_43AB38,
                modifier = Modifier.onSizeChanged {
                    iconWidthPx = it.width
                }
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = yOffsetDp),
            shape = RoundedCornerShape(13.dp),
            containerColor = KusTheme.colors.c_E0E0E0,
            modifier = Modifier
                .then(if (anchorWidthPx > 0) Modifier.width(menuWidthDp) else Modifier),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "게시판 선택",
                    style = KusTheme.typography.type14r,
                    color = KusTheme.colors.c_AAAAAA,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 1.dp)
                )
            }

            listOf(PostCategory.FREE, PostCategory.SUGGESTION, PostCategory.COLUMN).forEach { c ->
                DropdownMenuItem(
                    modifier = Modifier.height(32.dp),
                    text = {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 1.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = c.displayName,
                                style = KusTheme.typography.type14r,
                                color = KusTheme.colors.c_666666
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onSelect(c)
                    },
                )
            }
        }
    }
}

@Composable
private fun CommunityWriteBottomToolbar(
    onPickImage: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onBold: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = Modifier.fillMaxWidth()){
        HorizontalDivider(thickness = 1.dp, color = KusTheme.colors.c_E0E0E0)
        Row(
            modifier = modifier.padding(vertical = 4.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_insert_image),
                contentDescription = "이미지 삽입 아이콘입니다.",
                modifier = Modifier
                    .size(18.dp)
                    .noRippleClickable { onPickImage() })
            Spacer(Modifier.width(18.dp))
            Image(
                painter = painterResource(Res.drawable.ic_undo),
                contentDescription = "뒤로가기 아이콘입니다.",
                modifier = Modifier
                    .size(width = 16.61.dp, height = 14.05.dp)
                    .noRippleClickable { onUndo() })
            Spacer(Modifier.width(18.dp))
            Image(
                painter = painterResource(Res.drawable.ic_redo),
                contentDescription = "되돌리기 아이콘입니다.",
                modifier = Modifier
                    .size(width = 16.61.dp, height = 14.05.dp)
                    .noRippleClickable { onRedo() })
            Spacer(Modifier.width(18.dp))
            Image(
                painter = painterResource(Res.drawable.ic_bold),
                contentDescription = "굵게 처리 아이콘입니다.",
                modifier = Modifier
                    .size(width = 15.dp, height = 14.dp)
                    .noRippleClickable { onBold() })
        }

        HorizontalDivider(thickness = 1.dp, color = KusTheme.colors.c_E0E0E0)
    }

}
