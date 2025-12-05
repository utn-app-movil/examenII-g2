package cr.ac.utn.appmovil.rooms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utn.rooms.repository.meg_Repository
import cr.ac.utn.appmovil.rooms.model.MegRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class meg_RoomViewModel : ViewModel() {
    private val repo = meg_Repository()

    private val _rooms = MutableLiveData<List<MegRoom>>()
    val rooms: LiveData<List<MegRoom>> = _rooms

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    // Creamos un scope manual en vez de viewModelScope
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun loadRooms() {
        scope.launch {
            runCatching { repo.getRooms() }
                .onSuccess { resp ->
                    _rooms.postValue(resp.data ?: emptyList())
                    _message.postValue(resp.message)
                }
                .onFailure { e ->
                    _rooms.postValue(emptyList())
                    _message.postValue("Error: ${e.localizedMessage}")
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
