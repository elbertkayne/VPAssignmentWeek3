package com.arielelbert.vpassignmentweek3

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CoffeeOrderApp() {
    val context = LocalContext.current
    var selectedSize by remember { mutableStateOf("Regular") }
    var quantity by remember { mutableIntStateOf(3) }
    val basePrice = 25000
    val sizeExtra = if (selectedSize == "Large") 6000 else 0
    val unitPrice = basePrice + sizeExtra
    val subtotal = unitPrice * quantity
    val tax = (subtotal * 0.1).toInt()
    val totalBill = subtotal + tax
    val rupiahFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F6F0))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Kopi Kenangan Senja", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4E2A1E))
                    Text("☕", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(Color(0xFF8B4513), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("☕", fontSize = 48.sp, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Caramel Latte", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Espresso shot, steamed milk & caramel syrup", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Rp 25.000 / cup", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD2691E))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("PILIHAN UKURAN:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SizeOptionButton(
                        text = "Regular (+0)",
                        isSelected = selectedSize == "Regular",
                        modifier = Modifier.weight(1f),
                        onClick = { selectedSize = "Regular" }
                    )
                    SizeOptionButton(
                        text = "Large (+6k)",
                        isSelected = selectedSize == "Large",
                        modifier = Modifier.weight(1f),
                        onClick = { selectedSize = "Large" }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Jumlah Pesanan:", fontWeight = FontWeight.Medium, color = Color.Black)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = { if (quantity > 1) quantity-- },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5EBE0)),
                                modifier = Modifier.size(36.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("-", color = Color.Black, fontWeight = FontWeight.Bold)
                            }

                            Text(
                                text = "$quantity",
                                modifier = Modifier.padding(horizontal = 16.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Button(
                                onClick = { quantity++ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE5B4)),
                                modifier = Modifier.size(36.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("+", color = Color(0xFFD2691E), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal:", color = Color.Gray)
                            Text("Rp ${subtotal.toFormattedString()}", fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Pajak Resto (10%):", color = Color.Gray)
                            Text("Rp ${tax.toFormattedString()}", fontWeight = FontWeight.Medium)
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Tagihan:", fontWeight = FontWeight.Bold, color = Color.Black)
                            Text("Rp ${totalBill.toFormattedString()}", fontWeight = FontWeight.Bold, color = Color(0xFFD2691E))
                        }
                    }
                }
            }

            Button(
                onClick = {
                    Toast.makeText(context, "Berhasil ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2691E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("TAMBAH KE KERANJANG", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SizeOptionButton(text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(40.dp)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) Color(0xFFD2691E) else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (isSelected) Color(0xFFFFFBFA) else Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color(0xFFD2691E) else Color.DarkGray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 13.sp
        )
    }
}

fun Int.toFormattedString(): String {
    return NumberFormat.getInstance(Locale("in", "ID")).format(this)
}

@Preview(showBackground = true, name = "Soal 1 - Coffee Order Preview")
@Composable
fun PreviewCoffeeOrder() {
    CoffeeOrderApp()
}