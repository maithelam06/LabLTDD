package com.example.lab7

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CourseUiState(
    val name: String = "",
    val duration: String = "",
    val description: String = "",
    val editName: String = "",
    val editDuration: String = "",
    val editDescription: String = "",
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val pendingDeleteId: String? = null,
    val editingCourseId: String? = null,
    val message: String? = null,
)

class CourseViewModel : ViewModel() {
    companion object {
        private const val TAG = "CourseViewModel"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val coursesCollection = firestore.collection("courses")

    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    private var snapshotListener: ListenerRegistration? = null

    init {
        observeCourses()
    }

    fun updateName(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun updateDuration(value: String) {
        _uiState.value = _uiState.value.copy(duration = value)
    }

    fun updateDescription(value: String) {
        _uiState.value = _uiState.value.copy(description = value)
    }

    fun updateEditName(value: String) {
        _uiState.value = _uiState.value.copy(editName = value)
    }

    fun updateEditDuration(value: String) {
        _uiState.value = _uiState.value.copy(editDuration = value)
    }

    fun updateEditDescription(value: String) {
        _uiState.value = _uiState.value.copy(editDescription = value)
    }

    fun addCourse() {
        val state = _uiState.value
        val name = state.name.trim()
        val duration = state.duration.trim()
        val description = state.description.trim()

        if (name.isEmpty() || duration.isEmpty() || description.isEmpty()) {
            showMessage("Vui long nhap day du thong tin.")
            return
        }

        _uiState.value = state.copy(isSaving = true)

        val document = coursesCollection.document()
        val course = Course(
            id = document.id,
            name = name,
            duration = duration,
            description = description
        )

        document.set(course)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        name = "",
                        duration = "",
                        description = "",
                        isSaving = false,
                        message = "Them khoa hoc thanh cong."
                    )
                } else {
                    val error = task.exception
                    Log.e(TAG, "Add course failed", error)
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        message = error?.message ?: "Khong the them khoa hoc."
                    )
                }
            }
    }

    fun showEditDialog(course: Course) {
        _uiState.value = _uiState.value.copy(
            editingCourseId = course.id,
            editName = course.name,
            editDuration = course.duration,
            editDescription = course.description
        )
    }

    fun hideEditDialog() {
        _uiState.value = _uiState.value.copy(
            editingCourseId = null,
            editName = "",
            editDuration = "",
            editDescription = ""
        )
    }

    fun updateCourse() {
        val state = _uiState.value
        val editingId = state.editingCourseId ?: return
        val name = state.editName.trim()
        val duration = state.editDuration.trim()
        val description = state.editDescription.trim()

        if (name.isEmpty() || duration.isEmpty() || description.isEmpty()) {
            showMessage("Thong tin sua khong duoc de trong.")
            return
        }

        _uiState.value = state.copy(isSaving = true)

        coursesCollection.document(editingId)
            .set(
                Course(
                    id = editingId,
                    name = name,
                    duration = duration,
                    description = description
                )
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        editingCourseId = null,
                        editName = "",
                        editDuration = "",
                        editDescription = "",
                        message = "Cap nhat thanh cong."
                    )
                } else {
                    val error = task.exception
                    Log.e(TAG, "Update course failed", error)
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        message = error?.message ?: "Khong the cap nhat khoa hoc."
                    )
                }
            }
    }

    fun deleteCourse(course: Course) {
        _uiState.value = _uiState.value.copy(pendingDeleteId = course.id)

        coursesCollection.document(course.id)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        pendingDeleteId = null,
                        message = "Da xoa ${course.name}."
                    )
                } else {
                    val error = task.exception
                    Log.e(TAG, "Delete course failed", error)
                    _uiState.value = _uiState.value.copy(
                        pendingDeleteId = null,
                        message = error?.message ?: "Khong the xoa khoa hoc."
                    )
                }
            }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    private fun observeCourses() {
        snapshotListener?.remove()
        snapshotListener = coursesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Observe courses failed", error)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = error.message ?: "Khong the tai danh sach khoa hoc."
                )
                return@addSnapshotListener
            }

            val courses = snapshot?.documents
                ?.mapNotNull { document ->
                    document.toObject(Course::class.java)?.copy(id = document.id)
                }
                ?.sortedBy { it.name.lowercase() }
                .orEmpty()

            _uiState.value = _uiState.value.copy(
                courses = courses,
                isLoading = false
            )
        }
    }

    private fun showMessage(message: String) {
        _uiState.value = _uiState.value.copy(message = message)
    }

    override fun onCleared() {
        snapshotListener?.remove()
        super.onCleared()
    }
}
