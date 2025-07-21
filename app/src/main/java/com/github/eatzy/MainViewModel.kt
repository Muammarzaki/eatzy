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
import com.github.eatzy.domain.FoodForm
import com.github.eatzy.domain.FoodItem
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.domain.FoodWasteChartData
import com.github.eatzy.domain.Recipient
import com.github.eatzy.domain.UnreadableNotification
import com.github.eatzy.domain.User
import com.github.eatzy.domain.WastedFood
import com.github.eatzy.domain.WastedFoodTrend
import com.github.eatzy.ui.screen.DistributionItem
import com.github.eatzy.ui.screen.FoodItemCard
import com.github.eatzy.util.toStringDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(private val repository: DataRepository) : ViewModel() {

    private val _currentBusinessUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentBusinessUser

    val wastedFoods: Flow<PagingData<FoodItemCard>> = repository.getAllWasted()
        .map { pagingData ->
            pagingData.map { wastedFood ->
                FoodItemCard(
                    foodName = wastedFood.foodItem,
                    date = wastedFood.leftoverInputDate?.toStringDate("dd MMMM yyyy") ?: "",
                    size = wastedFood.leftoverQuantity,
                    unit = wastedFood.unit,
                    option = FoodOption.Wasted,
                    id = wastedFood.id ?: -1,
                    form = wastedFood.form
                )
            }
        }
        .cachedIn(viewModelScope)

    val foodItemsCard: Flow<PagingData<FoodItemCard>> = repository.getAllFood()
        .map { pagingData ->
            pagingData.map { foodItem ->
                FoodItemCard(
                    foodName = foodItem.foodName,
                    date = foodItem.inputDate?.toStringDate("dd MMMM yyyy") ?: "",
                    size = foodItem.initialQuantity,
                    unit = foodItem.unit,
                    option = FoodOption.STOCK,
                    id = foodItem.id ?: 0,
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

    suspend fun findWastedFoodById(id: Int): WastedFood? = repository.findWastedFoodById(id)

    suspend fun findFoodItemById(id: Int): FoodItem? = repository.findFoodItemById(id)

    fun saveFoodItemStock(foodItem: FoodItem) {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser.value?.business?.id?.let { repository.saveFoodStock(foodItem, it) }
        }
    }

    fun saveWastedFood(wastedFood: WastedFood) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveWastedFood(wastedFood)
        }
    }

    fun saveDistribution(wastedFoofId: Int, recipientId: Int, notes: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveDistribution(wastedFoofId, recipientId, notes)
        }
    }

    fun registerUser(user: User, onRegistrationComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUser(user)
                .let { userId ->
                    repository.saveBusiness(
                        user.business?.copy(userId = userId)
                            ?: throw IllegalStateException("Business information is required for registration.")
                    )
                    viewModelScope.launch(Dispatchers.Main) {
                        onRegistrationComplete()
                    }
                }
        }
    }

    fun loginUser(email: String, password: String, onSuccessfulLogin: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.findUserByName(email)?.let {
                viewModelScope.launch(Dispatchers.Main) {
                    _currentBusinessUser.value = it
                    onSuccessfulLogin()
                }
            }
        }
    }

    private val _selectedUnit = MutableStateFlow(FoodUnit.KILOGRAM)

    @OptIn(ExperimentalCoroutinesApi::class)
    val wastedFoodsByUnit: StateFlow<PagingData<WastedFood>> =
        _selectedUnit
            .flatMapLatest { unit ->
                repository.getAllWastedByUnit(unit)
                    .cachedIn(viewModelScope)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PagingData.empty()
            )

    fun selectUnit(unit: FoodUnit) {
        _selectedUnit.value = unit
    }

    private val _chartData = MutableStateFlow<FoodWasteChartData?>(null)
    val summaryChartData: StateFlow<FoodWasteChartData?> = _chartData.asStateFlow()

    private val _currentYear = MutableStateFlow<String?>(null)
    val currentYear: StateFlow<String?> = _currentYear.asStateFlow()

    private val _dataWastedFoodEachMonth = MutableStateFlow<List<WastedFoodTrend>>(emptyList())
    val dataWastedFoodEachMonth: StateFlow<List<WastedFoodTrend>> =
        _dataWastedFoodEachMonth.asStateFlow()

    private fun fetchChartData() {
        viewModelScope.launch {
            repository.getFoodWasteChartData().collect {
                _chartData.value = it
            }
        }
    }

    fun setCurrentYear(year: String) {
        _currentYear.value = year
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

    init {
        fetchChartData()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        _currentYear.value = currentYear

        viewModelScope.launch {
            _currentYear.filterNotNull().collectLatest { year ->
                repository.getWastedFoodEachMonth(year).collect {
                    _dataWastedFoodEachMonth.value = it
                }
            }
        }
    }
}
