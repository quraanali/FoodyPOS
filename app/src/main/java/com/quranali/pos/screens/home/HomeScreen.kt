package com.quranali.pos.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.quranali.pos.data.local.entity.ProductEntity
import com.quranali.pos.screens.component.ProgressLoader
import com.quranali.pos.R
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    val context = LocalContext.current

    uiState.errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        viewModel.clearErrorMsg()
    }

    Box(Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {


            UserView(
                username = uiState.userName,
                userId = uiState.userID,
                isConnected = uiState.isConnected
            )

            MenuView(tables = "04", guest = "02")

            var searchQuery by remember { mutableStateOf("") }
            ProductSearchBar(
                query = searchQuery,
                onQueryChange = { newText ->
                    searchQuery = newText
                    viewModel.searchProducts(newText)

                },
                onSearchExecuted = { finalQuery ->
                }
            )


            if (uiState.categoriesList.isNotEmpty()) {
                PrimaryScrollableTabRow(
                    selectedTabIndex = uiState.selectedCategoryIndex,
                    modifier = Modifier
                        .fillMaxWidth(),
                    containerColor = Color.Transparent,
                    divider = {},
                    edgePadding = 0.dp,
                    indicator = {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier
                                .tabIndicatorOffset(uiState.selectedCategoryIndex)
                                .padding(bottom = 8.dp)
                                .fillMaxWidth(),
                            width = 70.dp,
                            height = 2.dp,
                            color = colorResource(R.color.primary),
                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                    }

                ) {
                    uiState.categoriesList.forEachIndexed { index, category ->
                        Tab(
                            selected = index == uiState.selectedCategoryIndex,
                            onClick = {
                                viewModel.selectCategory(index)
                            }) {
                            Text(
                                modifier = Modifier.padding(bottom = 12.dp),
                                text = category.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (uiState.selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (uiState.selectedCategoryIndex == index) Color.Black else Color.Gray
                            )
                        }
                    }

                }
            }


            ProductsGrid(
                modifier = Modifier.fillMaxWidth(),
                list = uiState.productsList,
                onItemClick = { product ->
                    viewModel.addProductToCart(product)
                },
            )

        }

        if (uiState.isLoading) {
            ProgressLoader(Modifier.fillMaxSize())
        }
    }
}


@Composable
fun UserView(
    modifier: Modifier = Modifier, username: String, userId: String, isConnected: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = username,
                modifier = Modifier.size(26.dp),
                tint = colorResource(id = R.color.primary)
            )


            Text(
                text = username,
                modifier = modifier.padding(horizontal = 8.dp),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = userId, style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(8.dp))

        Badge(
            modifier = Modifier.size(18.dp),
            containerColor = if (isConnected) Color.Green else Color.Red
        )
    }
}


@Composable
fun MenuView(modifier: Modifier = Modifier, tables: String, guest: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.menu),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 6.dp),
                tint = colorResource(id = R.color.primary),
                painter = painterResource(R.drawable.ic_f1),
                contentDescription = tables
            )

            Text(
                text = tables,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))


        Row(
            modifier = modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 6.dp),
                tint = colorResource(id = R.color.primary),
                painter = painterResource(R.drawable.ic_guest),
                contentDescription = guest
            )

            Text(
                text = guest,
                overflow = TextOverflow.Ellipsis,
            )
        }


    }
}

@Composable
fun ProductSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchExecuted: (String) -> Unit,
) {

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
        placeholder = { Text(stringResource(R.string.search_hint), color = Color.Gray) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Black)
        },
        // Force the sharp corners here
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchExecuted(query) }
        )
    )
}


@Composable
fun CartView(modifier: Modifier, uiState: HomeUiState, viewModel: HomeViewModel) {

    LazyColumn(modifier = modifier) {
        items(uiState.selectedProductList.size, key = { it }) { item ->
            CartItem(selectedProduct = uiState.selectedProductList[item], viewModel = viewModel)
        }

        if (uiState.total != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = uiState.total)
                        if (uiState.discount != null) Text(
                            text = uiState.discount, color = Color(0xFF653F03)
                        )
                    }
                    Button(onClick = {
                        viewModel.checkoutOrder()
                    }) {
                        Text("Checkout")
                    }
                }
            }
        }
    }

}


@Composable
fun CartItem(selectedProduct: SelectedProduct, viewModel: HomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = selectedProduct.thumb,
            contentDescription = selectedProduct.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp)
        )

        Text(
            text = selectedProduct.name + " x " + selectedProduct.quantity + "\n" + "Price: " + selectedProduct.price * selectedProduct.quantity,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        )
        Icon(
            imageVector = Icons.Default.Delete,
            tint = Color.Red,
            contentDescription = "Delete",
            modifier = Modifier
                .size(22.dp)
                .clickable { viewModel.removeProductFromCart(selectedProduct) })

    }

}

@Composable
private fun ProductsGrid(
    list: List<ProductEntity>,
    onItemClick: (ProductEntity) -> Unit,
    modifier: Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
        modifier = modifier,
    ) {
        items(list, key = { it.id }) { item ->
            GridListItem(
                item = item,
                onClick = { onItemClick(item) },
            )
        }
    }
}

@Composable
private fun GridListItem(
    item: ProductEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = modifier
            .padding(12.dp),
        onClick = onClick,
    ) {
        Column(
            modifier
                .clickable { onClick() }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = item.image,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.ic_not_found),
                    modifier = Modifier.matchParentSize(),
                    placeholder = painterResource(R.drawable.ic_loading)
                )


            }

            Spacer(Modifier.height(2.dp))

            Text(
                item.name,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                item.price.toString(),
                modifier = Modifier.padding(start = 12.dp, bottom = 12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}
