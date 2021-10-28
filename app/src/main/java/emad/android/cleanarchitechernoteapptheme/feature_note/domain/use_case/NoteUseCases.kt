package emad.android.cleanarchitechernoteapptheme.feature_note.domain.use_case

data class NoteUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNote: AddNote,
    val getNote: GetNote
)
