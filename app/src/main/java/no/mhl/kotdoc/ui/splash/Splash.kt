package no.mhl.kotdoc.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.mhl.kotdoc.R
import java.util.concurrent.TimeUnit

// region Main Content
@Composable
fun Splash(
    splashComplete: () -> Unit
) {
    startScreenDelay(splashComplete)

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colors.surface)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.h4
            )
        }
    }
}
// endregion

// region Initial Entry
private fun startScreenDelay(
    splashComplete: () -> Unit
) = CoroutineScope(Dispatchers.Main).launch {
    delay(TimeUnit.SECONDS.toMillis(2))
    splashComplete()
}
// endregion