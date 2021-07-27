package com.example.syncmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      //  example

//        private fun getConstructionPrepareSync(
//                mainActivity: MainActivity,
//                displayLoading: Boolean,
//                callbackReportCV: SyncConstructionBeforeReportCV? = null,
//                constructionUploadBottomSheet: Construction? = null
//
//        ) {
//            viewModelScope.launch(Dispatchers.IO) {
//                var indexStackSession = -1
//                val sessionId = SyncManager.instance.createSyncId()
//                val sessionTask = {
//                    SyncHelper.getConstructionNeedDownLoadSyncManager(
//                            mainActivity,
//                            pref,
//                            object : GetConstructionDownLoadSyncManagerCallBack {
//                                override fun onGetConstructionDownload(constructions: ArrayList<Construction>?) {
//                                    SyncManager.instance.onFinish(
//                                            indexStackSession,
//                                            taskDoingAfterFinishSession = {
//                                                val listConstructionIdPrepareSync = arrayListOf<String>()
//                                                val listConstructionDownload = arrayListOf<String>()
//                                                constructions?.let {
//                                                    //filter construction download
//                                                    val listFilterConstructionDownload =
//                                                            filterConstructionDownload(constructions)
//                                                    // delete construction cloud if they're not existed on cloud list
//                                                    if (deleteConstructionInLocal(constructions)) {
//                                                        handleGetAllData()
//                                                    }
//                                                    //
//                                                    listConstructionDownload.addAll(
//                                                            listFilterConstructionDownload.map { constructionFilter -> constructionFilter.constructionId }
//                                                    )
//                                                }
//                                                listConstructionIdPrepareSync.addAll(
//                                                        mergerUploadAndDownloadString(
//                                                                listConstructionDownload
//
//                                                        )
//                                                )
//                                                // just upload a construction when click bottom sheet
//                                                if (constructionUploadBottomSheet != null) {
//                                                    listConstructionIdPrepareSync.clear()
//                                                    if (constructionUploadBottomSheet.status != AppConstants.ConstructionStatus.LOCAL) {
//                                                        constructions?.forEach { itemConstructionCloud ->
//                                                            if (itemConstructionCloud.constructionId == constructionUploadBottomSheet.constructionId) {
//                                                                if (itemConstructionCloud.version != constructionUploadBottomSheet.version || constructionUploadBottomSheet.isStatusNotSync()) {
//                                                                    listConstructionIdPrepareSync.add(
//                                                                            constructionUploadBottomSheet.constructionId
//                                                                    )
//                                                                }
//                                                            }
//
//                                                        }
//
//                                                    } else{
//                                                        listConstructionIdPrepareSync.add(
//                                                                constructionUploadBottomSheet.constructionId
//                                                        )
//                                                    }
//                                                }
//
//                                                //syncManager to Cloud
//
//
//                                                if (listConstructionIdPrepareSync.isNotEmpty()) {
//                                                    if (mainActivity.idConstructionloginFromDetail.isNotEmpty()){
//                                                        listConstructionIdPrepareSync.remove((mainActivity.idConstructionloginFromDetail))
//                                                        mainActivity.idConstructionloginFromDetail = ""
//                                                    }
//                                                    constructionSyncManager(
//                                                            mainActivity,
//                                                            listConstructionIdPrepareSync,
//                                                            isUpload = true,
//                                                            displayLoading = displayLoading,
//                                                            callbackReportCV
//                                                    )
//                                                } else {
//                                                    mutableLiveDataShowLoading.postValue(false)
//                                                    if (displayLoading){
//                                                        mutableLiveDataShowPercentLoading.postValue(100f)
//                                                    }
//
//                                                    callbackReportCV?.onSyncConstructionSuccess()
//                                                }
//                                            })
//                                }
//                            })
//                }
//                val session = SyncManager.Sync(sessionId, sessionTask)
//                indexStackSession = SyncManager.instance.start(session);
//            }
//        }
    }
}