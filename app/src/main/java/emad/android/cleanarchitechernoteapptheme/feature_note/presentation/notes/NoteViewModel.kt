package emad.android.cleanarchitechernoteapptheme.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import emad.android.cleanarchitechernoteapptheme.feature_note.domain.model.Note
import emad.android.cleanarchitechernoteapptheme.feature_note.domain.use_case.NoteUseCases
import emad.android.cleanarchitechernoteapptheme.feature_note.domain.util.NoteOrder
import emad.android.cleanarchitechernoteapptheme.feature_note.domain.util.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel(){
    private val _state = mutableStateOf(NoteState())
    val state: State<NoteState> = _state

    private var recentDeletedNote: Note? = null
    private var getNotesJob: Job? = null
    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NoteEvent){
        when (event) {
            is NoteEvent.Order -> {
                if(state.value.noteOrder::class == event.noteOrder::class &&
                        state.value.noteOrder.orderType == event.noteOrder.orderType) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNoteUseCase(event.note)
                    recentDeletedNote = event.note
                }
            }
            is NoteEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentDeletedNote ?: return@launch)
                    recentDeletedNote = null
                }
            }
            is NoteEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }
    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotesUseCase(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}