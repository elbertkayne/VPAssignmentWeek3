package com.example.vpassignmentweek3

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelJournalApp() {
    val context = LocalContext.current
    var spotWisata by remember { mutableStateOf("Fjellheisen Cable Car") }
    var pengalaman by remember { mutableStateOf("Pemandangan langit malam hijau aurora spektakuler dari puncak gunung...") } //[cite: 29]
    var catatan by remember { mutableStateOf("") }
    var tingkatKepuasan by remember { mutableStateOf("Luar Biasa") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101820))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("LOG PERJALANAN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60A5FA))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Tromsø, Norway", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("❄️", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Ekspedisi Aurora Borealis", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("SPOT WISATA FAVORIT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = spotWisata,
                    onValueChange = { spotWisata = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B),
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("APA YANG PALING KAMU NIKMATI?", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = pengalaman,
                    onValueChange = { pengalaman = it },
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B),
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("CATATAN TAMBAHAN / PERLENGKAPAN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    placeholder = { Text("(Ketik perlengkapan ekstra di sini)", color = Color.Gray, fontSize = 12.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B),
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tingkat Kepuasan[cite: 29]
                Text("TINGKAT KEPUASAN:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SatisfactionChip(label = "Biasa", isSelected = tingkatKepuasan == "Biasa", onClick = { tingkatKepuasan = "Biasa" })
                    SatisfactionChip(label = "Seru", isSelected = tingkatKepuasan == "Seru", onClick = { tingkatKepuasan = "Seru" })
                    SatisfactionChip(
                        label = "Luar Biasa",
                        isSelected = tingkatKepuasan == "Luar Biasa",
                        hasIcon = true,
                        onClick = { tingkatKepuasan = "Luar Biasa" }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Status: Draft Tersimpan", fontSize = 12.sp, color = Color.Gray)
                FloatingActionButton(
                    onClick = {
                        Toast.makeText(context, "Journal Saved! Spot: $spotWisata", Toast.LENGTH_SHORT).show()
                    },
                    containerColor = Color(0xFF3B82F6),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Journal")
                }
            }
        }
    }
}

@Composable
fun SatisfactionChip(label: String, isSelected: Boolean, hasIcon: Boolean = false, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .background(
                color = if (isSelected) Color(0xFF3B82F6) else Color(0xFF1E293B),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                color = if (isSelected) Color.White else Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            if (hasIcon) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Soal 2 - Travel Journal Preview")
@Composable
fun PreviewTravelJournal() {
    TravelJournalApp()
}