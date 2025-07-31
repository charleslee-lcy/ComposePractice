package cn.thecover.media.core.widget.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cn.thecover.media.core.widget.icon.YBIcons

/**
 *  Created by Wing at 10:02 on 2025/7/31
 *  普通弹窗封装
 */

@Composable
fun YBDialog(
    dialogState: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    title: String? = null,
    content: @Composable () -> Unit,

) {
    if(dialogState.value){
        Dialog(
            onDismissRequest = onDismissRequest,
            content = {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                    modifier = Modifier
                        .padding()
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.weight(1f))
                            if (title != null) {
                                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(imageVector = YBIcons.Close, "关闭弹窗", modifier = Modifier.size(24.dp).clickable{
                               dialogState.value=false
                            })
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        content()
                    }
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }

}


 