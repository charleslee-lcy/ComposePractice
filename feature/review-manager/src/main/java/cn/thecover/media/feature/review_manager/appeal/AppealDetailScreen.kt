package cn.thecover.media.feature.review_manager.appeal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cn.thecover.media.core.data.AppealListData
import cn.thecover.media.core.data.AuditFlow
import cn.thecover.media.core.data.Material
import cn.thecover.media.core.network.BaseUiState
import cn.thecover.media.core.network.HttpStatus
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.GradientLeftBottom
import cn.thecover.media.core.widget.GradientLeftTop
import cn.thecover.media.core.widget.YBShape
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.component.YBInput
import cn.thecover.media.core.widget.component.YBLabel
import cn.thecover.media.core.widget.component.YBTitleBar
import cn.thecover.media.core.widget.component.popup.YBDialog
import cn.thecover.media.core.widget.component.popup.YBLoadingDialog
import cn.thecover.media.core.widget.state.rememberTipsDialogState
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.MsgColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import cn.thecover.media.feature.review_manager.ReviewManageViewModel
import kotlinx.serialization.json.Json

/**
 * 申诉详情
 * <p> Created by CharlesLee on 2025/8/6
 * 15708478830@163.com
 */
@Composable
internal fun AppealDetailRoute(
    modifier: Modifier = Modifier,
    appealId: Long,
    canEdit: Boolean,
    navController: NavController,
) {
    AppealDetailScreen(modifier, appealId, canEdit, navController)
}

