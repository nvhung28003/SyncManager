package com.lecre.zengo.data.local.helper

import android.util.Log
import com.blankj.utilcode.util.LogUtils

/**
 * This class presenter for manager all session with AWS cloud
 * It a singleton class.
 */
class SyncManager private constructor() {
    companion object {
        val instance: SyncManager = SyncManager()
    }

    private var sessionStore: MutableList<Sync> = mutableListOf()

    /**
     * start new session [Sync]
     *
     * @return index session in stack
     */
    fun start(newSession: Sync) : Int{
        changeAllSameSessionToCancel(newSession.id)
        val indexStackSession = addSession(newSession)

        //can start session or only put to queue
        val checkExitsSameSessionIsRunning = hasSameSessionIsRunning(newSession.id)
        if (!checkExitsSameSessionIsRunning) {
            newSession.task.invoke()
            newSession.syncStatus = SyncStatus.RUNNING
            newSession.isRunningReally = true
            LogUtils.iTag("SyncManager", "----start stackId ["+indexStackSession+"] session id ["+showIdString(newSession.id)+"] with idQueue= " + newSession.idQueue)
        }

        return indexStackSession;
    }

    /**
     * call when session [indexStackSession] finished
     * if session is running then invoke [taskDoingAfterFinishSession]
     *
     * @param indexStackSession: index session in stack
     * @param taskDoingAfterFinishSession: task will invoke
     * @return false if session is cancel
     */
    fun onFinish(indexStackSession: Int, taskDoingAfterFinishSession: () -> Unit, taskDoingAfterCancelSession: (() -> Unit)? = null ){
        LogUtils.iTag(
            "SyncManager",
            "----onFinish stackId ["+indexStackSession+"] session"
        )
        logStack()
        if(indexStackSession >= sessionStore.count())
            return

        sessionStore.get(indexStackSession).let {

            if(it.syncStatus == SyncStatus.RUNNING) {

                LogUtils.iTag(
                    "SyncManager",
                    "----onFinish Running stackId ["+indexStackSession+"] session id ["+showIdString(it.id)+"] with idQueue= " + it.idQueue
                )

                it.syncStatus = SyncStatus.FINISH
                taskDoingAfterFinishSession.invoke( )
                it.isRunningReally = false;
                return
            }

            if(it.syncStatus == SyncStatus.CANCEL && it.isRunningReally){
                LogUtils.iTag(
                    "SyncManager",
                    "----onCANCEL Running stackId ["+indexStackSession+"] session id ["+showIdString(it.id)+"] with idQueue= " + it.idQueue
                )

                taskDoingAfterCancelSession?.invoke()
                findAndStartNewestSession(it.id)
                it.isRunningReally = false
                return;
            }


            val isAllSessionFinished = sessionStore.find { it.syncStatus != SyncStatus.CANCEL && it.syncStatus != SyncStatus.FINISH } == null
            if(isAllSessionFinished){
                sessionStore = mutableListOf()
                LogUtils.iTag("SyncManager", "Clear all session!")
            }
            return
        }
    }

    fun clearAllSession(){
        sessionStore = mutableListOf()
    }


    /**
     * add and return index in stack
     */
    private fun addSession(newSession: Sync): Int {
        sessionStore.add(newSession)
        newSession.idQueue = sessionStore.count { it.id == newSession.id }
        LogUtils.iTag(
            "SyncManager",
            "----addSession stackId ["+(sessionStore.count() - 1)+"] session"
        )
        logStack()
        return sessionStore.count() - 1
    }

    private fun logStack(){
        var stringLog = ""

        sessionStore.forEachIndexed { index, session ->
            stringLog += "\nstackId ["+ index +"] session id ["+session.id+"] with idQueue= " + session.idQueue +", Status= " + session.syncStatus + ", isRunningReally= " + session.isRunningReally
        }
        LogUtils.iTag("SyncManager", stringLog)
    }


    private fun findAndStartNewestSession(id: Pair<String, String>) {
        LogUtils.iTag("SyncManager", "findAndStartNewestSession")
        sessionStore.findLast{it.id == id && it.syncStatus == SyncStatus.NOT_START}?.let {
            it.syncStatus = SyncStatus.RUNNING
            it.isRunningReally = true
            it.task.invoke()
            logStack()
        }
    }

    fun changeAllSameSessionToCancel(sessionIdTarget: Pair<String, String>) {
        for (session in sessionStore) {
            if (session.id == sessionIdTarget) {
//                if(session.syncStatus!=SyncStatus.RUNNING)
                session.syncStatus = SyncStatus.CANCEL;
            }
        }
    }

    fun getSession(sessionIndex: Int) = sessionStore.get(sessionIndex)

    private fun hasSameSessionIsRunning(targetSessionId: Pair<String, String>): Boolean {
        sessionStore.find { it.id == targetSessionId && it.isRunningReally}?.let {
            return true
        }
        return false
    }

    fun createSyncId(constructionId: String ="", constructionSiteId: String = "", syncStrategy: SyncType = SyncType.FETCH): Pair<String, String> {
        return if(syncStrategy == SyncType.FETCH)
            "FETCH" to "FETCH"
        else
            constructionId to constructionSiteId
    }

    private fun showIdString(idSession: Pair<String, String>) : String{
        return "[" + idSession.first + "," + idSession.second+"]";
    }

    fun isHasNotStartLatestSession(constructionId: Pair<String, String>): Boolean {
        LogUtils.iTag("SyncManager", "chuan bi ne")
        logStack()
        return sessionStore.findLast { it.id == constructionId && !it.isRunningReally && it.syncStatus == SyncStatus.NOT_START } != null
    }


    fun isRunningLatestSession(constructionId: Pair<String, String>): Boolean {
        return sessionStore.findLast { it.id == constructionId && it.isRunningReally && it.syncStatus == SyncStatus.RUNNING } != null
    }

    enum class SyncStatus {
        NOT_START,
        RUNNING,
        FINISH,
        CANCEL
    }

    /**
     * this enum presenter sync type from AWS
     *
     * @property FETCH: fetch all constructions
     * @property QUERY: fetch by target
     */
    enum class SyncType{
        FETCH,
        QUERY
    }

    class Sync(
        var id: Pair<String, String>,
        var task: () -> Unit
    ){
        /**
         * index queue same session
         */
        var idQueue: Int = 0
        var syncStatus: SyncStatus = SyncStatus.NOT_START
        var isRunningReally = false;
    }
}
