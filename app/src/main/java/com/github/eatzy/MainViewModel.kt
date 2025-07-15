package com.github.eatzy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.eatzy.data.DataRepository
import com.github.eatzy.data.EatzyDatabase
import com.github.eatzy.domain.FoodItem
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.domain.Recipient
import com.github.eatzy.domain.UnreadableNotification
import com.github.eatzy.domain.User
import com.github.eatzy.domain.WastedFood
import com.github.eatzy.ui.screen.DistributionItem
import com.github.eatzy.ui.screen.FoodItemCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataRepository) : ViewModel() {

    private val _businessId = MutableStateFlow<Int?>(null)
    val businessId: StateFlow<Int?> = _businessId

    val wastedFoods: Flow<PagingData<FoodItemCard>> = repository.getAllWasted()
        .map { pagingData ->
            pagingData.map { foodItem ->
                FoodItemCard(
                    foodName = foodItem.foodItem,
                    date = foodItem.leftoverInputDate.toString(),
                    size = foodItem.leftoverQuantity,
                    unit = foodItem.unit.toString(),
                    option = FoodOption.Wasted
                )
            }
        }
        .cachedIn(viewModelScope)

    val foodItemsCard: Flow<PagingData<FoodItemCard>> = repository.getAllFood()
        .map { pagingData ->
            pagingData.map { foodItem ->
                FoodItemCard(
                    foodName = foodItem.foodName,
                    date = foodItem.inputDate.toString(),
                    size = foodItem.initialQuantity,
                    unit = foodItem.unit.toString(),
                    option = FoodOption.Stock
                )
            }
        }
        .cachedIn(viewModelScope)

    val foodItems: Flow<PagingData<FoodItem>> = repository.getAllFood()
        .cachedIn(viewModelScope)

    val recipients: Flow<PagingData<Recipient>> = repository.getAllRecipient()
        .cachedIn(viewModelScope)

    val unDistributedWastedFood: Flow<PagingData<DistributionItem>> =
        repository.getAllUndistributedWastedFood()
            .map { pagingData ->
                pagingData.map {
                    DistributionItem(
                        value = it.leftoverQuantity,
                        progress = it.difference ?: 0.0,
                        foodName = it.foodItem,
                        typeText = it.condition.toString(),
                        id = it.id ?: 0
                    )
                }
            }
            .cachedIn(viewModelScope)

    val distributedWastedFood: Flow<PagingData<DistributionItem>> =
        repository.getAllDistributedWastedFood()
            .map { pagingData ->
                pagingData.map {
                    DistributionItem(
                        value = it.wastedFood.leftoverQuantity,
                        progress = it.wastedFood.leftoverQuantity,
                        foodName = it.wastedFood.foodItem,
                        typeText = it.wastedFood.condition.toString(),
                        id = it.wastedFood.id ?: 0
                    )
                }
            }
            .cachedIn(viewModelScope)

    val unreadableNotifications: Flow<PagingData<UnreadableNotification>> =
        repository.getUnreadableNotification()
            .cachedIn(viewModelScope)

    fun findWastedFoodById(id: Int): Flow<WastedFood?> = flow {
        emit(repository.findWastedFoodById(id))
    }

    fun saveFoodItemStock(foodItem: FoodItem) {
        viewModelScope.launch(Dispatchers.IO) {
            businessId.value?.let { repository.saveFoodStock(foodItem, it) }
        }
    }

    fun saveWastedFood(wastedFood: WastedFood) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveWastedFood(wastedFood)
        }
    }

    fun registerUser(user: User, onRegistrationComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUser(user)
                .let {
                    repository.saveBusiness(
                        user.business.copy(userId = it)
                    ).let {
                        viewModelScope.launch(Dispatchers.Main) {
                            onRegistrationComplete()
                        }
                    }
                }
        }
    }

    fun loginUser(email: String, password: String, onSuccessfulLogin: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.findUserByName(email)?.let {
                viewModelScope.launch(Dispatchers.Main) {
                    _businessId.value = it.business.id
                    onSuccessfulLogin()
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val repository = DataRepository(EatzyDatabase.getInstance(application).eatzyDao())
                return MainViewModel(
                    repository
                ) as T
            }
        }
    }
}