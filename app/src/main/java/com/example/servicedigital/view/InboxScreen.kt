import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.servicedigital.R
import com.example.servicedigital.model.Conversation
import com.example.servicedigital.model.Message

@Composable
fun InboxScreen(navController: NavController) {
    val conversations = remember {
        mutableStateListOf(
            Conversation(
                id = 1,
                contactName = "Juan Pérez",
                avatarRes = R.drawable.fotousuario,
                messages = mutableListOf(
                    Message(1, "them", "Hola — estoy disponible mañana a las 10am.", "10:12"),
                    Message(2, "me", "Perfecto, me sirve esa hora. ¿Dónde nos vemos?", "10:15")
                )
            ),
            Conversation(
                id = 2,
                contactName = "María López",
                avatarRes = R.drawable.fotousuario,
                messages = mutableListOf(
                    Message(1, "them", "Puedo certificar la instalación hoy.", "09:30"),
                    Message(2, "me", "Excelente, ¿a qué hora podrías?", "09:35"),
                    Message(3, "them", "A las 18:00 estaría bien.", "09:40")
                )
            )
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFEFF7FB)) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate("catalog/{nombreUsuario}") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8arrow24),
                        contentDescription = "Volver",
                        tint = Color(0xFF0D47A1),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Bandeja de entrada",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF0D47A1)
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(conversations) { conv ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("chatScreen/${conv.contactName}")
                            },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color.LightGray, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = conv.avatarRes),
                                    contentDescription = conv.contactName,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(conv.contactName, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                val last = conv.messages.lastOrNull()?.text ?: ""
                                Text(
                                    text = if (last.length > 48) last.take(48) + "..." else last,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray
                                )
                            }

                            val lastTime = conv.messages.lastOrNull()?.time ?: ""
                            Text(
                                lastTime,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}