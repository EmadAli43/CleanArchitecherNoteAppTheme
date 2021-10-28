package emad.android.cleanarchitechernoteapptheme.feature_note.presentation.notes

import emad.android.cleanarchitechernoteapptheme.feature_note.domain.model.Note
import emad.android.cleanarchitechernoteapptheme.feature_note.domain.util.NoteOrder
import emad.android.cleanarchitechernoteapptheme.feature_note.domain.util.OrderType

data class NoteState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
