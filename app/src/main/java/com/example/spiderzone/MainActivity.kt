package com.example.spiderzone

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.io.FileInputStream
import java.io.InputStream
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import android.webkit.MimeTypeMap
import coil.compose.AsyncImage
import java.util.UUID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import com.google.firebase.storage.StorageException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
private fun SpiderZoneRoot() {
    val context = LocalContext.current
    val themePrefs = remember(context) { AppThemePreferences(context) }
    var themeMode by remember { mutableStateOf(themePrefs.getThemeMode()) }
    SpiderZoneTheme(mode = themeMode) {
        SpiderZoneApp(
            themeMode = themeMode,
            onThemeModeChange = { mode ->
                themeMode = mode
                themePrefs.setThemeMode(mode)
            }
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        configureFirebaseStorageBucket()
        createReminderChannel()
        setContent { SpiderZoneRoot() }
    }

    private fun configureFirebaseStorageBucket() {
        val bucket = FirebaseApp.getInstance().options.storageBucket?.trim().orEmpty()
        if (bucket.isNotEmpty()) {
            Firebase.storage("gs://$bucket")
        }
    }

    private fun createReminderChannel() {
        val channel = NotificationChannel(
            ReminderReceiver.CHANNEL_ID,
            "SpiderZone reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}

internal data class Species(
    val id: String = "",
    val latinName: String = "",
    val commonName: String = "",
    val category: String = "tarantula",
    /** Występowanie geograficzne (mapa — w przyszłości). */
    val occurrence: String = "",
    val lifeMode: String = "",
    val temperatureDay: String = "",
    val temperatureNight: String = "",
    val humidity: String = "",
    val sizeMale: String = "",
    val sizeFemale: String = "",
    val temperament: String = "",
    val venom: String = "",
    /** Poziom zaawansowania hodowli. */
    val difficulty: String = "",
    /** tak | nie */
    val cites: String = "",
    val diet: String = "",
    /** Opis literaturowy; później powiązanie z próbami użytkowników. */
    val documentedBreeding: String = "",
    val careNotes: String = "",
    val funFact: String = "",
    val imageUrl: String = "",
    val phylum: String = "",
    val taxonomicClass: String = "",
    val suborder: String = "",
    val family: String = "",
    /** Pola legacy (Firestore) — używane jako fallback w UI. */
    val temperature: String = "",
    val lifespan: String = "",
    val size: String = "",
    val origin: String = ""
) {
    fun displayOccurrence(): String = occurrence.ifBlank { origin }
    fun displayTemperatureDay(): String = temperatureDay.ifBlank { temperature }
    fun displaySizeMale(): String = sizeMale.ifBlank { size.takeIf { it.isNotBlank() } ?: "" }
}

internal data class Animal(
    val id: String = "",
    val name: String = "",
    val speciesLatinName: String = "",
    val speciesCommonName: String = "",
    val speciesId: String = "",
    /** taxonomy_static | firestore_species | custom | legacy */
    val speciesSource: String = "taxonomy_static",
    val speciesLabel: String = "",
    val stage: String = "L1",
    val sex: String = "unknown",
    val notes: String = "",
    val birthDate: String = "",
    val ownedSince: String = "",
    val photoUrls: List<String> = emptyList(),
    val videoUrls: List<String> = emptyList(),
    val isPublic: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    fun speciesDisplay(): String {
        val latin = speciesLatinName.ifBlank { speciesLabel }
        val common = speciesCommonName.takeIf { it.isNotBlank() }
        return when {
            latin.isNotBlank() && common != null -> "$latin ($common)"
            latin.isNotBlank() -> latin
            common != null -> common
            else -> "—"
        }
    }
}

private data class PublicUserProfile(
    val uid: String = "",
    val nickname: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val avatarUrl: String = "",
    val bio: String = "",
    val createdAt: Long = 0L
) {
    fun displayName(): String = nickname.ifBlank { firstName.ifBlank { "Hodowca" } }
}

private data class CommunityAnimalItem(
    val ownerUid: String,
    val animal: Animal,
    val ownerProfile: PublicUserProfile? = null
)

private data class Reminder(
    val id: String = "",
    val title: String = "",
    val dueEpochMillis: Long = 0
)

private data class SignUpProfileData(
    val nickname: String,
    val firstName: String,
    val lastName: String,
    val breederLevel: String
)

private data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val nickname: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val breederLevel: String = "",
    val avatarUrl: String = "",
    val bio: String = "",
    val createdAt: Long = 0L
)

private data class AuthFieldErrors(
    val email: String? = null,
    val password: String? = null,
    val general: String? = null
)

private class AuthPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getRememberMe(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, false)

    fun getSavedEmail(): String = prefs.getString(KEY_EMAIL, "").orEmpty()

    fun saveLogin(email: String, rememberMe: Boolean) {
        prefs.edit()
            .putBoolean(KEY_REMEMBER_ME, rememberMe)
            .putString(KEY_EMAIL, if (rememberMe) email else "")
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "spiderzone_auth"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_EMAIL = "saved_email"
    }
}

private fun validateAuthEmail(email: String): String? = when {
    email.isBlank() -> "Podaj adres email"
    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Nieprawidlowy format adresu email"
    else -> null
}

private fun validateAuthPassword(password: String, forSignUp: Boolean): String? = when {
    password.isBlank() -> "Podaj haslo"
    forSignUp && password.length < 6 -> "Haslo musi miec co najmniej 6 znakow"
    else -> null
}

private fun communityLoadErrorMessage(e: Exception): String {
    val msg = e.message.orEmpty()
    return when {
        msg.contains("PERMISSION_DENIED", ignoreCase = true) ->
            "Brak uprawnien Firestore. Wklej i opublikuj firestore.rules (publicAnimals, publicProfiles) w Firebase Console."
        msg.contains("FAILED_PRECONDITION", ignoreCase = true) && msg.contains("index", ignoreCase = true) ->
            "Firestore wymaga indeksu. Otworz link z logow Android Studio (Create index)."
        else -> msg.ifBlank { "Blad wczytywania spolecznosci" }
    }
}

private fun mapFirebaseAuthError(error: Throwable): AuthFieldErrors {
    if (error !is FirebaseAuthException) {
        return AuthFieldErrors(general = error.message ?: "Wystapil nieoczekiwany blad")
    }
    return when (error.errorCode) {
        "ERROR_INVALID_EMAIL" -> AuthFieldErrors(email = "Nieprawidlowy adres email")
        "ERROR_USER_NOT_FOUND" -> AuthFieldErrors(email = "Nie znaleziono konta z tym adresem email")
        "ERROR_WRONG_PASSWORD" -> AuthFieldErrors(password = "Nieprawidlowe haslo")
        "ERROR_INVALID_CREDENTIAL", "ERROR_INVALID_LOGIN_CREDENTIALS" ->
            AuthFieldErrors(general = "Nieprawidlowy email lub haslo")
        "ERROR_USER_DISABLED" -> AuthFieldErrors(general = "To konto zostalo zablokowane")
        "ERROR_TOO_MANY_REQUESTS" -> AuthFieldErrors(general = "Zbyt wiele prob logowania. Sprobuj pozniej")
        "ERROR_NETWORK_REQUEST_FAILED" -> AuthFieldErrors(general = "Brak polaczenia z internetem")
        "ERROR_EMAIL_ALREADY_IN_USE" -> AuthFieldErrors(email = "Ten adres email jest juz zajety")
        "ERROR_WEAK_PASSWORD" -> AuthFieldErrors(password = "Haslo jest zbyt slabe (min. 6 znakow)")
        else -> AuthFieldErrors(general = "Blad logowania (${error.errorCode})")
    }
}

private class SpiderZoneRepository(
    private val auth: FirebaseAuth = Firebase.auth
) {
    private val db = Firebase.firestore

    fun currentUserId(): String? = auth.currentUser?.uid

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun sendEmailVerification() {
        auth.currentUser?.sendEmailVerification()?.await()
    }

    suspend fun reloadCurrentUser(): FirebaseUser? {
        val user = auth.currentUser ?: return null
        user.reload().await()
        return auth.currentUser
    }

    fun isCurrentUserEmailVerified(): Boolean = auth.currentUser?.isEmailVerified == true

    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    fun signOut() = auth.signOut()

    suspend fun createUserProfile(context: Context, uid: String, email: String, data: SignUpProfileData, avatarUri: Uri?) {
        val avatarUrl = if (avatarUri != null) uploadAvatar(context, uid, avatarUri) else ""
        db.collection("users").document(uid).set(
            mapOf(
                "email" to email,
                "nickname" to data.nickname,
                "firstName" to data.firstName,
                "lastName" to data.lastName,
                "breederLevel" to data.breederLevel,
                "avatarUrl" to avatarUrl,
                "bio" to "",
                "createdAt" to System.currentTimeMillis()
            )
        ).await()
        syncPublicProfile(uid)
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        val snap = db.collection("users").document(uid).get().await()
        if (!snap.exists()) return null
        return snap.toUserProfile(uid)
    }

    suspend fun ensureUserProfile(uid: String, email: String) {
        val ref = db.collection("users").document(uid)
        if (!ref.get().await().exists()) {
            ref.set(
                mapOf(
                    "email" to email,
                    "nickname" to "",
                    "firstName" to "",
                    "lastName" to "",
                    "breederLevel" to "poczatkujacy",
                    "avatarUrl" to "",
                    "bio" to "",
                    "createdAt" to System.currentTimeMillis()
                )
            ).await()
        }
    }

    suspend fun updateUserProfile(uid: String, updates: Map<String, Any?>) {
        db.collection("users").document(uid).set(updates, SetOptions.merge()).await()
        syncPublicProfile(uid)
    }

    suspend fun getPublicProfile(uid: String): PublicUserProfile? {
        return runCatching {
            val snap = db.collection("publicProfiles").document(uid).get().await()
            if (snap.exists()) snap.toPublicUserProfile(uid) else null
        }.getOrNull()
    }

    private suspend fun loadOwnerSnapshotForFeed(ownerUid: String): PublicUserProfile? {
        getPublicProfile(ownerUid)?.let { return it }
        return runCatching {
            getUserProfile(ownerUid)?.let { profile ->
                PublicUserProfile(
                    uid = ownerUid,
                    nickname = profile.nickname,
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    avatarUrl = profile.avatarUrl,
                    bio = profile.bio,
                    createdAt = profile.createdAt
                )
            }
        }.getOrNull()
    }

    suspend fun publicAnimalsByOwner(ownerUid: String): List<Animal> {
        val snap = db.collection("publicAnimals")
            .whereEqualTo("ownerUid", ownerUid)
            .get()
            .await()
        return snap.documents.map { it.toAnimal() }
            .sortedByDescending { it.createdAt }
    }

    suspend fun publishPublicProfile(uid: String) = syncPublicProfile(uid)

    private suspend fun syncPublicProfile(uid: String) {
        val snap = db.collection("users").document(uid).get().await()
        if (!snap.exists()) return
        val profile = snap.toUserProfile(uid)
        db.collection("publicProfiles").document(uid).set(
            mapOf(
                "nickname" to profile.nickname,
                "firstName" to profile.firstName,
                "lastName" to profile.lastName,
                "avatarUrl" to profile.avatarUrl,
                "bio" to profile.bio,
                "createdAt" to profile.createdAt
            )
        ).await()
    }

    suspend fun updateUserAvatar(context: Context, uid: String, avatarUri: Uri): String {
        val avatarUrl = uploadAvatar(context, uid, avatarUri)
        updateUserProfile(uid, mapOf("avatarUrl" to avatarUrl))
        return avatarUrl
    }

    private suspend fun uploadAvatar(context: Context, uid: String, avatarUri: Uri): String {
        val cached = importPickedMedia(context, avatarUri, "photos")
        val ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(cached.mimeType) ?: "jpg"
        val ref = uploadCachedMediaToStorage(cached, "avatars/$uid.$ext")
        return ref.downloadUrl.await().toString()
    }

    suspend fun species(limit: Long = 3000): List<Species> {
        val snap = db.collection("species").orderBy("latinName").limit(limit).get().await()
        return snap.documents.map { it.toSpecies() }
    }

    suspend fun userAnimals(uid: String): List<Animal> {
        val snap = db.collection("users").document(uid).collection("animals").get().await()
        return snap.documents.map { it.toAnimal() }
    }

    suspend fun createAnimalWithMedia(
        context: Context,
        uid: String,
        draft: Animal,
        photoFiles: List<CachedMedia>,
        videoFiles: List<CachedMedia>
    ) {
        val docRef = db.collection("users").document(uid).collection("animals").document()
        val animalId = docRef.id
        val photoUrls = photoFiles.map { uploadAnimalMedia(uid, animalId, it, "photos") }
        val videoUrls = videoFiles.map { uploadAnimalMedia(uid, animalId, it, "videos") }
        val now = System.currentTimeMillis()
        val label = draft.speciesCommonName.ifBlank { draft.speciesLatinName }
        val complete = draft.copy(
            id = animalId,
            photoUrls = photoUrls,
            videoUrls = videoUrls,
            speciesLabel = draft.speciesLabel.ifBlank { label },
            createdAt = now,
            updatedAt = now
        )
        val batch = db.batch()
        batch.set(docRef, complete.toFirestoreMap())
        if (complete.isPublic) {
            val ownerSnapshot = loadOwnerSnapshotForFeed(uid)
            batch.set(
                db.collection("publicAnimals").document(animalId),
                complete.toPublicFeedMap(uid, ownerSnapshot)
            )
        }
        batch.commit().await()
    }

    suspend fun updateAnimalWithMedia(
        context: Context,
        uid: String,
        existing: Animal,
        draft: Animal,
        newPhotoFiles: List<CachedMedia>,
        newVideoFiles: List<CachedMedia>
    ) {
        val animalId = existing.id
        val addedPhotos = newPhotoFiles.map { uploadAnimalMedia(uid, animalId, it, "photos") }
        val addedVideos = newVideoFiles.map { uploadAnimalMedia(uid, animalId, it, "videos") }
        val label = draft.speciesCommonName.ifBlank { draft.speciesLatinName }
        val complete = draft.copy(
            id = animalId,
            photoUrls = existing.photoUrls + addedPhotos,
            videoUrls = existing.videoUrls + addedVideos,
            speciesLabel = draft.speciesLabel.ifBlank { label.ifBlank { existing.speciesLabel } },
            createdAt = existing.createdAt,
            updatedAt = System.currentTimeMillis()
        )
        val batch = db.batch()
        val docRef = db.collection("users").document(uid).collection("animals").document(animalId)
        batch.set(docRef, complete.toFirestoreMap())
        val publicRef = db.collection("publicAnimals").document(animalId)
        if (complete.isPublic) {
            val ownerSnapshot = loadOwnerSnapshotForFeed(uid)
            batch.set(publicRef, complete.toPublicFeedMap(uid, ownerSnapshot))
        } else {
            runCatching { batch.delete(publicRef) }
        }
        batch.commit().await()
    }

    private suspend fun syncPublicAnimalFeed(ownerUid: String, animal: Animal) {
        val ref = db.collection("publicAnimals").document(animal.id)
        if (animal.isPublic) {
            val ownerSnapshot = loadOwnerSnapshotForFeed(ownerUid)
            ref.set(animal.toPublicFeedMap(ownerUid, ownerSnapshot)).await()
        } else {
            runCatching { ref.delete().await() }
        }
    }

    private suspend fun uploadAnimalMedia(
        uid: String,
        animalId: String,
        media: CachedMedia,
        subfolder: String
    ): String {
        val ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(media.mimeType)
            ?: if (subfolder == "photos") "jpg" else "mp4"
        val fileName = "${UUID.randomUUID()}.$ext"
        val path = "users/$uid/animals/$animalId/$subfolder/$fileName"
        val ref = uploadCachedMediaToStorage(media, path)
        return ref.downloadUrl.await().toString()
    }

    suspend fun reminders(uid: String): List<Reminder> {
        val snap = db.collection("users").document(uid).collection("reminders")
            .orderBy("dueEpochMillis", Query.Direction.ASCENDING)
            .get()
            .await()
        return snap.documents.map { it.toReminder() }
    }

    suspend fun addReminder(uid: String, reminder: Reminder) {
        db.collection("users").document(uid).collection("reminders")
            .add(mapOf("title" to reminder.title, "dueEpochMillis" to reminder.dueEpochMillis))
            .await()
    }

    /** Uzupełnia publicAnimals dla pupili oznaczonych jako publiczne (np. zapisanych przed sync). */
    suspend fun syncMyPublicAnimalsToFeed(uid: String) {
        userAnimals(uid).filter { it.isPublic }.forEach { syncPublicAnimalFeed(uid, it) }
    }

    /** Feed społeczności — kolekcja publicAnimals (bez collectionGroup, bez specjalnych indeksów). */
    suspend fun publicCommunityAnimals(limit: Long = 60): List<CommunityAnimalItem> {
        val snap = db.collection("publicAnimals").get().await()
        return snap.documents.mapNotNull { doc ->
            val ownerUid = doc.getString("ownerUid") ?: return@mapNotNull null
            CommunityAnimalItem(
                ownerUid = ownerUid,
                animal = doc.toAnimal(),
                ownerProfile = doc.ownerProfileFromFeed(ownerUid)
            )
        }
            .sortedByDescending { it.animal.createdAt }
            .take(limit.toInt())
    }

    suspend fun loadCommunityFeed(myUid: String?, limit: Long = 60): List<CommunityAnimalItem> {
        if (!myUid.isNullOrBlank()) {
            runCatching { publishPublicProfile(myUid) }
            runCatching { syncMyPublicAnimalsToFeed(myUid) }
        }
        return publicCommunityAnimals(limit)
    }
}

private fun List<*>?.toRemoteUrlList(): List<String> =
    this?.mapNotNull { it as? String }?.filter { it.startsWith("http://") || it.startsWith("https://") }
        ?: emptyList()

private fun isRemoteUrl(url: String?): Boolean =
    url?.startsWith("http://") == true || url?.startsWith("https://") == true

internal data class CachedMedia(val file: File, val mimeType: String)

private const val FIREBASE_PROJECT_ID = "spiderzone-d112d"

private fun firebaseStorageBucketCandidates(): List<String> {
    val fromConfig = FirebaseApp.getInstance().options.storageBucket?.trim().orEmpty()
    val defaults = listOf(
        "$FIREBASE_PROJECT_ID.firebasestorage.app",
        "$FIREBASE_PROJECT_ID.appspot.com"
    )
    return buildList {
        if (fromConfig.isNotEmpty()) add(fromConfig)
        defaults.forEach { bucket -> if (!contains(bucket)) add(bucket) }
    }
}

private fun firebaseStorageForBucket(bucket: String): FirebaseStorage =
    Firebase.storage("gs://$bucket")

private fun isStorageNotFoundError(error: Throwable): Boolean {
    val root = generateSequence(error) { it.cause }.last()
    if (root is StorageException) {
        if (root.httpResultCode == 404) return true
        if (root.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) return true
        if (root.errorCode == -13011) return true // ERROR_BUCKET_NOT_FOUND
        if (root.errorCode == -13012) return true // ERROR_PROJECT_NOT_FOUND
    }
    val msg = root.message.orEmpty()
    return msg.contains("404", ignoreCase = true) &&
        msg.contains("not found", ignoreCase = true)
}

private fun defaultMimeForFolder(subfolder: String): String =
    if (subfolder == "photos") "image/jpeg" else "video/mp4"

private fun resolveMimeType(context: Context, uri: Uri, subfolder: String): String {
    context.contentResolver.getType(uri)?.takeIf { it.isNotBlank() }?.let { return it }
  return when (uri.scheme?.lowercase()) {
        "file" -> {
            val name = uri.lastPathSegment.orEmpty().lowercase()
            when {
                name.endsWith(".png") -> "image/png"
                name.endsWith(".webp") -> "image/webp"
                name.endsWith(".gif") -> "image/gif"
                name.endsWith(".mp4") -> "video/mp4"
                name.endsWith(".mov") -> "video/quicktime"
                else -> defaultMimeForFolder(subfolder)
            }
        }
        else -> defaultMimeForFolder(subfolder)
    }
}

internal fun importPickedMedia(context: Context, uri: Uri, subfolder: String): CachedMedia {
    val mime = resolveMimeType(context, uri, subfolder)
    val ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mime)
        ?: if (subfolder == "photos") "jpg" else "mp4"
    val dir = File(context.cacheDir, "picked_$subfolder").apply { mkdirs() }
    val dest = File(dir, "${UUID.randomUUID()}.$ext")

    fun copyFrom(input: InputStream) {
        input.use { stream ->
            dest.outputStream().use { output -> stream.copyTo(output) }
        }
    }

    val copied = runCatching {
        when {
            uri.scheme.equals("file", ignoreCase = true) -> {
                val path = uri.path ?: error("Nieprawidlowa sciezka pliku")
                copyFrom(File(path).inputStream())
            }
            else -> {
                val fromDescriptor = context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                    copyFrom(FileInputStream(pfd.fileDescriptor))
                }
                if (fromDescriptor == null) {
                    val stream = context.contentResolver.openInputStream(uri)
                        ?: error("Nie mozna odczytac pliku. Wybierz zdjecie ponownie.")
                    copyFrom(stream)
                }
            }
        }
    }

    if (copied.isFailure || !dest.exists() || dest.length() <= 0L) {
        copied.exceptionOrNull()?.let { throw it }
    }

    if (!dest.exists() || dest.length() <= 0L) {
        dest.delete()
        error("Nie udalo sie skopiowac pliku. Wybierz zdjecie ponownie.")
    }
    return CachedMedia(dest, mime)
}

