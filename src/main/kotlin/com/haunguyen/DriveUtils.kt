package com.haunguyen

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.*
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import org.mortbay.jetty.security.Credential
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*

class DriveUtils : GoogleCredentials() {

    fun createDriveFolder(parentFolderId: String?, folderName: String?): String {
        val fileMetadata = com.google.api.services.drive.model.File()
            .setName(folderName)
            .setMimeType("application/vnd.google-apps.folder")

        if (parentFolderId != null) {
            val parents = listOf<String>(parentFolderId)
            fileMetadata.parents = parents
        }

        val file = driveServiceBuilder().files().create(fileMetadata)
            .setFields("id, name")
            .execute()

        return file.id
    }

    fun uploadFileToDrive(fileName: String, filePath: String, fileType: String, rootFolderId: String): String {
        val fileMetadata = com.google.api.services.drive.model.File()
            .setName(fileName)
            .setParents(Collections.singletonList(rootFolderId))

        val mediaContent = FileContent(fileType, File(filePath))
        val file = driveServiceBuilder().files().create(fileMetadata, mediaContent)
            .setFields("id")
            .execute()

        println("fileId = ${file.id}")
        return file.id
    }
}
