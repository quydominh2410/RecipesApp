package com.qq.recipes.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qq.recipes.data.Repository
import com.qq.recipes.models.FoodRecipe
import com.qq.recipes.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response


class MainViewModel @ViewModelInject constructor(
    private val repository: Repository, application: Application
) : AndroidViewModel(application) {

    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData();

    fun getRecipes(queries: Map<String, String>) {
        viewModelScope.launch {
            getRecipesSafeCall(queries)
        }
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        val response = repository.remoteDataSource.getRecipes(queries)
        recipesResponse.value = handleRecipesResponse(response)
    }

    private fun handleRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().contains("timeout") -> return NetworkResult.Error(
                null, "Connection Timeout"
            )
            response.code() == 402 -> return NetworkResult.Error(null, "Key NotValid")
            response.body()!!.results.isEmpty() -> return NetworkResult.Error(
                null,
                "Recipes not found"
            )
            response.isSuccessful -> {
                val data = response.body()
                return NetworkResult.Success(data!!)
            }

            else -> return NetworkResult.Error(null, response.message())
        }
    }


}