private suspend fun uploadCachedMediaToStorage(
    media: CachedMedia,
    storagePath: String
): com.google.firebase.storage.StorageReference {
    if (!media.file.exists() || media.file.length() <= 0L) {
        error("Plik lokalny nie istnieje lub jest pusty")
    }
    val metadata = StorageMetadata.Builder().setContentType(media.mimeType).build()
    val fileUri = Uri.fromFile(media.file)
    var lastError: Throwable? = null
    for (bucket in firebaseStorageBucketCandidates()) {
        try {
            val ref = firebaseStorageForBucket(bucket).reference.child(storagePath)
            ref.putFile(fileUri, metadata).await()
            return ref
        } catch (e: Throwable) {
            lastError = e
            if (!isStorageNotFoundError(e)) break
        }
    }
    throw lastError ?: IllegalStateException("Nie udalo sie wyslac pliku do Firebase Storage")
}

internal fun mediaUploadErrorMessage(error: Throwable): String {
    val root = generateSequence(error) { it.cause }.last()
    if (root is StorageException) {
        if (isStorageNotFoundError(root)) {
            return storageNotFoundHelpMessage()
        }
        return when (root.errorCode) {
            StorageException.ERROR_NOT_AUTHORIZED ->
                "Brak uprawnien do Firebase Storage. Wgraj storage.rules z projektu."
            else -> root.message ?: "Blad Firebase Storage (${root.errorCode})"
        }
    }
    if (isStorageNotFoundError(root)) {
        return storageNotFoundHelpMessage()
    }
    val msg = root.message.orEmpty()
    return when {
        msg.contains("404", ignoreCase = true) && msg.contains("not found", ignoreCase = true) ->
            "Zdjecie nie jest dostepne (404). Wybierz plik z pamieci telefonu, nie tylko z chmury."
        msg.contains("Nie mozna odczytac", ignoreCase = true) -> msg
        else -> msg.ifBlank { "Nie udalo sie dodac pliku" }
    }
}

private fun storageNotFoundHelpMessage(): String =
    "Firebase Storage nie jest dostepny (404). W Firebase Console otworz Storage → Get started / Rozpocznij, " +
        "wybierz lokalizacje Europy, potem wklej i opublikuj storage.rules z projektu."

