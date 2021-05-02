package com.main.climbingdiary.common

import android.content.Context
import android.net.Uri
import android.os.storage.StorageManager
import android.provider.DocumentsContract
import java.io.File

object FileUtil {
    private const val PRIMARY_VOLUME_NAME = "primary"

    fun getFullPathFromTreeUri(treeUri: Uri?, con: Context): String? {
        if (treeUri == null) return null

        var volumePath = getVolumePath(getVolumeIdFromTreeUri(treeUri), con)
        if (volumePath == null) return File.separator
        if (volumePath.endsWith(File.separator)) volumePath =
            volumePath.substring(0, volumePath.length - 1)
        var documentPath = getDocumentPathFromTreeUri(treeUri)
        if (documentPath!!.endsWith(File.separator)) documentPath =
            documentPath.substring(0, documentPath.length - 1)

        return if (documentPath.isNotEmpty()) {
            if (documentPath.startsWith(File.separator)) volumePath + documentPath else volumePath + File.separator + documentPath
        } else volumePath
    }


    private fun getVolumePath(volumeId: String?, context: Context): String? {
        return try {
            val mStorageManager =
                context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            val storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
            val getVolumeList = mStorageManager.javaClass.getMethod("getVolumeList")
            val getUuid = storageVolumeClazz.getMethod("getUuid")
            val getPath = storageVolumeClazz.getMethod("getPath")
            val isPrimary = storageVolumeClazz.getMethod("isPrimary")
            val result = getVolumeList.invoke(mStorageManager)
            val length = java.lang.reflect.Array.getLength(result)
            for (i in 0 until length) {
                val storageVolumeElement = java.lang.reflect.Array.get(result, i)
                val uuid = getUuid.invoke(storageVolumeElement) as String
                val primary = isPrimary.invoke(storageVolumeElement) as Boolean

                // primary volume?
                if (primary && PRIMARY_VOLUME_NAME == volumeId) return getPath.invoke(
                    storageVolumeElement
                ) as String

                // other volumes?
                if (uuid == volumeId) return getPath.invoke(storageVolumeElement) as String
            }
            // not found.
            null
        } catch (ex: Exception) {
            null
        }
    }

    private fun getVolumeIdFromTreeUri(treeUri: Uri): String? {
        val docId = DocumentsContract.getTreeDocumentId(treeUri)
        val split = docId.split(":").toTypedArray()
        return if (split.isNotEmpty()) split[0] else null
    }

    private fun getDocumentPathFromTreeUri(treeUri: Uri): String? {
        val docId = DocumentsContract.getTreeDocumentId(treeUri)
        val split: Array<String?> = docId.split(":").toTypedArray()
        return if (split.size >= 2 && split[1] != null) split[1] else File.separator
    }
}