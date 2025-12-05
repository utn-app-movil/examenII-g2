package cr.ac.utn.appmovil.rooms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utn.rooms.repository.meg_Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class meg_CreateRoomViewModel : ViewModel() {
    private val repo = meg_Repository()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun createRoom(name: String, capacity: Int) {
        scope.launch {
            runCatching { repo.createRoom(name, capacity) }
                .onSuccess { resp -> _message.postValue(resp.message) }
                .onFailure { e -> _message.postValue("Error: ${e.localizedMessage}") }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