private fun grantPersistableRead(context: Context, uri: Uri) {
    runCatching {
        context.contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
}

private fun DocumentSnapshot.ownerProfileFromFeed(uid: String): PublicUserProfile {
    val nickname = getString("ownerNickname").orEmpty()
    val avatarUrl = getString("ownerAvatarUrl").orEmpty()
    return PublicUserProfile(
        uid = uid,
        nickname = nickname,
        firstName = getString("ownerFirstName").orEmpty(),
        lastName = "",
        avatarUrl = avatarUrl,
        bio = "",
        createdAt = getLong("ownerCreatedAt") ?: 0L
    ).let { profile ->
        if (profile.nickname.isBlank() && profile.avatarUrl.isBlank()) {
            PublicUserProfile(uid = uid, nickname = "Hodowca")
        } else {
            profile
        }
    }
}

private fun DocumentSnapshot.toPublicUserProfile(uid: String) = PublicUserProfile(
    uid = uid,
    nickname = getString("nickname").orEmpty(),
    firstName = getString("firstName").orEmpty(),
    lastName = getString("lastName").orEmpty(),
    avatarUrl = getString("avatarUrl").orEmpty(),
    bio = getString("bio").orEmpty(),
    createdAt = getLong("createdAt") ?: 0L
)

private fun DocumentSnapshot.toUserProfile(uid: String) = UserProfile(
    uid = uid,
    email = getString("email").orEmpty(),
    nickname = getString("nickname").orEmpty(),
    firstName = getString("firstName").orEmpty(),
    lastName = getString("lastName").orEmpty(),
    breederLevel = getString("breederLevel").orEmpty(),
    avatarUrl = getString("avatarUrl").orEmpty(),
    bio = getString("bio").orEmpty(),
    createdAt = getLong("createdAt") ?: 0L
)

private fun DocumentSnapshot.toSpecies() = Species(
    id = id,
    latinName = getString("latinName").orEmpty(),
    commonName = getString("commonName").orEmpty(),
    category = getString("category").orEmpty(),
    occurrence = getString("occurrence").orEmpty(),
    lifeMode = getString("lifeMode").orEmpty(),
    temperatureDay = getString("temperatureDay").orEmpty(),
    temperatureNight = getString("temperatureNight").orEmpty(),
    humidity = getString("humidity").orEmpty(),
    sizeMale = getString("sizeMale").orEmpty(),
    sizeFemale = getString("sizeFemale").orEmpty(),
    temperament = getString("temperament").orEmpty(),
    venom = getString("venom").orEmpty(),
    difficulty = getString("difficulty").orEmpty(),
    cites = getString("cites").orEmpty(),
    diet = getString("diet").orEmpty(),
    documentedBreeding = getString("documentedBreeding").orEmpty(),
    careNotes = getString("careNotes").orEmpty(),
    funFact = getString("funFact").orEmpty(),
    imageUrl = getString("imageUrl").orEmpty(),
    phylum = getString("phylum").orEmpty(),
    taxonomicClass = getString("taxonomicClass").orEmpty(),
    suborder = getString("suborder").orEmpty(),
    family = getString("family").orEmpty(),
    temperature = getString("temperature").orEmpty(),
    lifespan = getString("lifespan").orEmpty(),
    size = getString("size").orEmpty(),
    origin = getString("origin").orEmpty()
)

private fun DocumentSnapshot.toAnimal(): Animal {
    val photoUrls = (get("photoUrls") as? List<*>)?.toRemoteUrlList() ?: emptyList()
    val videoUrls = (get("videoUrls") as? List<*>)?.toRemoteUrlList() ?: emptyList()
    val latin = getString("speciesLatinName").orEmpty()
    val legacyLabel = getString("speciesLabel").orEmpty()
    val common = getString("speciesCommonName").orEmpty()
    return Animal(
        id = id,
        name = getString("name").orEmpty(),
        speciesLatinName = latin.ifBlank { legacyLabel },
        speciesCommonName = common,
        speciesId = getString("speciesId").orEmpty(),
        speciesSource = getString("speciesSource").orEmpty().ifBlank { "legacy" },
        speciesLabel = legacyLabel,
        stage = getString("stage").orEmpty(),
        sex = getString("sex").orEmpty(),
        notes = getString("notes").orEmpty(),
        birthDate = getString("birthDate").orEmpty(),
        ownedSince = getString("ownedSince").orEmpty(),
        photoUrls = photoUrls,
        videoUrls = videoUrls,
        isPublic = getBoolean("isPublic") ?: false,
        createdAt = getLong("createdAt") ?: 0L,
        updatedAt = getLong("updatedAt") ?: 0L
    )
}

private fun Animal.toFirestoreMap(): HashMap<String, Any?> = hashMapOf(
    "name" to name,
    "speciesLatinName" to speciesLatinName,
    "speciesCommonName" to speciesCommonName,
    "speciesId" to speciesId,
    "speciesSource" to speciesSource,
    "speciesLabel" to speciesLabel.ifBlank { speciesCommonName.ifBlank { speciesLatinName } },
    "stage" to stage,
    "sex" to sex,
    "notes" to notes,
    "birthDate" to birthDate,
    "ownedSince" to ownedSince,
    "photoUrls" to photoUrls,
    "videoUrls" to videoUrls,
    "isPublic" to isPublic,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt
)

private fun Animal.toPublicFeedMap(ownerUid: String, owner: PublicUserProfile? = null): Map<String, Any> = buildMap {
    put("ownerUid", ownerUid)
    owner?.let {
        put("ownerNickname", it.nickname)
        put("ownerFirstName", it.firstName)
        put("ownerAvatarUrl", it.avatarUrl)
        put("ownerCreatedAt", it.createdAt)
    }
    put("name", name)
    put("speciesLatinName", speciesLatinName)
    put("speciesCommonName", speciesCommonName)
    put("speciesId", speciesId)
    put("speciesSource", speciesSource)
    put("speciesLabel", speciesLabel.ifBlank { speciesCommonName.ifBlank { speciesLatinName } })
    put("stage", stage)
    put("sex", sex)
    put("notes", notes)
    put("birthDate", birthDate)
    put("ownedSince", ownedSince)
    put("photoUrls", photoUrls)
    put("videoUrls", videoUrls)
    put("isPublic", isPublic)
    put("createdAt", createdAt)
    put("updatedAt", updatedAt)
}

private fun DocumentSnapshot.toReminder() = Reminder(
    id = id,
    title = getString("title").orEmpty(),
    dueEpochMillis = getLong("dueEpochMillis") ?: 0L
)

private enum class Tab { HOME, SPECIES, COLLECTION, COMMUNITY, REMINDERS, PROFILE }

private sealed interface SpeciesSearchHit {
    val latinName: String
    val commonName: String
    val sourceLabel: String

    data class Firestore(val species: Species) : SpeciesSearchHit {
        override val latinName: String get() = species.latinName
        override val commonName: String get() = species.commonName
        override val sourceLabel: String get() = "Baza Firestore"
    }

    data class Taxonomy(val node: TaxonomyNode) : SpeciesSearchHit {
        override val latinName: String get() = node.title
        override val commonName: String get() = node.subtitle.orEmpty()
        override val sourceLabel: String get() = "Baza ptasnikow"
    }
}

private fun searchSpeciesCatalog(allSpecies: List<Species>, query: String): List<SpeciesSearchHit> {
    val q = query.trim().lowercase()
    if (q.isEmpty()) return emptyList()
    return allSpecies
        .filter {
            it.latinName.lowercase().contains(q) || it.commonName.lowercase().contains(q)
        }
        .sortedBy { it.latinName }
        .distinctBy { it.latinName.lowercase() }
        .take(40)
        .map { SpeciesSearchHit.Firestore(it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpiderZoneApp(
    themeMode: AppThemeMode,
    onThemeModeChange: (AppThemeMode) -> Unit,
    repository: SpiderZoneRepository = remember { SpiderZoneRepository() }
) {
    var currentTab by remember { mutableStateOf(Tab.HOME) }
    var species by remember { mutableStateOf<List<Species>>(emptyList()) }
    var catalogSpecies by remember { mutableStateOf<List<Species>>(emptyList()) }
    var animals by remember { mutableStateOf<List<Animal>>(emptyList()) }
    var reminders by remember { mutableStateOf<List<Reminder>>(emptyList()) }
    var filterDifficulty by remember { mutableStateOf("all") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val authPreferences = remember(context) { AuthPreferences(context) }
    var authEmail by remember { mutableStateOf(authPreferences.getSavedEmail()) }
    var authPassword by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(authPreferences.getRememberMe()) }
    var authEmailError by remember { mutableStateOf<String?>(null) }
    var authPasswordError by remember { mutableStateOf<String?>(null) }
    var authGeneralError by remember { mutableStateOf<String?>(null) }
    var authLoading by remember { mutableStateOf(false) }
    var authResetLoading by remember { mutableStateOf(false) }
    var authModeLogin by remember { mutableStateOf(true) }
    var authGateVersion by remember { mutableStateOf(0) }
    var authNickname by remember { mutableStateOf("") }
    var authFirstName by remember { mutableStateOf("") }
    var authLastName by remember { mutableStateOf("") }
    var authBreederLevel by remember { mutableStateOf("poczatkujacy") }
    var authAvatarUri by remember { mutableStateOf<Uri?>(null) }
    var communityFeed by remember { mutableStateOf<List<CommunityAnimalItem>>(emptyList()) }
    var communityLoading by remember { mutableStateOf(false) }
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var pendingSpeciesDetail by remember { mutableStateOf<Species?>(null) }
    var pendingHomeSpeciesDetail by remember { mutableStateOf<Species?>(null) }
    var pendingCustomSpecies by remember { mutableStateOf<Animal?>(null) }
    var viewingPublicProfileUid by remember { mutableStateOf<String?>(null) }

    val taxonomyRoot = remember(catalogSpecies) {
        TerrariumSpeciesCatalog.buildTaxonomyTree(catalogSpecies)
    }

    fun openSpeciesForAnimal(animal: Animal) {
        val latin = animal.speciesLatinName.ifBlank { animal.speciesLabel }
        species.find {
            (animal.speciesId.isNotBlank() && it.id == animal.speciesId) ||
                (latin.isNotBlank() && it.latinName.equals(latin, ignoreCase = true))
        }?.let {
            pendingHomeSpeciesDetail = it
            pendingSpeciesDetail = null
            pendingCustomSpecies = null
            currentTab = Tab.HOME
            return
        }
        if (latin.isNotBlank() || animal.speciesCommonName.isNotBlank()) {
            pendingCustomSpecies = animal
            pendingSpeciesDetail = null
            pendingHomeSpeciesDetail = null
            currentTab = Tab.SPECIES
            return
        }
        scope.launch {
            snackbarHostState.showSnackbar("Brak danych o gatunku tego pupila")
        }
    }

    LaunchedEffect(Unit) {
        if (!authPreferences.getRememberMe() && repository.currentUserId() != null) {
            repository.signOut()
            authGateVersion++
        }
    }

    LaunchedEffect(Unit) {
        catalogSpecies = withContext(Dispatchers.IO) {
            runCatching { TerrariumSpeciesCatalog.load(context) }.getOrDefault(emptyList())
        }
    }

    LaunchedEffect(repository.currentUserId(), authGateVersion, catalogSpecies) {
        if (catalogSpecies.isEmpty()) return@LaunchedEffect
        val uid = repository.currentUserId()
        species = if (uid == null) {
            catalogSpecies
        } else {
            val email = Firebase.auth.currentUser?.email.orEmpty()
            repository.ensureUserProfile(uid, email)
            SeedData.seedSpeciesIfEmpty()
            val firestoreSpecies = runCatching { repository.species() }.getOrDefault(emptyList())
            animals = repository.userAnimals(uid)
            reminders = repository.reminders(uid)
            userProfile = repository.getUserProfile(uid)
            runCatching { repository.publishPublicProfile(uid) }
            mergeSpeciesLists(catalogSpecies, firestoreSpecies)
        }
    }

    LaunchedEffect(repository.currentUserId(), authGateVersion, currentTab) {
        if (repository.currentUserId() == null) return@LaunchedEffect
        if (currentTab != Tab.COMMUNITY) return@LaunchedEffect
        communityLoading = true
        try {
            communityFeed = repository.loadCommunityFeed(repository.currentUserId())
        } catch (e: Exception) {
            snackbarHostState.showSnackbar(communityLoadErrorMessage(e))
            communityFeed = emptyList()
        } finally {
            communityLoading = false
        }
    }

    if (repository.currentUserId() == null) {
        Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        AuthScreen(
            modifier = Modifier.padding(padding),
            email = authEmail,
            password = authPassword,
            emailError = authEmailError,
            passwordError = authPasswordError,
            generalError = authGeneralError,
            rememberMe = rememberMe,
            loading = authLoading,
            resetLoading = authResetLoading,
            loginMode = authModeLogin,
            nickname = authNickname,
            firstName = authFirstName,
            lastName = authLastName,
            breederLevel = authBreederLevel,
            avatarUri = authAvatarUri,
            onEmailChange = {
                authEmail = it
                authEmailError = null
                authGeneralError = null
            },
            onPasswordChange = {
                authPassword = it
                authPasswordError = null
                authGeneralError = null
            },
            onRememberMeChange = { rememberMe = it },
            onNicknameChange = { authNickname = it },
            onFirstNameChange = { authFirstName = it },
            onLastNameChange = { authLastName = it },
            onBreederLevelChange = { authBreederLevel = it },
            onAvatarSelected = { authAvatarUri = it },
            onToggleMode = {
                authModeLogin = !authModeLogin
                authEmailError = null
                authPasswordError = null
                authGeneralError = null
            },
            onSubmit = authSubmit@{
                authEmailError = null
                authPasswordError = null
                authGeneralError = null
                val trimmedEmail = authEmail.trim()
                val trimmedPassword = authPassword.trim()
                val emailValidation = validateAuthEmail(trimmedEmail)
                val passwordValidation = validateAuthPassword(trimmedPassword, forSignUp = !authModeLogin)
                if (emailValidation != null) authEmailError = emailValidation
                if (passwordValidation != null) authPasswordError = passwordValidation
                if (emailValidation != null || passwordValidation != null) return@authSubmit

                authLoading = true
                try {
                    if (authModeLogin) {
                        repository.signIn(trimmedEmail, trimmedPassword)
                        authPreferences.saveLogin(trimmedEmail, rememberMe)
                        authGateVersion++
                    } else {
                        val profileData = SignUpProfileData(
                            nickname = authNickname.trim(),
                            firstName = authFirstName.trim(),
                            lastName = authLastName.trim(),
                            breederLevel = authBreederLevel
                        )
                        require(profileData.nickname.isNotBlank()) { "Podaj pseudonim" }
                        require(profileData.firstName.isNotBlank()) { "Podaj imie" }
                        require(profileData.lastName.isNotBlank()) { "Podaj nazwisko" }
                        repository.signUp(trimmedEmail, trimmedPassword)
                        runCatching {
                            repository.sendEmailVerification()
                        }.onFailure { sendError ->
                            val details = (sendError as? FirebaseAuthException)?.errorCode
                                ?: sendError.message
                                ?: "UNKNOWN"
                            snackbarHostState.showSnackbar("Nie udalo sie wyslac maila weryfikacyjnego: $details")
                        }
                        val uid = repository.currentUserId() ?: error("Brak uid po rejestracji")
                        runCatching {
                            repository.createUserProfile(
                                context = context,
                                uid = uid,
                                email = trimmedEmail,
                                data = profileData,
                                avatarUri = authAvatarUri
                            )
                        }.onFailure {
                            snackbarHostState.showSnackbar("Konto utworzone, ale profil nie zapisal sie poprawnie.")
                        }
                        authPreferences.saveLogin(trimmedEmail, rememberMe)
                        authGateVersion++
                        snackbarHostState.showSnackbar("Konto utworzone. Sprawdz email i potwierdz konto.")
                    }
                } catch (e: Exception) {
                    if (e is IllegalArgumentException || e is IllegalStateException) {
                        authGeneralError = e.message
                    } else {
                        val fieldErrors = mapFirebaseAuthError(e)
                        authEmailError = fieldErrors.email
                        authPasswordError = fieldErrors.password
                        authGeneralError = fieldErrors.general
                    }
                } finally {
                    authLoading = false
                }
            },
            onResetPassword = authReset@{
                authEmailError = null
                authGeneralError = null
                val trimmedEmail = authEmail.trim()
                val emailValidation = validateAuthEmail(trimmedEmail)
                if (emailValidation != null) {
                    authEmailError = emailValidation
                    return@authReset
                }
                authResetLoading = true
                try {
                    repository.resetPassword(trimmedEmail)
                    snackbarHostState.showSnackbar("Wyslano link resetujacy haslo na podany email")
                } catch (e: Exception) {
                    val fieldErrors = mapFirebaseAuthError(e)
                    authEmailError = fieldErrors.email ?: fieldErrors.general
                    authGeneralError = if (fieldErrors.email == null) fieldErrors.general else null
                } finally {
                    authResetLoading = false
                }
            }
        )
        }
        return
    }

    if (!repository.isCurrentUserEmailVerified()) {
        Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        VerifyEmailScreen(
            modifier = Modifier.padding(padding),
            onResend = {
                runCatching {
                    repository.sendEmailVerification()
                    snackbarHostState.showSnackbar("Wyslano ponownie email weryfikacyjny.")
                }.onFailure {
                    val details = (it as? FirebaseAuthException)?.errorCode ?: it.message ?: "Blad wysylki"
                    snackbarHostState.showSnackbar("Blad wysylki: $details")
                }
            },
            onRefreshStatus = {
                runCatching {
                    repository.reloadCurrentUser()
                    authGateVersion++
                    if (repository.isCurrentUserEmailVerified()) {
                        snackbarHostState.showSnackbar("Email zweryfikowany.")
                    } else {
                        snackbarHostState.showSnackbar("Email nadal niezweryfikowany.")
                    }
                }.onFailure { snackbarHostState.showSnackbar(it.message ?: "Blad odswiezania") }
            },
            onSignOut = {
                repository.signOut()
                authGateVersion++
            }
        )
        }
        return
    }

    val filteredSpecies = species.filter {
        filterDifficulty == "all" || it.difficulty.equals(filterDifficulty, ignoreCase = true)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomAppBar {
                NavigationBarItem(selected = currentTab == Tab.HOME, onClick = { currentTab = Tab.HOME }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") })
                NavigationBarItem(selected = currentTab == Tab.SPECIES, onClick = { currentTab = Tab.SPECIES }, icon = { Icon(Icons.Default.Search, null) }, label = { Text("Gatunki") })
                NavigationBarItem(selected = currentTab == Tab.COLLECTION, onClick = { currentTab = Tab.COLLECTION }, icon = { Icon(Icons.Default.Pets, null) }, label = { Text("Hodowla") })
                NavigationBarItem(selected = currentTab == Tab.COMMUNITY, onClick = { currentTab = Tab.COMMUNITY }, icon = { Icon(Icons.Default.People, null) }, label = { Text("Spolecznosc") })
                NavigationBarItem(selected = currentTab == Tab.REMINDERS, onClick = { currentTab = Tab.REMINDERS }, icon = { Icon(Icons.Default.Notifications, null) }, label = { Text("Przypomnienia") })
                NavigationBarItem(selected = currentTab == Tab.PROFILE, onClick = { currentTab = Tab.PROFILE }, icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profil") })
            }
        }
    ) { padding ->
        when (currentTab) {
            Tab.HOME -> HomeScreen(
                padding = padding,
                speciesCount = species.size,
                animalCount = animals.size,
                catalogSpecies = catalogSpecies,
                allSpecies = species,
                openSpeciesDetail = pendingHomeSpeciesDetail,
                onOpenSpeciesDetailConsumed = { pendingHomeSpeciesDetail = null }
            )
            Tab.SPECIES -> SpeciesScreen(
                padding = padding,
                species = filteredSpecies,
                allSpecies = species,
                openSpeciesDetail = pendingSpeciesDetail,
                openCustomSpecies = pendingCustomSpecies,
                onOpenSpeciesDetailConsumed = {
                    pendingSpeciesDetail = null
                    pendingCustomSpecies = null
                },
                onFilterChange = { filterDifficulty = it }
            )
            Tab.COLLECTION -> CollectionScreen(
                padding = padding,
                species = species,
                animals = animals,
                onSubmitAnimal = { ctx, draft, photoUris, videoUris ->
                    val uid = repository.currentUserId() ?: throw IllegalStateException("Brak konta")
                    repository.createAnimalWithMedia(ctx, uid, draft, photoUris, videoUris)
                    animals = repository.userAnimals(uid)
                },
                onUpdateAnimal = { ctx, existing, draft, photoUris, videoUris ->
                    val uid = repository.currentUserId() ?: throw IllegalStateException("Brak konta")
                    repository.updateAnimalWithMedia(ctx, uid, existing, draft, photoUris, videoUris)
                    animals = repository.userAnimals(uid)
                },
                onNotify = { msg ->
                    scope.launch {
                        snackbarHostState.showSnackbar(msg)
                    }
                }
            )
            Tab.COMMUNITY -> CommunityScreen(
                padding = padding,
                myUid = repository.currentUserId().orEmpty(),
                items = communityFeed,
                loading = communityLoading,
                repository = repository,
                onOpenSpecies = { animal -> openSpeciesForAnimal(animal) },
                onOpenOwnerProfile = { uid -> viewingPublicProfileUid = uid },
                onRefresh = {
                    scope.launch {
                        communityLoading = true
                        try {
                            communityFeed = repository.loadCommunityFeed(repository.currentUserId())
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar(communityLoadErrorMessage(e))
                            communityFeed = emptyList()
                        } finally {
                            communityLoading = false
                        }
                    }
                }
            )
            Tab.REMINDERS -> RemindersScreen(
                padding = padding,
                reminders = reminders,
                onAddReminder = { reminder ->
                    val uid = repository.currentUserId() ?: return@RemindersScreen
                    runCatching {
                        repository.addReminder(uid, reminder)
                        reminders = repository.reminders(uid)
                        scheduleReminder(context, reminder)
                    }
                }
            )
            Tab.PROFILE -> ProfileScreen(
                padding = padding,
                uid = repository.currentUserId().orEmpty(),
                email = Firebase.auth.currentUser?.email.orEmpty(),
                profile = userProfile,
                animals = animals,
                repository = repository,
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange,
                onProfileUpdated = { userProfile = it },
                onNotify = { msg ->
                    scope.launch { snackbarHostState.showSnackbar(msg) }
                },
                onSignOut = {
                    repository.signOut()
                    authGateVersion++
                }
            )
        }

        viewingPublicProfileUid?.let { ownerUid ->
            PublicUserProfileOverlay(
                ownerUid = ownerUid,
                myUid = repository.currentUserId().orEmpty(),
                repository = repository,
                onDismiss = { viewingPublicProfileUid = null },
                onOpenSpecies = { animal -> openSpeciesForAnimal(animal) }
            )
        }
    }
}

@Composable
private fun AuthScreen(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    emailError: String?,
    passwordError: String?,
    generalError: String?,
    rememberMe: Boolean,
    loading: Boolean,
    resetLoading: Boolean,
    loginMode: Boolean,
    nickname: String,
    firstName: String,
    lastName: String,
    breederLevel: String,
    avatarUri: Uri?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onNicknameChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onBreederLevelChange: (String) -> Unit,
    onAvatarSelected: (Uri?) -> Unit,
    onToggleMode: () -> Unit,
    onSubmit: suspend () -> Unit,
    onResetPassword: suspend () -> Unit
) {
    val scope = rememberCoroutineScope()
    val avatarPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        onAvatarSelected(uri)
    }
    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("SpiderZone", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            if (generalError != null) {
                Text(
                    generalError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
            }
            AppOutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email",
                errorMessage = emailError
            )
            Spacer(Modifier.height(8.dp))
            AppOutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Haslo",
                errorMessage = passwordError
            )
            if (loginMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = onRememberMeChange
                    )
                    Text(
                        "Zapamietaj mnie",
                        modifier = Modifier.clickable { onRememberMeChange(!rememberMe) }
                    )
                }
            }
            if (!loginMode) {
                Spacer(Modifier.height(8.dp))
                AppOutlinedTextField(value = nickname, onValueChange = onNicknameChange, label = "Pseudonim")
                Spacer(Modifier.height(8.dp))
                AppOutlinedTextField(value = firstName, onValueChange = onFirstNameChange, label = "Imie")
                Spacer(Modifier.height(8.dp))
                AppOutlinedTextField(value = lastName, onValueChange = onLastNameChange, label = "Nazwisko")
                Spacer(Modifier.height(8.dp))
                Text("Stopien zaawansowania hodowcy")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("poczatkujacy", "sredniozaawansowany", "zaawansowany").forEach { level ->
                        AssistChip(onClick = { onBreederLevelChange(level) }, label = { Text(level) })
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text("Wybrany avatar: ${avatarUri?.lastPathSegment ?: "brak"}", color = SpiderZoneColors.TextSecondary)
                TextButton(onClick = { avatarPicker.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Dodaj avatar")
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { scope.launchCatching(onSubmit) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && !resetLoading
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.height(20.dp))
                } else {
                    Text(if (loginMode) "Zaloguj" else "Zarejestruj")
                }
            }
            TextButton(onClick = onToggleMode, modifier = Modifier.fillMaxWidth()) {
                Text(if (loginMode) "Nie masz konta? Rejestracja" else "Masz konto? Logowanie")
            }
            if (loginMode) {
                TextButton(
                    onClick = { scope.launchCatching(onResetPassword) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading && !resetLoading
                ) {
                    Text(if (resetLoading) "Wysylanie..." else "Reset hasla")
                }
            }
        }
}

@Composable
private fun VerifyEmailScreen(
    modifier: Modifier = Modifier,
    onResend: suspend () -> Unit,
    onRefreshStatus: suspend () -> Unit,
    onSignOut: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Potwierdz email", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Aby korzystac z aplikacji, potwierdz adres email przez link wyslany na skrzynke.",
                color = SpiderZoneColors.TextSecondary
            )
            Spacer(Modifier.height(12.dp))
            Button(onClick = { scope.launchCatching(onResend) }, modifier = Modifier.fillMaxWidth()) {
                Text("Wyslij email ponownie")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { scope.launchCatching(onRefreshStatus) }, modifier = Modifier.fillMaxWidth()) {
                Text("Sprawdz status weryfikacji")
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) {
                Text("Wyloguj")
            }
    }
}

