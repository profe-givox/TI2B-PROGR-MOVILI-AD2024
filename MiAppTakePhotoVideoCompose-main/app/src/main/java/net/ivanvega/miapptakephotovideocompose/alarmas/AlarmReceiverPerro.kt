package net.ivanvega.miapptakephotovideocompose.alarmas

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import net.ivanvega.miapptakephotovideocompose.R
import java.time.LocalDateTime
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AlarmasScreen( alarmScheduler: AlarmScheduler){

    val recordAudioPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    var secondText by remember {
        mutableStateOf("")
    }
    var messageText by remember {
        mutableStateOf("")
    }
    var alarmItem : AlarmItem? = null
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = secondText, onValueChange = {
            secondText = it
        },
            label = {
                Text(text = "Delay Second")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = messageText, onValueChange = {
            messageText = it
        },
            label = {
                Text(text = "Message")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                 /*alarmItem =
                    AlarmItem(
                        alarmTime = LocalDateTime.now().plusSeconds(
                            secondText.toLong()
                        ),
                        message = messageText
                    )*/
                 alarmItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AlarmItem(
                        LocalDateTime.now().plusSeconds(secondText.toLong()),
                        "El mensaje"
                    )
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                alarmItem?.let(alarmScheduler::schedule)
                secondText = ""
                messageText = ""
            }) {
                Text(text = "Schedule")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                alarmItem?.let(alarmScheduler::cancel)
            }) {
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

class AlarmReceiverPerro : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        val channelId = "alarm_id"
        context?.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Alarm Demo")
                .setContentText("Notification sent with message $message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(1, builder.build())
        }

    }
}

data class AlarmItem(
    val alarmTime : LocalDateTime,
    val message : String
)

interface AlarmScheduler {
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}

class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler{

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    //private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager

    override fun schedule(alarmItem: AlarmItem) {
        val intent = Intent(context, AlarmReceiverPerro::class.java).apply {
            putExtra("EXTRA_MESSAGE", alarmItem.message)
        }

        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime()+10000,
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.e("Alarm", "Alarm set at ")
    }

    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(context, AlarmReceiverPerro::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}