package com.example.shoppingcart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingcart.ui.theme.ShoppingCartTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingCartTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Cartpage()
                }
            }
        }
    }
}

private data class CartLineItem(
    val foodItem: FoodItem, val quantity: MutableState<Int>
)

@Composable
fun Cartpage() {
    val cartLineItems = remember {
        FoodItemDataSource.foodItems.map { CartLineItem(it, mutableStateOf(1)) }
    }

    val total = cartLineItems.sumOf { it.foodItem.price * it.quantity.value }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { CartTopBar() },
        bottomBar = {
            CheckoutSection(
                total = total, onCheckout = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Successful....you go girl")
                    }
                })
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 8.dp)
        ) {
            items(cartLineItems) { lineItem ->
                FoodItemCard(lineItem = lineItem, onIncrement = {
                    if (lineItem.quantity.value < 10) lineItem.quantity.value++
                }, onDecrement = {
                    if (lineItem.quantity.value > 1) lineItem.quantity.value--
                })
            }
        }
    }
}

@Composable
private fun CartTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(vertical = 15.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_arrow_back_24),
            contentDescription = null,
            tint = Color.Blue
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = "Cart")


    }
}

@Composable
private fun FoodItemCard(
    lineItem: CartLineItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val foodItem = lineItem.foodItem
    val quantity = lineItem.quantity.value
    val lineTotal = foodItem.price * quantity

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(verticalAlignment = Alignment.Top) {
                Image(
                    painter = painterResource(foodItem.imageRes),
                    contentDescription = foodItem.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = foodItem.name, fontWeight = FontWeight.Bold)
                    Text(
                        text = foodItem.description, fontSize = 13.sp, color = Color.Gray
                    )
                }
                Text(
                    text = "$%.2f".format(foodItem.price), fontSize = 14.sp, color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuantityStepper(
                    quantity = quantity, onIncrement = onIncrement, onDecrement = onDecrement
                )
                Text(
                    text = "$%.2f".format(lineTotal),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D5AFE)
                )
            }
        }
    }
}

@Composable
private fun QuantityStepper(
    quantity: Int, onIncrement: () -> Unit, onDecrement: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFEDEDF5))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "−",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable(onClick = onDecrement)
        )
        Text(text = "$quantity")
        Text(
            text = "+",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable(onClick = onIncrement)
        )
    }
}

@Composable
private fun CheckoutSection(
    total: Double, onCheckout: () -> Unit, modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp, color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total")
                Text(text = "$%.2f".format(total),
                    fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3D5AFE),
                    contentColor = Color.White,
                )

            ) {
                Text(
                    text = "Checkout",
                )
                Spacer(
                    modifier = Modifier.width(5.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_add_shopping_cart_24),
                    contentDescription = null
                )


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartPreview() {
    ShoppingCartTheme {
        Cartpage()
    }
}