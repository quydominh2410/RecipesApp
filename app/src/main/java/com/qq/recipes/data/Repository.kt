package com.qq.recipes.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(val remoteDataSource: RemoteDataSource) {

}