@Composable
fun AppealDetailScreen(
    modifier: Modifier = Modifier,
    appealId: Long,
    canEdit: Boolean,
    navController: NavController,
    viewModel: ReviewManageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val showApprovalDialog = remember { mutableStateOf(false) }
    val showRejectDialog = remember { mutableStateOf(false) }
    val detailInfoStatus by viewModel.appealDetailUiState.collectAsStateWithLifecycle()
    val auditEnableStatus by viewModel.auditEnableState.collectAsStateWithLifecycle()
    val auditDetailStatus by viewModel.auditDetailUiState.collectAsStateWithLifecycle()
    var attachments by remember { mutableStateOf(listOf<Material>()) }
    val loadingState = rememberTipsDialogState()

    LaunchedEffect(Unit) {
        viewModel.getAppealDetailInfo(appealId)
    }

    LaunchedEffect(detailInfoStatus) {
        when (detailInfoStatus.status) {
            HttpStatus.LOADING -> {
                loadingState.show()
            }
            HttpStatus.SUCCESS -> {
                loadingState.hide()
                if (detailInfoStatus.data == null) {
                    Toast.makeText(
                        context,
                        detailInfoStatus.errorMsg.ifEmpty { "获取申诉详情失败" },
                        Toast.LENGTH_SHORT
                    ).show()
                }

                detailInfoStatus.data?.material?.let {
                    try {
                        if (it.isNotEmpty()) {
                            attachments = Json.decodeFromString<List<Material>>(it)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
            HttpStatus.ERROR -> {
                loadingState.hide()
                Toast.makeText(context, detailInfoStatus.errorMsg, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    LaunchedEffect(auditDetailStatus) {
        when (auditDetailStatus.status) {
            HttpStatus.LOADING -> {
                loadingState.show()
            }
            HttpStatus.SUCCESS -> {
                loadingState.hide()
                Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            HttpStatus.ERROR -> {
                loadingState.hide()
                Toast.makeText(
                    context,
                    auditDetailStatus.errorMsg.ifEmpty { "操作失败" },
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    LaunchedEffect(auditEnableStatus) {
        when (auditEnableStatus.status) {
            HttpStatus.SUCCESS -> {
                auditEnableStatus.data?.takeIf {
                    it.value == 1 && it.inEffect
                }?.let {
                    if (it.operation == 2) {
                        showApprovalDialog.value = true
                    }
                    if (it.operation == 4) {
                        showRejectDialog.value = true
                    }
                }
            }
            HttpStatus.ERROR -> {
                Toast.makeText(
                    context,
                    auditEnableStatus.errorMsg.ifEmpty { "操作失败" },
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
        )
        YBTitleBar(title = "申诉详情", leftOnClick = {
            navController.popBackStack()
        })
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 15.dp),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    YBLabel(
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                        label = {
                            Text(
                                text = "申诉信息",
                                color = MainTextColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        space = 5.dp,
                        leadingIcon = {
                            YBShape(
                                modifier = Modifier.size(6.dp, 16.dp),
                                colors = listOf(MainColor, Color.Transparent),
                                start = GradientLeftTop,
                                end = GradientLeftBottom
                            )
                        })
                    YBLabel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 12.dp),
                        leadingIcon = {
                            Text(
                                modifier = Modifier.weight(0.24f),
                                text = "申诉人：",
                                color = SecondaryTextColor,
                                fontSize = 14.sp
                            )
                        },
                        label = {
                            Text(
                                modifier = Modifier.weight(0.76f),
                                text = detailInfoStatus.data?.creatorName.orEmpty()
                                    .ifEmpty { "--" },
                                color = MainTextColor,
                                fontSize = 14.sp
                            )
                        })
                    YBLabel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 4.dp),
                        leadingIcon = {
                            Text(
                                modifier = Modifier.weight(0.24f),
                                text = "申诉类型：",
                                color = SecondaryTextColor,
                                fontSize = 14.sp
                            )
                        },
                        label = {
                            Text(
                                modifier = Modifier.weight(0.76f),
                                text = detailInfoStatus.data?.typeName.orEmpty().ifEmpty { "--" },
                                color = MainTextColor,
                                fontSize = 14.sp
                            )
                        })
                    YBLabel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 4.dp),
                        leadingIcon = {
                            Text(
                                modifier = Modifier.weight(0.24f),
                                text = "申诉对象：",
                                color = SecondaryTextColor,
                                fontSize = 14.sp
                            )
                        },
                        label = {
                            Text(
                                modifier = Modifier.weight(0.76f),
                                text = detailInfoStatus.data?.appealTitle.orEmpty()
                                    .ifEmpty { "--" },
                                color = MainTextColor,
                                fontSize = 14.sp
                            )
                        },
                        verticalAlignment = Alignment.Top
                    )
                    YBLabel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 4.dp),
                        leadingIcon = {
                            Text(
                                modifier = Modifier.weight(0.24f),
                                text = "申诉内容：",
                                color = SecondaryTextColor,
                                fontSize = 14.sp
                            )
                        },
                        label = {
                            Text(
                                modifier = Modifier.weight(0.76f),
                                text = detailInfoStatus.data?.content.orEmpty().ifEmpty { "--" },
                                color = MainTextColor,
                                fontSize = 14.sp
                            )
                        },
                        verticalAlignment = Alignment.Top
                    )
                    YBLabel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 12.dp),
                        leadingIcon = {
                            Text(
                                modifier = Modifier.weight(0.24f),
                                text = "证明材料：",
                                color = SecondaryTextColor,
                                fontSize = 14.sp
                            )
                        },
                        label = {
                            Column(modifier = Modifier.weight(0.76f)) {
                                if (attachments.isEmpty()) {
                                    Text(
                                        text = "--",
                                        color = MainTextColor,
                                        fontSize = 14.sp
                                    )
                                } else {
                                    attachments.forEach { item ->
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = item.fileName,
                                            color = MainTextColor,
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.MiddleEllipsis
                                        )
                                    }
                                }
                            }
                        },
                        verticalAlignment = Alignment.Top
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    YBLabel(
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                        label = {
                            Text(
                                text = "审批流程",
                                color = MainTextColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        space = 5.dp,
                        leadingIcon = {
                            YBShape(
                                modifier = Modifier.size(6.dp, 16.dp),
                                colors = listOf(MainColor, Color.Transparent),
                                start = GradientLeftTop,
                                end = GradientLeftBottom
                            )
                        })

                    ApprovalProcessContent(detailInfoStatus)
                }

                if (!detailInfoStatus.data?.reply.isNullOrEmpty()) {
                    Card(
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        YBLabel(
                            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                            label = {
                                Text(
                                    text = "申诉回复",
                                    color = MainTextColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            space = 5.dp,
                            leadingIcon = {
                                YBShape(
                                    modifier = Modifier.size(6.dp, 16.dp),
                                    colors = listOf(MainColor, Color.Transparent),
                                    start = GradientLeftTop,
                                    end = GradientLeftBottom
                                )
                            })
                        YBLabel(
                            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                            leadingIcon = {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = handleReplyUser(detailInfoStatus.data?.auditFlows),
                                    color = MainTextColor,
                                    fontSize = 14.sp
                                )
                            },
                            label = {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = "已回复",
                                    color = MainTextColor,
                                    fontSize = 14.sp
                                )
                            },
                            trailingIcon = {
                                Text(
                                    modifier = Modifier.padding(end = 12.dp),
                                    text = "2025-06-24 09:32:52",
                                    color = MainTextColor,
                                    fontSize = 14.sp
                                )
                            }
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp, top = 10.dp)
                                .fillMaxWidth(),
                            text = "回复内容：",
                            color = SecondaryTextColor,
                            fontSize = 14.sp
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 12.dp)
                                .fillMaxWidth(),
                            text = detailInfoStatus.data?.reply.orEmpty().ifEmpty { "--" },
                            color = MainTextColor,
                            fontSize = 14.sp
                        )
                    }
                }

            }

            if (canEdit /*&& detailInfoStatus.data?.status == 1*/) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    YBButton(
                        text = { Text(text = "驳回", fontSize = 16.sp, color = MsgColor) },
                        modifier = modifier
                            .border(
                                width = 0.5.dp,
                                color = MsgColor,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .weight(1f)
                            .height(44.dp),
                        backgroundColor = Color.Transparent,
                        onClick = {
                            viewModel.getAuditEnableInfo(operation = 4)
                        }
                    )
                    YBButton(
                        text = { Text(text = "通过", fontSize = 16.sp) },
                        modifier = modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(2.dp),
                        onClick = {
                            viewModel.getAuditEnableInfo(operation = 2)
                        }
                    )
                }
            }
        }

    }

    YBDialog(
        dialogState = showApprovalDialog,
        onDismissRequest = { showApprovalDialog.value = false },
        title = "申诉审批",
        content = {
            Text(text = "确认通过审批？", fontSize = 16.sp, color = MainTextColor)
        },
        confirmText = "确认",
        onConfirm = {
            viewModel.auditAppealDetailInfo(
                id = appealId,
                operation = 2,
                curNodeId = detailInfoStatus.data?.curNodeId,
                nextNodeId = detailInfoStatus.data?.nextNodeId
            )
        },
        cancelText = "取消",
        onCancel = {
            showApprovalDialog.value = false
        }
    )

    var reasons = ""

    YBDialog(
        dialogState = showRejectDialog,
        onDismissRequest = {
            showRejectDialog.value = false
            reasons = ""
        },
        title = "申诉审批",
        content = {
            Column {
                Text(text = "确认驳回审批？", fontSize = 16.sp, color = MainTextColor)
                Row(
                    modifier = Modifier.padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "*", fontSize = 14.sp, color = MsgColor)
                    Text(text = "驳回意见：", fontSize = 14.sp, color = MainTextColor)
                }
                YBInput(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(
                            PageBackgroundColor
                        ),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = MainTextColor
                    ),
                    hint = "输入意见，不超过200字",
                    hintTextSize = 14.sp,
                    singleLine = false,
                    showCount = true,
                    maxLength = 200,
                    contentPadding = 12.dp,
                    contentAlignment = Alignment.TopStart,
                    onValueChange = {
                        reasons = it
                    }
                )
            }
        },
        confirmText = "确认",
        onConfirm = {
            if (reasons.isEmpty()) {
                Toast.makeText(context, "请输入驳回意见", Toast.LENGTH_SHORT).show()
                return@YBDialog
            }
            viewModel.auditAppealDetailInfo(
                id = appealId,
                operation = 4,
                reasons = reasons,
                curNodeId = detailInfoStatus.data?.curNodeId,
                nextNodeId = detailInfoStatus.data?.nextNodeId
            )
            showRejectDialog.value = false
        },
        cancelText = "取消",
        onCancel = {
            showRejectDialog.value = false
        },
        handleConfirmDismiss = true
    )

    YBLoadingDialog(loadingState, enableDismiss = true, onDismissRequest = { loadingState.hide() })
}

