package net.ivanvega.miholamundoo

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import net.ivanvega.miholamundoo.ui.theme.MiHolaMundooTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiHolaMundooTheme {
                MyApp( modifier = Modifier.fillMaxSize())
            }
        }
        Log.d("ciclovida", "Paso por onCretate")
    }

    override fun onStart() {
        super.onStart()
        Log.d("ciclovida", "Paso por onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ciclovida", "Paso por onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ciclovida", "Paso por onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ciclovida", "Paso por onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("ciclovida", "Paso por onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ciclovida", "Paso por onDestroy")
    }
}

@Composable
fun  Greetings(modifier: Modifier = Modifier,
               names : List<String> = List(1000,  init = {  "$it"  })
               ){

    LazyColumn {
        items(items = names){ indice ->
            Greeting(name = indice)
        }
    }

}

@Composable
fun Onboardingscreen( modifier: Modifier = Modifier,
                      onContinueClick : () -> Unit
                      ){
    Column (modifier = Modifier.padding(top = 48.dp)) {
        Text(text = stringResource(R.string.welcome_to_the_basics_compose) )
        Button(onClick = onContinueClick) {
            Text(text = "Continue")
        }
    }
}

@Preview
@Composable
fun PreviewOnboardingscreen(){
    MiHolaMundooTheme {
        //Onboardingscreen()
    }
}

@Composable
fun MyApp(modifier: Modifier =  Modifier,
          names : List<String> = listOf("Mundo","Android", "Compose", "Mundo","Android", "Compose","Mundo","Android", "Compose","Mundo","Android", "Compose")
){
    val sholudShowOnBoarding: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(true)
    }
    Surface (
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {

        if (sholudShowOnBoarding.value){
            Onboardingscreen(onContinueClick = { sholudShowOnBoarding.value = false })
        }else{
            /*Column (modifier = modifier.padding(vertical = 4.dp)) {
                for (  name in names  ){
                    Greeting(name = name)
                }
            }*/
            Greetings()
        }

    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    /*Surface( content = {

        })*/
    //var expanded = false
    var expanded = remember {
         mutableStateOf(false)
    }
    val extraPadding by
        /*animateDpAsState(
            targetValue = if (expanded.value) 48.dp else 0.dp,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow)

        )*/
    animateDpAsState(
        targetValue = if (expanded.value) 48.dp else 0.dp,
        animationSpec = tween(durationMillis = 1000, 500, FastOutLinearInEasing)

    )
    Surface (color = MaterialTheme .colorScheme.primary
        , modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row (modifier = Modifier
            .padding(24.dp)
            .padding(bottom = extraPadding.coerceAtLeast(0.dp))) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Hello ",
                )
                Text(
                    text = "$name!",
                    style= MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                )
                if(expanded.value){
                    Text(text = (" COmposer ipsum color sit lazy, " +
                                    "pading theme elit. sed do buncy".repeat(4)
                            )
                    )
                }
            }
            val contentDescription = if (expanded.value) stringResource(R.string.show_less) else stringResource(
                R.string.show_more
            )
            IconButton(onClick = { expanded.value = !expanded.value }) {
                Icon(imageVector = if (expanded.value)  Icons.Filled.ExpandLess else Icons.Filled.ExpandMore  ,
                    contentDescription = contentDescription)
            }
            /*ElevatedButton(onClick =
                {  expanded.value = !expanded.value   }
            ) {
                Text(text =  if (expanded.value) "Show less" else "Show more"   )
            }*/
        }
    }
}


@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Greeetings Obscuro"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    MiHolaMundooTheme {
        Greetings()
    }
}