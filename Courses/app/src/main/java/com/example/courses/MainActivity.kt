package com.example.courses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courses.data.DataSource
import com.example.courses.model.Topic
import com.example.courses.ui.theme.CoursesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoursesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CourseList(topics = DataSource.topics)
                }
            }
        }
    }
}

@Composable
fun CourseList(topics: List<Topic>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
//        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        items(topics.size) { index ->
            TopicCard(topic = topics[index], modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseListPreview() {
    CoursesTheme {
        CourseList(topics = DataSource.topics)
    }
}

@Composable
fun TopicCard(topic: Topic, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(id = topic.imageResourceId),
                contentDescription = topic.stringResourceId.toString(),
                modifier = Modifier
                    .width(68.dp)
                    .height(68.dp)
            )

            Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                Text(
                    text = stringResource(id = topic.stringResourceId),
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_grain),
                        contentDescription = "",
                        modifier = Modifier.padding(end = 8.dp),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )

                    Text(
                        text = topic.good.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopicCardPreview() {
    TopicCard(
        topic = Topic(R.string.photography, 321, R.drawable.photography),
        modifier = Modifier.height(68.dp),
    )
}