private fun handleReplyUser(flows: List<AuditFlow>?): String {
    var result = "--"
    flows?.forEach {
        if (it.operation == 5) {
            result = it.operatorName
            return@forEach
        }
    }
    return result
}

@Composable
private fun ApprovalProcessContent(detailInfoStatus: BaseUiState<AppealListData>) {
    var count = 0
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(7.dp))
        detailInfoStatus.data?.auditFlows?.filter {
            it.operation != 5
        }?.also {
            count = it.size
        }?.forEachIndexed { index, flow ->
            if (flow.operation != 5) {
                ApprovalFlowItem(
                    node = flow,
                    index = index,
                    count = count
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun ApprovalFlowItem(
    node: AuditFlow,
    index: Int,
    count: Int
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(start = 11.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (index != 0) {
                VerticalDivider(
                    modifier = Modifier
                        .padding(bottom = 3.dp)
                        .height(5.dp),
                    thickness = 1.dp,
                    color = MainColor
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }
            Badge(
                modifier = Modifier
                    .size(8.dp),
                containerColor = MainColor
            )
            if (index != count - 1) {
                VerticalDivider(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .height(if (index == 0) 20.dp else 40.dp),
                    thickness = 1.dp,
                    color = MainColor
                )
            }
        }
        Column {
            if (node.nodeName.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 6.dp),
                    text = node.nodeName,
                    color = SecondaryTextColor,
                    fontSize = 14.sp
                )
            }
            Row {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    text = node.operatorName,
                    color = MainTextColor,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = node.operationName,
                    color = MainTextColor,
                    fontSize = 14.sp
                )
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = node.createTime,
                    color = MainTextColor,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@PhonePreview
@Composable
fun AppealDetailPreview() {
    YBTheme {
        AppealDetailScreen(
            viewModel = ReviewManageViewModel(
                savedStateHandle = SavedStateHandle(),
                retrofit = { previewRetrofit }
            ),
            navController = NavController(LocalContext.current),
            appealId = 1,
            canEdit = false
        )
    }
}


 