@Composable
private fun HomeScreen(
    padding: PaddingValues,
    speciesCount: Int,
    animalCount: Int,
    catalogSpecies: List<Species>,
    allSpecies: List<Species>,
    openSpeciesDetail: Species? = null,
    onOpenSpeciesDetailConsumed: () -> Unit = {}
) {
    val taxonomyRoot = remember(catalogSpecies) {
        TerrariumSpeciesCatalog.buildTaxonomyTree(catalogSpecies)
    }
    var taxonomyStack by remember(catalogSpecies) { mutableStateOf(listOf(taxonomyRoot)) }
    var selectedSpeciesDetail by remember { mutableStateOf<Species?>(null) }
    val taxonomyCurrent = taxonomyStack.last()

    fun findSpeciesByLatin(latin: String): Species? =
        allSpecies.find { it.latinName.equals(latin.trim(), ignoreCase = true) }

    LaunchedEffect(catalogSpecies, taxonomyRoot) {
        if (catalogSpecies.isNotEmpty()) {
            taxonomyStack = listOf(taxonomyRoot)
        }
    }

    LaunchedEffect(openSpeciesDetail) {
        if (openSpeciesDetail != null) {
            selectedSpeciesDetail = openSpeciesDetail
            taxonomyStack = listOf(taxonomyRoot)
            onOpenSpeciesDetailConsumed()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp)
    ) {
        Text("SpiderZone", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Platforma hodowli egzotycznych zwierzat.", color = SpiderZoneColors.TextSecondary)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Gatunki w bazie", speciesCount.toString(), modifier = Modifier.weight(1f))
                StatCard("Twoje zwierzeta", animalCount.toString(), modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Taksonomia terrarium",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Gromada → rząd → podrząd → rodzina → gatunek (${catalogSpecies.size} gatunków z bazy).",
                style = MaterialTheme.typography.bodySmall,
                color = SpiderZoneColors.TextSecondary
            )
            Spacer(Modifier.height(8.dp))

            when {
                selectedSpeciesDetail != null -> {
                    SpeciesDetailScreen(
                        species = selectedSpeciesDetail!!,
                        onBack = { selectedSpeciesDetail = null },
                        modifier = Modifier.weight(1f)
                    )
                }
                else -> {
                    if (taxonomyStack.size > 1) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { taxonomyStack = taxonomyStack.dropLast(1) }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wstecz")
                            }
                            Text(
                                taxonomyStack.drop(1).joinToString(" › ") { it.title },
                                style = MaterialTheme.typography.bodySmall,
                                color = SpiderZoneColors.TextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    if (catalogSpecies.isEmpty()) {
                        Text(
                            "Ładowanie katalogu gatunków…",
                            color = SpiderZoneColors.TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else if (taxonomyCurrent.children.isEmpty()) {
                        Text(
                            "Brak podkategorii na tym poziomie — wróć wyżej.",
                            color = SpiderZoneColors.TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                items = taxonomyCurrent.children,
                                key = { child ->
                                    taxonomyStack.joinToString("/") { it.title } + "/" + child.title
                                }
                            ) { node ->
                                TaxonomyBoxCard(node) {
                                    if (node.isLeaf) {
                                        selectedSpeciesDetail = findSpeciesByLatin(node.title)
                                    } else {
                                        taxonomyStack = taxonomyStack + node
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
}

@Composable
private fun TaxonomyBoxCard(node: TaxonomyNode, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface)
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp)) {
            Text(node.title, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            if (!node.subtitle.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    node.subtitle!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = SpiderZoneColors.TextSecondary
                )
            }
            if (!node.isLeaf) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "Otwórz",
                    style = MaterialTheme.typography.labelMedium,
                    color = SpiderZoneColors.Primary
                )
            }
        }
    }
}

@Composable
private fun SpeciesScreen(
    padding: PaddingValues,
    species: List<Species>,
    allSpecies: List<Species>,
    openSpeciesDetail: Species?,
    openCustomSpecies: Animal?,
    onOpenSpeciesDetailConsumed: () -> Unit,
    onFilterChange: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf<Species?>(null) }
    var customSpeciesView by remember { mutableStateOf<Animal?>(null) }

    LaunchedEffect(openSpeciesDetail, openCustomSpecies) {
        when {
            openSpeciesDetail != null -> {
                selectedSpecies = openSpeciesDetail
                customSpeciesView = null
                searchQuery = openSpeciesDetail.latinName
                onOpenSpeciesDetailConsumed()
            }
            openCustomSpecies != null -> {
                customSpeciesView = openCustomSpecies
                selectedSpecies = null
                searchQuery = openCustomSpecies.speciesLatinName.ifBlank { openCustomSpecies.speciesLabel }
                onOpenSpeciesDetailConsumed()
            }
        }
    }

    val searchResults = remember(searchQuery, allSpecies) {
        searchSpeciesCatalog(allSpecies, searchQuery)
    }

    Box(Modifier.fillMaxSize().padding(padding)) {
        when {
            selectedSpecies != null -> {
                    SpeciesDetailScreen(
                        species = selectedSpecies!!,
                        onBack = { selectedSpecies = null },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                customSpeciesView != null -> {
                    val animal = customSpeciesView!!
                    Column(Modifier.fillMaxSize().padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { customSpeciesView = null }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wstecz")
                            }
                            Text("Gatunek pupila", style = MaterialTheme.typography.titleMedium)
                        }
                        Spacer(Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(animal.speciesLatinName.ifBlank { "—" }, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                                if (animal.speciesCommonName.isNotBlank()) {
                                    Text(animal.speciesCommonName, color = SpiderZoneColors.TextSecondary)
                                }
                                Text(
                                    "Wpis wlasny / spoza bazy Firestore. Szczegoly hodowlane uzupelnij w notatkach pupila.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SpiderZoneColors.TextSecondary
                                )
                            }
                        }
                    }
                }
                else -> {
                Column(Modifier.fillMaxSize().padding(12.dp)) {
                    Text("Gatunki", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    AppOutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = "Szukaj gatunku",
                        placeholder = "np. metallica, Brachypelma…"
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(onClick = { onFilterChange("all") }, label = { Text("Wszystkie") })
                        AssistChip(onClick = { onFilterChange("beginner") }, label = { Text("Poczatkujacy") })
                        AssistChip(onClick = { onFilterChange("advanced") }, label = { Text("Zaawansowani") })
                    }
                    Spacer(Modifier.height(10.dp))
                    if (searchQuery.isNotBlank()) {
                        Text(
                            "Wyniki (${searchResults.size}) — wpisuj litery, wyszukiwanie na biezaco",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (searchResults.isEmpty()) {
                                item {
                                    Text(
                                        "Brak pasujacych gatunkow. Sprobuj innej czesci nazwy lacinskiej lub zwyczajowej.",
                                        color = SpiderZoneColors.TextSecondary
                                    )
                                }
                            }
                            items(
                                searchResults,
                                key = { hit -> "${hit.sourceLabel}-${hit.latinName}" }
                            ) { hit ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            when (hit) {
                                                is SpeciesSearchHit.Firestore -> {
                                                    selectedSpecies = hit.species
                                                    customSpeciesView = null
                                                }
                                                is SpeciesSearchHit.Taxonomy -> {
                                                    selectedSpecies = allSpecies.find {
                                                        it.latinName.equals(hit.latinName, ignoreCase = true)
                                                    }
                                                    customSpeciesView = null
                                                }
                                            }
                                        },
                                    colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Column(Modifier.padding(14.dp)) {
                                        Text(hit.latinName, fontWeight = FontWeight.Bold)
                                        if (hit.commonName.isNotBlank()) {
                                            Text(hit.commonName, color = SpiderZoneColors.TextSecondary)
                                        }
                                        Text(
                                            hit.sourceLabel,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SpiderZoneColors.Primary
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text("Lista gatunkow", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        if (allSpecies.isEmpty()) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Baza Firestore jest pusta — wpisz nazwe w wyszukiwarce powyzej (szuka tez w bazie ptasnikow).",
                                style = MaterialTheme.typography.bodySmall,
                                color = SpiderZoneColors.TextSecondary
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (species.isEmpty()) {
                                item {
                                    Text(
                                        "Wpisz co najmniej jedna litere w polu szukaj, np. „bra” lub „rose”.",
                                        color = SpiderZoneColors.TextSecondary
                                    )
                                }
                            }
                            items(species, key = { it.id }) { s ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedSpecies = s },
                                    colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Column(Modifier.fillMaxWidth().padding(14.dp)) {
                                        Text(s.latinName, fontWeight = FontWeight.SemiBold)
                                        if (s.commonName.isNotBlank()) {
                                            Text(s.commonName, color = SpiderZoneColors.TextSecondary)
                                        }
                                        Text(
                                            speciesListSubtitle(s),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = SpiderZoneColors.TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                }
            }
        }
}

private fun speciesListSubtitle(species: Species): String {
    val careParts = listOfNotNull(
        species.difficulty.takeIf { it.isNotBlank() }?.let { "Zaawansowanie: $it" },
        species.displayTemperatureDay().takeIf { it.isNotBlank() }?.let { "Temp.: $it" },
        species.displayOccurrence().takeIf { it.isNotBlank() }?.let { "Występowanie: $it" }
    )
    if (careParts.isNotEmpty()) return careParts.joinToString(" · ")
    val taxParts = listOfNotNull(
        species.family.takeIf { it.isNotBlank() },
        species.phylum.takeIf { it.isNotBlank() }
    )
    return taxParts.joinToString(" · ").ifBlank { "Szczegóły hodowlane — do uzupełnienia" }
}

private const val SPECIES_FIELD_EMPTY = "—"

@Composable
private fun SpeciesDetailScreen(
    species: Species,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wstecz")
            }
            Text("Opis gatunku", style = MaterialTheme.typography.titleMedium)
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                if (isRemoteUrl(species.imageUrl)) {
                    RemoteImage(
                        url = species.imageUrl,
                        contentDescription = species.latinName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            item {
                SpeciesDetailCard(species)
            }
        }
    }
}

@Composable
private fun SpeciesFieldRow(label: String, value: String) {
    CommunityDetailRow(label, value.ifBlank { SPECIES_FIELD_EMPTY })
}

@Composable
private fun SpeciesDetailCard(species: Species) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(species.latinName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            if (species.commonName.isNotBlank()) {
                Text(species.commonName, style = MaterialTheme.typography.titleMedium, color = SpiderZoneColors.TextSecondary)
            }

            Text("Taksonomia", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            SpeciesFieldRow("Gromada", species.phylum)
            SpeciesFieldRow("Rząd", species.taxonomicClass)
            SpeciesFieldRow("Podrząd", species.suborder)
            SpeciesFieldRow("Rodzina", species.family)

            Spacer(Modifier.height(4.dp))
            Text("Parametry hodowlane", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            Text(
                "Pola puste (—) uzupełnimy w bazie. Występowanie — docelowo mapa.",
                style = MaterialTheme.typography.bodySmall,
                color = SpiderZoneColors.TextSecondary
            )
            SpeciesFieldRow("Występowanie", species.displayOccurrence())
            SpeciesFieldRow("Tryb życia", species.lifeMode)
            SpeciesFieldRow("Temperatura (dzień)", species.displayTemperatureDay())
            SpeciesFieldRow("Temperatura (noc)", species.temperatureNight)
            SpeciesFieldRow("Wilgotność", species.humidity)
            SpeciesFieldRow("Wielkość (samiec)", species.displaySizeMale())
            SpeciesFieldRow("Wielkość (samica)", species.sizeFemale)
            SpeciesFieldRow("Temperament", species.temperament)
            SpeciesFieldRow("Jad", species.venom)
            SpeciesFieldRow("Zaawansowanie", species.difficulty)
            SpeciesFieldRow("CITES", species.cites.ifBlank { SPECIES_FIELD_EMPTY }.let {
                when (it.lowercase()) {
                    "tak", "yes", "true" -> "Tak"
                    "nie", "no", "false" -> "Nie"
                    SPECIES_FIELD_EMPTY -> SPECIES_FIELD_EMPTY
                    else -> it
                }
            })
            SpeciesFieldRow("Dieta", species.diet)
            SpeciesFieldRow("Udokumentowane rozmnażenia", species.documentedBreeding)
            Text(
                "Wkrótce: zakładka Rozmnażanie — hodowcy będą mogli dokumentować próby i powiązać je z opisem gatunku.",
                style = MaterialTheme.typography.bodySmall,
                color = SpiderZoneColors.Primary
            )

            if (species.lifespan.isNotBlank()) {
                SpeciesFieldRow("Długość życia", species.lifespan)
            }
            if (species.careNotes.isNotBlank() && !species.careNotes.startsWith("Gromada:")) {
                Text("Uwagi", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(species.careNotes, color = SpiderZoneColors.TextSecondary)
            }
            if (species.funFact.isNotBlank()) {
                Text("Ciekawostka", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(species.funFact, color = SpiderZoneColors.Primary)
            }
        }
    }
}

internal sealed interface HodowlaWyborGatunku {
    data class Takson(val pick: TaxonomySpeciesPick) : HodowlaWyborGatunku
    data class BazaFirestore(val sp: Species) : HodowlaWyborGatunku
}

private data class GatunekPodpowiedz(val label: String, val wybor: HodowlaWyborGatunku)

private fun mergeSpeciesSuggestions(
    query: String,
    staticList: List<TaxonomySpeciesPick>,
    fs: List<Species>
): List<GatunekPodpowiedz> {
    val q = query.trim().lowercase()
    if (q.isEmpty()) return emptyList()
    val seen = mutableSetOf<String>()
    val out = mutableListOf<GatunekPodpowiedz>()
    fs.filter {
        it.latinName.lowercase().contains(q) || it.commonName.lowercase().contains(q)
    }.sortedBy { it.latinName }.forEach { s ->
        val key = s.latinName.lowercase()
        if (seen.add(key)) {
            val lab = buildString {
                append(s.latinName)
                if (s.commonName.isNotBlank()) append(" (${s.commonName})")
                append(" · Katalog")
            }
            out.add(GatunekPodpowiedz(lab, HodowlaWyborGatunku.BazaFirestore(s)))
        }
    }
    staticList.filter {
        it.latinName.lowercase().contains(q) ||
            (it.commonName?.lowercase()?.contains(q) == true)
    }.sortedBy { it.latinName }.forEach { t ->
        val key = t.latinName.lowercase()
        if (seen.add(key)) {
            val lab = buildString {
                append(t.latinName)
                t.commonName?.takeIf { it.isNotBlank() }?.let { append(" ($it)") }
                append(" · Katalog")
            }
            out.add(GatunekPodpowiedz(lab, HodowlaWyborGatunku.Takson(t)))
        }
    }
    return out.take(40)
}

internal data class HodowlaFormState(
    val editingAnimal: Animal?,
    val pseudonym: String,
    val wyborGatunku: HodowlaWyborGatunku?,
    val trybWlasnyGatunek: Boolean,
    val customLatin: String,
    val customCommon: String,
    val stage: String,
    val notes: String,
    val ownedSince: String,
    val isPublic: Boolean
)

/** Buduje draft pupila z formularza hodowli (dodawanie i edycja). */
internal fun buildAnimalDraftFromForm(form: HodowlaFormState): Animal? {
    if (form.pseudonym.isBlank()) return null
    val base = form.editingAnimal

    if (base != null && form.wyborGatunku == null && !form.trybWlasnyGatunek) {
        return base.copy(
            name = form.pseudonym.trim(),
            stage = form.stage.trim(),
            notes = form.notes.trim(),
            ownedSince = form.ownedSince.trim(),
            isPublic = form.isPublic
        )
    }

    if (form.trybWlasnyGatunek) {
        val latin = form.customLatin.trim().ifBlank {
            base?.speciesLatinName?.takeIf { it.isNotBlank() }
                ?: base?.speciesLabel?.takeIf { it.isNotBlank() }
                ?: ""
        }
        if (latin.isBlank()) return null
        val seed = base ?: Animal()
        return seed.copy(
            name = form.pseudonym.trim(),
            speciesLatinName = latin,
            speciesCommonName = form.customCommon.trim().ifBlank { base?.speciesCommonName.orEmpty() },
            speciesId = base?.speciesId.orEmpty(),
            speciesSource = "custom",
            speciesLabel = "",
            stage = form.stage.trim(),
            sex = base?.sex?.takeIf { it.isNotBlank() } ?: seed.sex,
            notes = form.notes.trim(),
            birthDate = base?.birthDate?.takeIf { it.isNotBlank() } ?: LocalDate.now().toString(),
            ownedSince = form.ownedSince.trim(),
            isPublic = form.isPublic
        )
    }

    return when (val w = form.wyborGatunku) {
        is HodowlaWyborGatunku.Takson -> {
            val seed = base ?: Animal()
            seed.copy(
                name = form.pseudonym.trim(),
                speciesLatinName = w.pick.latinName,
                speciesCommonName = w.pick.commonName.orEmpty(),
                speciesId = "",
                speciesSource = "taxonomy_static",
                speciesLabel = "",
                stage = form.stage.trim(),
                sex = base?.sex?.takeIf { it.isNotBlank() } ?: seed.sex,
                notes = form.notes.trim(),
                birthDate = base?.birthDate?.takeIf { it.isNotBlank() } ?: LocalDate.now().toString(),
                ownedSince = form.ownedSince.trim(),
                isPublic = form.isPublic
            )
        }
        is HodowlaWyborGatunku.BazaFirestore -> {
            val seed = base ?: Animal()
            seed.copy(
                name = form.pseudonym.trim(),
                speciesLatinName = w.sp.latinName,
                speciesCommonName = w.sp.commonName,
                speciesId = w.sp.id,
                speciesSource = "firestore_species",
                speciesLabel = "",
                stage = form.stage.trim(),
                sex = base?.sex?.takeIf { it.isNotBlank() } ?: seed.sex,
                notes = form.notes.trim(),
                birthDate = base?.birthDate?.takeIf { it.isNotBlank() } ?: LocalDate.now().toString(),
                ownedSince = form.ownedSince.trim(),
                isPublic = form.isPublic
            )
        }
        null -> null
    }
}

@Composable
private fun CollectionScreen(
    padding: PaddingValues,
    species: List<Species>,
    animals: List<Animal>,
    onSubmitAnimal: suspend (Context, Animal, List<CachedMedia>, List<CachedMedia>) -> Unit,
    onUpdateAnimal: suspend (Context, Animal, Animal, List<CachedMedia>, List<CachedMedia>) -> Unit,
    onNotify: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var pseudonym by remember { mutableStateOf("") }
    var speciesQuery by remember { mutableStateOf("") }
    var wyborGatunku by remember { mutableStateOf<HodowlaWyborGatunku?>(null) }
    var trybWlasnyGatunek by remember { mutableStateOf(false) }
    var customLatin by remember { mutableStateOf("") }
    var customCommon by remember { mutableStateOf("") }
    var stage by remember { mutableStateOf("L1") }
    var notes by remember { mutableStateOf("") }
    var ownedSince by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(false) }
    var pickedPhotos by remember { mutableStateOf<List<CachedMedia>>(emptyList()) }
    var pickedVideos by remember { mutableStateOf<List<CachedMedia>>(emptyList()) }
    var editingAnimal by remember { mutableStateOf<Animal?>(null) }
    var viewingAnimal by remember { mutableStateOf<Animal?>(null) }
    var savingAnimal by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val catalogPicks = remember(species) { TerrariumSpeciesCatalog.toSpeciesPicks(species) }
    val podpowiedzi = remember(speciesQuery, species, catalogPicks) {
        mergeSpeciesSuggestions(speciesQuery, catalogPicks, species)
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isEmpty()) return@rememberLauncherForActivityResult
        scope.launch {
            runCatching {
                val cached = withContext(Dispatchers.IO) {
                    uris.onEach { grantPersistableRead(context, it) }
                        .map { importPickedMedia(context, it, "photos") }
                }
                pickedPhotos = pickedPhotos + cached
                onNotify("Dodano ${cached.size} zdjec")
            }.onFailure {
                onNotify(mediaUploadErrorMessage(it))
            }
        }
    }
    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isEmpty()) return@rememberLauncherForActivityResult
        scope.launch {
            runCatching {
                val cached = withContext(Dispatchers.IO) {
                    uris.onEach { grantPersistableRead(context, it) }
                        .map { importPickedMedia(context, it, "videos") }
                }
                pickedVideos = pickedVideos + cached
                onNotify("Dodano ${cached.size} filmow")
            }.onFailure {
                onNotify(mediaUploadErrorMessage(it))
            }
        }
    }

    fun resetForm() {
        editingAnimal = null
        pseudonym = ""
        speciesQuery = ""
        wyborGatunku = null
        trybWlasnyGatunek = false
        customLatin = ""
        customCommon = ""
        stage = "L1"
        notes = ""
        ownedSince = ""
        isPublic = false
        pickedPhotos = emptyList()
        pickedVideos = emptyList()
    }

    fun loadAnimalIntoForm(animal: Animal) {
        editingAnimal = animal
        pseudonym = animal.name
        stage = animal.stage.ifBlank { "L1" }
        notes = animal.notes
        ownedSince = animal.ownedSince
        isPublic = animal.isPublic
        pickedPhotos = emptyList()
        pickedVideos = emptyList()
        when (animal.speciesSource) {
            "custom" -> {
                trybWlasnyGatunek = true
                wyborGatunku = null
                customLatin = animal.speciesLatinName.ifBlank { animal.speciesLabel }
                customCommon = animal.speciesCommonName
                speciesQuery = ""
            }
            "firestore_species" -> {
                trybWlasnyGatunek = false
                customLatin = ""
                customCommon = ""
                val matched = species.find { it.id == animal.speciesId }
                if (matched != null) {
                    wyborGatunku = HodowlaWyborGatunku.BazaFirestore(matched)
                    speciesQuery = matched.latinName
                } else {
                    wyborGatunku = null
                    speciesQuery = animal.speciesLatinName.ifBlank { animal.speciesLabel }
                }
            }
            else -> {
                trybWlasnyGatunek = false
                wyborGatunku = null
                customLatin = ""
                customCommon = ""
                speciesQuery = animal.speciesLatinName.ifBlank { animal.speciesLabel }
            }
        }
        scope.launch { listState.animateScrollToItem(0) }
    }

    fun currentFormState() = HodowlaFormState(
        editingAnimal = editingAnimal,
        pseudonym = pseudonym,
        wyborGatunku = wyborGatunku,
        trybWlasnyGatunek = trybWlasnyGatunek,
        customLatin = customLatin,
        customCommon = customCommon,
        stage = stage,
        notes = notes,
        ownedSince = ownedSince,
        isPublic = isPublic
    )

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(padding).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    if (editingAnimal != null) "Edycja pupila" else "Hodowla",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    if (editingAnimal != null) {
                        "Zmien dane lub dodaj nowe zdjecia/filmy. Gatunek mozesz zostawic bez zmian lub wybrac ponownie."
                    } else {
                        "Wyszukaj gatunek w bazie ptasznikow lub Firestore; mozesz tez dodac wlasny wpis."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = SpiderZoneColors.TextSecondary
                )
                if (editingAnimal != null) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Edytujesz: ${editingAnimal!!.name}",
                        color = SpiderZoneColors.Primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    if (editingAnimal!!.photoUrls.isNotEmpty() || editingAnimal!!.videoUrls.isNotEmpty()) {
                        Text(
                            "Obecne media: ${editingAnimal!!.photoUrls.size} zdj., ${editingAnimal!!.videoUrls.size} film.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SpiderZoneColors.TextSecondary
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                AppOutlinedTextField(
                    value = pseudonym,
                    onValueChange = { pseudonym = it },
                    label = "Pseudonim pupila"
                )
                Spacer(Modifier.height(8.dp))
                AppOutlinedTextField(
                    value = speciesQuery,
                    onValueChange = {
                        speciesQuery = it
                        wyborGatunku = null
                        if (editingAnimal == null) {
                            trybWlasnyGatunek = false
                        }
                    },
                    label = "Szukaj gatunku (łacina lub nazwa zwyczajowa)",
                    placeholder = "np. metallica, Rosea…"
                )
                if (!trybWlasnyGatunek && speciesQuery.isNotBlank() && wyborGatunku == null) {
                    podpowiedzi.take(8).forEach { row ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                                .clickable {
                                    wyborGatunku = row.wybor
                                    speciesQuery = when (val w = row.wybor) {
                                        is HodowlaWyborGatunku.Takson -> w.pick.latinName
                                        is HodowlaWyborGatunku.BazaFirestore -> w.sp.latinName
                                    }
                                },
                            colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(row.label, Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                TextButton(
                    onClick = {
                        trybWlasnyGatunek = true
                        wyborGatunku = null
                        speciesQuery = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Nie ma na liście — dodaj własny gatunek")
                }
                if (trybWlasnyGatunek) {
                    AppOutlinedTextField(
                        value = customLatin,
                        onValueChange = { customLatin = it },
                        label = "Nazwa lacinska (wymagana)"
                    )
                    Spacer(Modifier.height(8.dp))
                    AppOutlinedTextField(
                        value = customCommon,
                        onValueChange = { customCommon = it },
                        label = "Nazwa zwyczajowa (opcjonalnie)"
                    )
                }
                wyborGatunku?.let { w ->
                    Spacer(Modifier.height(6.dp))
                    val opis = when (w) {
                        is HodowlaWyborGatunku.Takson ->
                            "Wybrano: ${w.pick.latinName}${w.pick.commonName?.let { " ($it)" } ?: ""} (baza taksonomii)"
                        is HodowlaWyborGatunku.BazaFirestore ->
                            "Wybrano: ${w.sp.latinName} (${w.sp.commonName}) · Firestore"
                    }
                    Text(opis, style = MaterialTheme.typography.bodySmall, color = SpiderZoneColors.Primary)
                }
                Spacer(Modifier.height(8.dp))
                AppOutlinedTextField(value = stage, onValueChange = { stage = it }, label = "Stadium (np. L4)")
                Spacer(Modifier.height(8.dp))
                AppOutlinedTextField(
                    value = ownedSince,
                    onValueChange = { ownedSince = it },
                    label = "W hodowli od (opcjonalnie)",
                    placeholder = "np. 2024-05-10"
                )
                Spacer(Modifier.height(8.dp))
                AppOutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Notatki o pupilu",
                    singleLine = false,
                    maxLines = 5
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            if (isPublic) "Publiczne (na przyszłość: społeczność)" else "Prywatne",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "Pola zapisujemy w Firestore; multimedia w Storage.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SpiderZoneColors.TextSecondary
                        )
                    }
                    Switch(checked = isPublic, onCheckedChange = { isPublic = it })
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        photoPicker.launch(arrayOf("image/*"))
                    }) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Zdjęcia")
                    }
                    Button(onClick = {
                        videoPicker.launch(arrayOf("video/*"))
                    }) {
                        Icon(Icons.Default.VideoLibrary, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Filmy")
                    }
                }
                if (pickedPhotos.isNotEmpty()) {
                    Text("Wybrane zdjęcia: ${pickedPhotos.size}", Modifier.padding(top = 8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        items(pickedPhotos, key = { it.file.absolutePath }) { media ->
                            Box {
                                AsyncImage(
                                    model = media.file,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(72.dp)
                                        .height(72.dp),
                                    contentScale = ContentScale.Crop
                                )
                                IconButton(
                                    onClick = { pickedPhotos = pickedPhotos.filter { it.file != media.file } },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Usuń", tint = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
                if (pickedVideos.isNotEmpty()) {
                    Text(
                        "Wybrane filmy: ${pickedVideos.size}",
                        Modifier.padding(top = 8.dp),
                        color = SpiderZoneColors.TextSecondary
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            val draft = buildAnimalDraftFromForm(currentFormState())
                            if (draft == null) {
                                onNotify(
                                    when {
                                        pseudonym.isBlank() -> "Podaj pseudonim pupila"
                                        trybWlasnyGatunek -> "Podaj nazwe lacinska gatunku"
                                        editingAnimal != null -> "Wybierz gatunek lub zostaw pole wyszukiwania bez zmian"
                                        else -> "Wybierz gatunek z listy lub dodaj wlasny"
                                    }
                                )
                                return@Button
                            }
                            val existing = editingAnimal
                            if (savingAnimal) return@Button
                            scope.launch {
                                savingAnimal = true
                                runCatching {
                                    withContext(Dispatchers.IO) {
                                        if (existing != null) {
                                            onUpdateAnimal(context, existing, draft, pickedPhotos, pickedVideos)
                                        } else {
                                            onSubmitAnimal(context, draft, pickedPhotos, pickedVideos)
                                        }
                                    }
                                    resetForm()
                                    onNotify(
                                        if (existing != null) "Zaktualizowano pupila"
                                        else "Zapisano pupila w hodowli"
                                    )
                                }.onFailure {
                                    val msg = it.message.orEmpty()
                                    onNotify(
                                        when {
                                            msg.contains("PERMISSION_DENIED", ignoreCase = true) ->
                                                "Brak uprawnien Firestore. Wgraj reguly z firestore.rules."
                                            else -> mediaUploadErrorMessage(it)
                                        }
                                    )
                                }
                                savingAnimal = false
                            }
                        },
                        enabled = !savingAnimal,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            when {
                                savingAnimal -> "Zapisywanie..."
                                editingAnimal != null -> "Zapisz zmiany"
                                else -> "Dodaj do hodowli"
                            }
                        )
                    }
                    if (editingAnimal != null) {
                        TextButton(onClick = { resetForm() }) {
                            Text("Anuluj")
                        }
                    }
                }
            }
            item {
                Text("Twoja hodowla", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            items(
                animals,
                key = { a -> a.id.ifBlank { "animal-${a.name}-${a.createdAt}" } }
            ) { a ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.fillMaxWidth().padding(14.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewingAnimal = a },
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RemoteImage(
                                    url = a.photoUrls.firstOrNull(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(56.dp)
                                        .height(56.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop,
                                    placeholderIcon = Icons.Default.Pets
                                )
                                Column {
                                    Text(a.name, fontWeight = FontWeight.Bold)
                                    Text(a.speciesDisplay(), color = SpiderZoneColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        "${a.stage} · ${if (a.isPublic) "Publiczne" else "Prywatne"}",
                                        color = SpiderZoneColors.TextSecondary,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            IconButton(onClick = { loadAnimalIntoForm(a) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edytuj")
                            }
                        }
                        if (a.notes.isNotBlank()) {
                            Spacer(Modifier.height(6.dp))
                            Text(a.notes, color = SpiderZoneColors.TextSecondary, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
        viewingAnimal?.let { animal ->
            AnimalDetailOverlay(animal = animal, isMine = true, onDismiss = { viewingAnimal = null })
        }
    }
}

@Composable
private fun CommunityScreen(
    padding: PaddingValues,
    myUid: String,
    items: List<CommunityAnimalItem>,
    loading: Boolean,
    repository: SpiderZoneRepository,
    onOpenSpecies: (Animal) -> Unit,
    onOpenOwnerProfile: (String) -> Unit,
    onRefresh: () -> Unit
) {
    var selectedItem by remember { mutableStateOf<CommunityAnimalItem?>(null) }

    Box(
        Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Spolecznosc", style = MaterialTheme.typography.titleLarge)
                            Text(
                                "Kliknij kafelek, aby zobaczyc szczegoly pupila.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SpiderZoneColors.TextSecondary
                            )
                        }
                        if (loading) {
                            CircularProgressIndicator(modifier = Modifier.height(24.dp))
                        } else {
                            TextButton(onClick = onRefresh) {
                                Text("Odswiez")
                            }
                        }
                    }
                }
                if (loading && items.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                if (!loading && items.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            "Brak publicznych pupili. W hodowli dodaj zwierze z wlaczonym „Publiczne”, potem kliknij Odswiez.",
                            color = SpiderZoneColors.TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                if (!loading && items.isNotEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            "Publicznych pupili: ${items.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = SpiderZoneColors.Primary
                        )
                    }
                }
                items(items, key = { "${it.ownerUid}-${it.animal.id}" }) { row ->
                    CommunityAnimalTile(
                        item = row,
                        isMine = myUid.isNotBlank() && row.ownerUid == myUid,
                        onClick = { selectedItem = row },
                        onOwnerClick = { onOpenOwnerProfile(row.ownerUid) }
                    )
                }
            }

        selectedItem?.let { item ->
            CommunityAnimalDetailOverlay(
                item = item,
                isMine = myUid.isNotBlank() && item.ownerUid == myUid,
                onDismiss = { selectedItem = null },
                onOwnerClick = { onOpenOwnerProfile(item.ownerUid) },
                onSpeciesClick = { onOpenSpecies(item.animal) }
            )
        }
    }
}

@Composable
private fun CommunityAnimalTile(
    item: CommunityAnimalItem,
    isMine: Boolean,
    onClick: () -> Unit,
    onOwnerClick: () -> Unit
) {
    val animal = item.animal
    val owner = item.ownerProfile
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(118.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
            ) {
                RemoteImage(
                    url = animal.photoUrls.firstOrNull(),
                    contentDescription = animal.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholderIcon = Icons.Default.Pets
                )
                if (isMine) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = SpiderZoneColors.Primary.copy(alpha = 0.92f)
                    ) {
                        Text(
                            "Twoj",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
            Column(Modifier.padding(10.dp)) {
                Text(
                    animal.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    animal.speciesDisplay(),
                    style = MaterialTheme.typography.bodySmall,
                    color = SpiderZoneColors.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    animal.stage.ifBlank { "—" },
                    style = MaterialTheme.typography.labelMedium,
                    color = SpiderZoneColors.Primary
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onOwnerClick),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    RemoteImage(
                        url = owner?.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(22.dp)
                            .clip(RoundedCornerShape(11.dp)),
                        contentScale = ContentScale.Crop,
                        placeholderIcon = Icons.Default.Person
                    )
                    Text(
                        owner?.displayName() ?: "Hodowca",
                        style = MaterialTheme.typography.labelSmall,
                        color = SpiderZoneColors.TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun CommunityAnimalDetailOverlay(
    item: CommunityAnimalItem,
    isMine: Boolean,
    onDismiss: () -> Unit,
    onOwnerClick: () -> Unit,
    onSpeciesClick: () -> Unit
) {
    AnimalDetailOverlay(
        animal = item.animal,
        isMine = isMine,
        ownerProfile = item.ownerProfile,
        onDismiss = onDismiss,
        onOwnerClick = onOwnerClick,
        onSpeciesClick = onSpeciesClick
    )
}

@Composable
private fun AnimalDetailOverlay(
    animal: Animal,
    isMine: Boolean,
    ownerProfile: PublicUserProfile? = null,
    onDismiss: () -> Unit,
    onOwnerClick: (() -> Unit)? = null,
    onSpeciesClick: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SpiderZoneColors.Background
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wstecz")
                }
                Text(
                    animal.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(14.dp))
                    ) {
                        RemoteImage(
                            url = animal.photoUrls.firstOrNull(),
                            contentDescription = animal.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholderIcon = Icons.Default.Pets
                        )
                    }
                }
                if (ownerProfile != null && onOwnerClick != null) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onOwnerClick),
                            colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                RemoteImage(
                                    url = ownerProfile.avatarUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(22.dp)),
                                    placeholderIcon = Icons.Default.Person
                                )
                                Column {
                                    Text("Dodal", style = MaterialTheme.typography.labelSmall, color = SpiderZoneColors.TextSecondary)
                                    Text(ownerProfile.displayName(), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                item {
                    if (isMine) {
                        Text(
                            "Twoj publiczny pupil",
                            color = SpiderZoneColors.Primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                item {
                    Text("Gatunek", style = MaterialTheme.typography.labelMedium, color = SpiderZoneColors.TextSecondary)
                    Text(
                        animal.speciesDisplay(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = SpiderZoneColors.Primary,
                        modifier = if (onSpeciesClick != null) Modifier.clickable(onClick = onSpeciesClick) else Modifier,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (onSpeciesClick != null) {
                        Text(
                            "Kliknij nazwe — opis z bazy Firestore, ptasnikow (Home) lub wpisu wlasnego",
                            style = MaterialTheme.typography.bodySmall,
                            color = SpiderZoneColors.TextSecondary
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    CommunityDetailRow("Stadium", animal.stage.ifBlank { "—" })
                    CommunityDetailRow("Plec", formatAnimalSex(animal.sex))
                    CommunityDetailRow("Data urodzenia", animal.birthDate.ifBlank { "—" })
                    CommunityDetailRow("W hodowli od", animal.ownedSince.ifBlank { "—" })
                    CommunityDetailRow("Dodano", formatEpochMillis(animal.createdAt))
                    if (animal.photoUrls.isNotEmpty() || animal.videoUrls.isNotEmpty()) {
                        CommunityDetailRow(
                            "Media",
                            "${animal.photoUrls.size} zdjec, ${animal.videoUrls.size} filmow"
                        )
                    }
                }
                if (animal.notes.isNotBlank()) {
                    item {
                        Text("Notatki", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Text(
                            animal.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SpiderZoneColors.TextSecondary
                        )
                    }
                }
                if (animal.photoUrls.size > 1) {
                    item {
                        Text("Wiecej zdjec", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(animal.photoUrls.drop(1)) { url ->
                                RemoteImage(
                                    url = url,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(120.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
                if (animal.videoUrls.isNotEmpty()) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.VideoLibrary,
                                contentDescription = null,
                                tint = SpiderZoneColors.Primary
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Do pupila dolaczono ${animal.videoUrls.size} filmow.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SpiderZoneColors.TextSecondary
                            )
                        }
                    }
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun CommunityDetailRow(label: String, value: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = SpiderZoneColors.TextSecondary)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun formatAnimalSex(sex: String): String = when (sex.lowercase()) {
    "male", "samiec", "m" -> "Samiec"
    "female", "samica", "f" -> "Samica"
    "unknown", "nieznana", "" -> "Nieznana"
    else -> sex
}

private fun formatEpochMillis(epoch: Long): String {
    if (epoch <= 0L) return "—"
    return Instant.ofEpochMilli(epoch)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
}

@Composable
private fun RemindersScreen(
    padding: PaddingValues,
    reminders: List<Reminder>,
    onAddReminder: suspend (Reminder) -> Unit
) {
    var title by remember { mutableStateOf("Karmienie") }
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().padding(padding).padding(12.dp)) {
        Text("Przypomnienia", style = MaterialTheme.typography.titleLarge)
            AppOutlinedTextField(value = title, onValueChange = { title = it }, label = "Tytul")
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                scope.launchCatching {
                    onAddReminder(
                        Reminder(
                            title = title,
                            dueEpochMillis = System.currentTimeMillis() + 6 * 60 * 60 * 1000
                        )
                    )
                }
            }) { Text("Dodaj (za 6h)") }
            Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(reminders) { r ->
                StatCard(r.title, "Termin: ${r.dueEpochMillis}")
            }
        }
    }
}

@Composable
private fun ProfileScreen(
    padding: PaddingValues,
    uid: String,
    email: String,
    profile: UserProfile?,
    animals: List<Animal>,
    repository: SpiderZoneRepository,
    themeMode: AppThemeMode,
    onThemeModeChange: (AppThemeMode) -> Unit,
    onProfileUpdated: (UserProfile?) -> Unit,
    onNotify: (String) -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var bioDraft by remember(profile?.bio) { mutableStateOf(profile?.bio.orEmpty()) }
    var savingBio by remember { mutableStateOf(false) }
    var savingAvatar by remember { mutableStateOf(false) }
    var resetLoading by remember { mutableStateOf(false) }
    var selectedAnimal by remember { mutableStateOf<Animal?>(null) }
    var showSettings by remember { mutableStateOf(false) }

    LaunchedEffect(profile?.bio) {
        bioDraft = profile?.bio.orEmpty()
    }

    val avatarPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null || uid.isBlank()) return@rememberLauncherForActivityResult
        scope.launch {
            savingAvatar = true
            runCatching {
                repository.updateUserAvatar(context, uid, uri)
                onProfileUpdated(repository.getUserProfile(uid))
                onNotify("Zaktualizowano avatar")
            }.onFailure {
                onNotify(it.message ?: "Nie udalo sie zapisac avatara")
            }
            savingAvatar = false
        }
    }

    val displayName = profile?.nickname?.takeIf { it.isNotBlank() }
        ?: profile?.firstName?.takeIf { it.isNotBlank() }
        ?: "Hodowca"
    val joinLabel = formatEpochMillis(profile?.createdAt ?: 0L)

    Box(Modifier.fillMaxSize().padding(padding)) {
        LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Text("Profil", style = MaterialTheme.typography.titleLarge)
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                Modifier
                                    .size(96.dp)
                                    .clip(RoundedCornerShape(48.dp))
                                    .background(SpiderZoneColors.Background)
                                    .clickable(enabled = !savingAvatar) {
                                        avatarPicker.launch("image/*")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                RemoteImage(
                                    url = profile?.avatarUrl,
                                    contentDescription = "Avatar",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    placeholderIcon = Icons.Default.Person
                                )
                                if (savingAvatar) {
                                    CircularProgressIndicator(Modifier.size(32.dp))
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            if (profile?.nickname?.isNotBlank() == true && profile.firstName.isNotBlank()) {
                                Text(profile.firstName + " " + profile.lastName, color = SpiderZoneColors.TextSecondary)
                            }
                            Text("@${profile?.nickname?.ifBlank { "—" } ?: "—"}", color = SpiderZoneColors.Primary)
                            Text("Dolaczono: $joinLabel", style = MaterialTheme.typography.bodySmall, color = SpiderZoneColors.TextSecondary)
                            Text(email, style = MaterialTheme.typography.bodySmall, color = SpiderZoneColors.TextSecondary)
                            if (profile?.bio?.isNotBlank() == true && !showSettings) {
                                Spacer(Modifier.height(8.dp))
                                Text(profile.bio, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        StatCard("Zwierzeta", animals.size.toString(), modifier = Modifier.weight(1f))
                        StatCard(
                            "Gatunki",
                            animals.map { it.speciesLatinName.ifBlank { it.speciesLabel }.ifBlank { it.speciesCommonName } }.distinct().count().toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Twoje zwierzeta", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        TextButton(onClick = { showSettings = !showSettings }) {
                            Icon(Icons.Default.Settings, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(if (showSettings) "Profil" else "Ustawienia")
                        }
                    }
                }
                if (!showSettings) {
                    if (animals.isEmpty()) {
                        item {
                            Text(
                                "Nie masz jeszcze zwierzat w hodowli.",
                                color = SpiderZoneColors.TextSecondary
                            )
                        }
                    } else {
                        items(animals, key = { it.id }) { animal ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedAnimal = animal },
                                colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    RemoteImage(
                                        url = animal.photoUrls.firstOrNull(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop,
                                        placeholderIcon = Icons.Default.Pets
                                    )
                                    Column(Modifier.weight(1f)) {
                                        Text(animal.name, fontWeight = FontWeight.Bold)
                                        Text(
                                            animal.speciesDisplay(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = SpiderZoneColors.TextSecondary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Ustawienia konta", fontWeight = FontWeight.Bold)
                                ThemeModeSelector(
                                    selected = themeMode,
                                    onSelected = onThemeModeChange
                                )
                                CommunityDetailRow("Email", email)
                                AppOutlinedTextField(
                                    value = bioDraft,
                                    onValueChange = { bioDraft = it },
                                    label = "Opis profilu",
                                    singleLine = false,
                                    maxLines = 6
                                )
                                Button(
                                    onClick = {
                                        if (uid.isBlank()) return@Button
                                        scope.launch {
                                            savingBio = true
                                            runCatching {
                                                repository.updateUserProfile(uid, mapOf("bio" to bioDraft.trim()))
                                                onProfileUpdated(repository.getUserProfile(uid))
                                                onNotify("Zapisano opis profilu")
                                            }.onFailure {
                                                onNotify(it.message ?: "Blad zapisu opisu")
                                            }
                                            savingBio = false
                                        }
                                    },
                                    enabled = !savingBio,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(if (savingBio) "Zapisywanie..." else "Zapisz opis")
                                }
                                TextButton(
                                    onClick = {
                                        scope.launch {
                                            resetLoading = true
                                            runCatching {
                                                repository.resetPassword(email)
                                                onNotify("Wyslano link resetujacy haslo na $email")
                                            }.onFailure {
                                                onNotify(it.message ?: "Blad resetu hasla")
                                            }
                                            resetLoading = false
                                        }
                                    },
                                    enabled = !resetLoading && email.isNotBlank(),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(if (resetLoading) "Wysylanie..." else "Reset hasla (email)")
                                }
                                Text(
                                    "Kliknij avatar na gorze, aby zmienic zdjecie profilowe.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SpiderZoneColors.TextSecondary
                                )
                            }
                        }
                    }
                }
                item {
                    Button(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) {
                        Text("Wyloguj")
                    }
                }
            }
        selectedAnimal?.let { animal ->
            AnimalDetailOverlay(animal = animal, isMine = true, onDismiss = { selectedAnimal = null })
        }
    }
}

@Composable
private fun RemoteImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderIcon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Default.Pets
) {
    if (isRemoteUrl(url)) {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Box(
            modifier = modifier.background(SpiderZoneColors.Background),
            contentAlignment = Alignment.Center
        ) {
            Icon(placeholderIcon, contentDescription = contentDescription, tint = SpiderZoneColors.Primary)
        }
    }
}

@Composable
private fun PublicUserProfileOverlay(
    ownerUid: String,
    myUid: String,
    repository: SpiderZoneRepository,
    onDismiss: () -> Unit,
    onOpenSpecies: (Animal) -> Unit
) {
    var profile by remember { mutableStateOf<PublicUserProfile?>(null) }
    var animals by remember { mutableStateOf<List<Animal>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var selectedAnimal by remember { mutableStateOf<Animal?>(null) }

    LaunchedEffect(ownerUid) {
        loading = true
        runCatching {
            profile = repository.getPublicProfile(ownerUid)
            animals = repository.publicAnimalsByOwner(ownerUid)
        }
        loading = false
    }

    Surface(modifier = Modifier.fillMaxSize(), color = SpiderZoneColors.Background) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wstecz")
                }
                Text("Profil hodowcy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                RemoteImage(
                                    url = profile?.avatarUrl,
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .size(88.dp)
                                        .clip(RoundedCornerShape(44.dp)),
                                    placeholderIcon = Icons.Default.Person
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(profile?.displayName() ?: "Hodowca", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                if (profile?.bio?.isNotBlank() == true) {
                                    Spacer(Modifier.height(6.dp))
                                    Text(profile!!.bio, color = SpiderZoneColors.TextSecondary)
                                }
                                Text(
                                    "Dolaczono: ${formatEpochMillis(profile?.createdAt ?: 0L)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SpiderZoneColors.TextSecondary
                                )
                                Text(
                                    "Publicznych pupili: ${animals.size}",
                                    color = SpiderZoneColors.Primary,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                    items(animals, key = { it.id }) { animal ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedAnimal = animal },
                            colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                RemoteImage(
                                    url = animal.photoUrls.firstOrNull(),
                                    contentDescription = null,
                                    modifier = Modifier.size(52.dp).clip(RoundedCornerShape(8.dp)),
                                    placeholderIcon = Icons.Default.Pets
                                )
                                Column(Modifier.weight(1f)) {
                                    Text(animal.name, fontWeight = FontWeight.Bold)
                                    Text(animal.speciesDisplay(), style = MaterialTheme.typography.bodySmall, color = SpiderZoneColors.TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    selectedAnimal?.let { animal ->
        AnimalDetailOverlay(
            animal = animal,
            isMine = ownerUid == myUid,
            ownerProfile = profile,
            onDismiss = { selectedAnimal = null },
            onSpeciesClick = { onOpenSpecies(animal) }
        )
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier.fillMaxWidth()) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SpiderZoneColors.Surface)
    ) {
        Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title)
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String? = null,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else 5
) {
    val hasError = errorMessage != null
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        isError = hasError,
        supportingText = errorMessage?.let { msg -> { Text(msg) } },
        singleLine = singleLine,
        maxLines = maxLines,
        textStyle = TextStyle(color = SpiderZoneColors.TextPrimary),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = SpiderZoneColors.TextPrimary,
            unfocusedTextColor = SpiderZoneColors.TextPrimary,
            focusedLabelColor = SpiderZoneColors.TextSecondary,
            unfocusedLabelColor = SpiderZoneColors.TextSecondary,
            cursorColor = SpiderZoneColors.Primary,
            errorTextColor = SpiderZoneColors.TextPrimary,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

private fun scheduleReminder(context: Context, reminder: Reminder) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("title", reminder.title)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        reminder.title.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.dueEpochMillis, pendingIntent)
}

private fun CoroutineScope.launchCatching(block: suspend () -> Unit) {
    this.launch {
        runCatching { block() }
    }
}